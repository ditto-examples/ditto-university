import DittoSwift
import Foundation

// MARK: DataManager Protocol
@MainActor protocol DataManager: ObservableObject {
    var appManager: AppManager { get set }
    var tasks: [TaskModel] { get set }
    var isPresentingEditScreen: Bool { get set }
    var selectedTaskModel: TaskModel? { get set }
    
    func initialize() async throws
    func populateTaskCollection() async throws
    func setSyncEnabled(_ newValue: Bool) throws
    func insertTaskModel(_ task: TaskModel) async
    func updateTaskModel(_ task: TaskModel) async
    func toggleComplete(task: TaskModel) async
    func deleteTaskModel(_ task: TaskModel) async
    
}

// MARK: DittoManager Implementation
@MainActor class DittoManager: ObservableObject, DataManager {
    
    @Published var tasks = [TaskModel]()
    @Published var isPresentingEditScreen: Bool = false
    @Published var selectedTaskModel: TaskModel?
    
    var subscription: DittoSyncSubscription?
    var storeObserver: DittoStoreObserver?
    var ditto: Ditto?
    
    var appManager: AppManager
    
    init(appManager: AppManager) {
        //cache state for future use
        self.appManager = appManager
    }
    
    func initialize() async throws {
        // setup logging
        DittoLogger.enabled = true
        DittoLogger.minimumLogLevel = .debug
        //setup logging level
        let isPreview: Bool =
        ProcessInfo.processInfo.environment["XCODE_RUNNING_FOR_PREVIEWS"] == "1"
        if !isPreview {
            DittoLogger.minimumLogLevel = .debug
        }
        
        //TODO: setup Ditto Identity
        ditto = Ditto(
            identity: .onlinePlayground(
                appID: appManager.appConfig.appID,
                token: appManager.appConfig.authToken,
                enableDittoCloudSync: false,
                customAuthURL: URL(string: appManager.appConfig.authURL)
            )
        )
        // Set the Ditto Websocket URL
        var config = DittoTransportConfig()
        config.connect.webSocketURLs.insert(appManager.appConfig.websocketURL)
        
        // TODO: enable all P2P transports
        config.enableAllPeerToPeer()
        ditto?.transportConfig = config
        
        do {
            var syncScopes = [
                "tasks": "SmallPeersOnly"
            ];
            try await ditto?.store.execute(
                query: "ALTER SYSTEM SET USER_COLLECTION_SYNC_SCOPES = :syncScopes",
                arguments: ["syncScopes": syncScopes]);
            
            //Disable sync with v3 peers, required for DQL
            try ditto?.disableSyncWithV3()
            
            //setup the collection with initial data and then setup observer
            try await self.populateTaskCollection()
            try self.registerObservers()
            
        } catch let error {
            self.appManager.setError(error)
        }
    }
    
    /// Performs cleanup of Ditto resources
    ///
    /// This method handles the graceful shutdown of Ditto components by:
    /// - Cancelling any active subscriptions
    /// - Cancelling store observers
    /// - Stopping the Ditto sync process
    deinit {
        subscription?.cancel()
        subscription = nil
        
        storeObserver?.cancel()
        storeObserver = nil
        
        if let dittoInstance = ditto {
            if dittoInstance.isSyncActive {
                dittoInstance.stopSync()
            }
        }
    }
    
}

// MARK:  Populate Initial Tasks for Task Collection
extension DittoManager {
    
    /// Populates the Ditto tasks collection with initial seed data if it's empty
    ///
    /// This method creates a set of predefined tasks in the Ditto store by:
    /// - Defining an array of initial `TaskModel` objects with unique IDs and titles
    /// - Inserting each task into the Ditto store using DQL (Ditto Query Language)
    /// - Using the INITIAL keyword to only insert if documents don't already exist
    ///
    /// The initial tasks include common todo items like:
    /// - Buy groceries
    /// - Clean the kitchen
    /// - Schedule dentist appointment
    /// - Pay bills
    ///
    /// - SeeAlso: https://docs.ditto.live/sdk/latest/crud/read#using-args-to-query-dynamic-values///
    ///
    /// - Throws: A DittoError if the insert operations fail
    func populateTaskCollection() async throws {
        
        let initialTasks: [TaskModel] = [
            TaskModel(
                _id: "50191411-4C46-4940-8B72-5F8017A04FA811",
                title: "Buy groceries"),
            TaskModel(
                _id: "6DA283DA-8CFE-4526-A6FA-D385089364E811",
                title: "Clean the kitchen"),
            TaskModel(
                _id: "5303DDF8-0E72-4FEB-9E82-4B007E5797F911",
                title: "Schedule dentist appointment"),
            TaskModel(
                _id: "38411F1B-6B49-4346-90C3-0B16CE97E17911",
                title: "Pay bills"),
        ]
        
        for task in initialTasks {
            do {
                if let dittoInstance = ditto {
                    
                    // https://docs.ditto.live/sdk/latest/crud/write#inserting-documents
                    try await dittoInstance.store.execute(
                        query: "INSERT INTO tasks INITIAL DOCUMENTS (:task)",
                        arguments: [
                            "task":
                                [
                                    "_id": task._id,
                                    "title": task.title,
                                    "done": task.done,
                                    "deleted": task.deleted,
                                ]
                        ]
                    )
                }
            } catch {
                appManager.setError(error)
            }
        }
    }
}

// MARK: Register Observer - Live Query
extension DittoManager {
    /// Registers observers for the planets collection to handle real-time updates.
    ///
    /// This method sets up a live query observer that:
    /// - Monitors the tasks collection for changes
    /// - Updates the @Published tasks array when changes occur
    /// - Filters out tasks NOT deleted
    ///
    /// - SeeAlso: https://docs.ditto.live/sdk/latest/crud/read
    ///
    /// - Throws: A DittoError if the observer cannot be registered
    func registerObservers() throws {
        if let dittoInstance = ditto {
            
            let observerQuery = "SELECT * FROM tasks WHERE NOT deleted"
            storeObserver = try dittoInstance.store.registerObserver(query: observerQuery) {
                [weak self] results in
                Task { @MainActor in
                    // Create new TaskModel instances and update the published property
                    self?.tasks = results.items.compactMap {
                        TaskModel($0.jsonString())
                    }
                }
            }
        }
    }
}

// MARK: Subscriptions and Sync

/// Sets up the initial subscription to the tasks collection in Ditto.
///
/// This subscription ensures that changes to the tasks collection are synced
/// between the local Ditto store the BigPeer or other SmallPeers
///
/// - SeeAlso: https://docs.ditto.live/sdk/latest/sync/syncing-data#creating-subscriptions
///
extension DittoManager {
    
    func setSyncEnabled(_ newValue: Bool) {
        if let dittoInstance = ditto {
            if !dittoInstance.isSyncActive && newValue {
                startSync()
            } else if dittoInstance.isSyncActive && !newValue {
                stopSync()
            }
        }
    }
    
    private func startSync() {
        do {
            if let dittoInstance = ditto {
                
                // https://docs.ditto.live/sdk/latest/install-guides/swift#integrating-and-initializing-sync
                try dittoInstance.startSync()
                
                // https://docs.ditto.live/sdk/latest/sync/syncing-data#creating-subscriptions
                let subscriptionQuery = "SELECT * from tasks"
                subscription = try dittoInstance.sync.registerSubscription(query: subscriptionQuery)
            }
        } catch {
            appManager.setError(error)
        }
    }
    
    private func stopSync() {
        //
        //TODO: implement stopping sync by calling cancel and setting object to nil
        //and then running stopSync function
        // https://docs.ditto.live/sdk/latest/sync/syncing-data#canceling-subscriptions
        //
        subscription?.cancel()
        subscription = nil
        
        ditto?.stopSync()
    }
}

// MARK: Adding/Editing Tasks
extension DittoManager {
    
    /// Creates a new TaskModel document in the Ditto store.
    ///
    /// This method:
    /// - Creates a new document in the tasks collection
    ///
    /// - Parameter task: The TaskModel to add to the store
    /// - SeeAlso: https://docs.ditto.live/sdk/latest/crud/create#creating-documents
    ///
    /// - Throws: A DittoError if the insert operation fails
    func insertTaskModel(_ task: TaskModel) async {
        let newTask = task.value
        
        // https://docs.ditto.live/dql/insert
        // https://docs.ditto.live/sdk/latest/crud/create#creating-documents
        let query = "INSERT INTO tasks DOCUMENTS (:newTask)"
        
        do {
            if let dittoInstance = ditto {
                
                // https://docs.ditto.live/sdk/latest/crud/create#creating-documents
                try await dittoInstance.store.execute(
                    query: query, arguments: ["newTask": newTask])
            }
        } catch {
            appManager.setError(error)
        }
    }
    
    /// Updates an existing TaskModel's properties in the Ditto store.
    ///
    /// This method uses DQL to update all mutable fields of the TaskModel
    ///
    /// - Parameter planet: The TaskModel object containing the updated values
    /// - SeeAlso: https://docs.ditto.live/sdk/latest/crud/update#updating
    ///
    /// - Throws: A DittoError if the update operation fails
    func updateTaskModel(_ task: TaskModel) async {
        // https://docs.ditto.live/dql/update#basic-update
        // https://docs.ditto.live/sdk/latest/crud/update#updating
        let query = """
                    UPDATE tasks SET
                    title = :title,
                    done = :done,
                    deleted = :deleted
                    WHERE _id == :_id
                    """
        
        do {
            if let dittoInstance = ditto {
                
                // https://docs.ditto.live/sdk/latest/crud/update#updating
                try await dittoInstance.store.execute(
                    query: query,
                    arguments: [
                        "title": task.title,
                        "done": task.done,
                        "deleted": task.deleted,
                        "_id": task._id,
                    ]
                )
            }
        } catch {
            appManager.setError(error)
        }
        
    }
    
}

// MARK: Updating TaskModel Complete
extension DittoManager {
    
    /// Toggles the completion status of a task in the Ditto store
    ///
    /// This method:
    /// - Inverts the current 'done' status of the task
    /// - Updates only the 'done' field in the store using DQL
    /// - Maintains all other task properties unchanged
    ///
    /// - Parameter task: The TaskModel to toggle completion status for
    /// - SeeAlso: https://docs.ditto.live/sdk/latest/crud/update#updating
    ///
    /// - Throws: A DittoError if the update operation fails
    func toggleComplete(task: TaskModel) async {
        let done = !task.done
        
        // https://docs.ditto.live/dql/update#basic-update
        // https://docs.ditto.live/sdk/latest/crud/update#updating
        let query = """
                    UPDATE tasks
                    SET done = :done 
                    WHERE _id == :_id
                    """
        
        do {
            if let dittoInstance = ditto {
                
                // https://docs.ditto.live/sdk/latest/crud/update#updating
                try await dittoInstance.store.execute(
                    query: query,
                    arguments: ["done": done, "_id": task._id]
                )
            }
        } catch {
            appManager.setError(error)
        }
    }
}

// MARK: Deleting TaskModel
extension DittoManager {
    
    /// Delete a TaskModel by setting its deleted flag to true.
    ///
    /// This method implements the 'Soft-Delete' pattern, which:
    /// - Marks the TaskModel as deleted instead of evicting it
    /// - Removes it from active queries and views
    /// - Maintains the data for historical purposes
    ///
    /// - Parameter task: The TaskModel to soft-delet
    /// - SeeAlso: https://docs.ditto.live/sdk/latest/crud/delete#soft-delete-pattern
    ///
    /// - Throws: A DittoError if the archive operation fails
    func deleteTaskModel(_ task: TaskModel) async {
        
        // https://docs.ditto.live/sdk/latest/crud/delete#soft-delete-pattern
        let query = "UPDATE tasks SET deleted = true WHERE _id = :_id"
        do {
            if let dittoInstance = ditto {
                
                // https://docs.ditto.live/sdk/latest/crud/update#updating
                try await dittoInstance.store.execute(
                    query: query, arguments: ["_id": task._id])
            }
        } catch {
            appManager.setError(error)
        }
    }
}

