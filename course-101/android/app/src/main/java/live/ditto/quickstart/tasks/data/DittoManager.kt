package live.ditto.quickstart.tasks.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import live.ditto.Ditto
import live.ditto.DittoError
import live.ditto.DittoIdentity
import live.ditto.DittoLogLevel
import live.ditto.DittoLogger
import live.ditto.DittoStoreObserver
import live.ditto.DittoSyncSubscription
import live.ditto.android.DefaultAndroidDittoDependencies
import live.ditto.quickstart.tasks.models.DittoConfig
import live.ditto.quickstart.tasks.models.TaskModel
import live.ditto.quickstart.tasks.services.ErrorService

interface DataManager {

    val dittoConfig: DittoConfig
    val syncEnabled: LiveData<Boolean>


    /**
     * Populates the Ditto tasks collection with initial seed data if it's empty.
     * 
     * This method:
     * - Creates a set of predefined tasks with unique IDs and titles
     * 
     * The initial tasks include common todo items like:
     * - Buy groceries
     * - Clean the kitchen
     * - Schedule dentist appointment
     * - Pay bills
     * 
     * @throws Exception if the insert operations fail
     * 
     */
    suspend fun populateTaskCollection()

    /**
     * Enables or disables synchronization
     * 
     * This method:
     * - Persists the sync preference in DataStore
     * - Updates the UI state through LiveData
     * 
     * Implementation details:
     * - Retrieves default sync state from preferences if not specified
     * - Updates both DataStore and LiveData to maintain state
     * 
     * @param enabled Boolean value to set sync state to. If null, uses stored preference
     * 
     */
    suspend fun setSyncEnabled(enabled: Boolean?)

    /**
     * Creates a Flow that observes and emits changes to the tasks collection 
     *
     * This method:
     * - Sets up a live query observer for the tasks collection
     * - Emits a new list of TaskModel objects whenever the data changes
     *
     * @return Flow<List<TaskModel>> A flow that emits updated lists of Tasks
     * whenever changes occur in the collection
     */
    fun getTaskModels(): Flow<List<TaskModel>>

    suspend fun getTaskModel(id: String): TaskModel? = null

    /**
     * Adds a new TaskModel 
     *
     * This method:
     * - Creates a new document in the tasks collection
     * - Assigns the provided ID and properties
     * - Triggers a sync with other devices
     *
     * The taskModel object should have:
     * - A unique ID
     * - All required fields populated
     *
     * @param taskModel The new TaskModel object to be added
     * @throws Exception if the insert operation fails
     */
    suspend fun insertTaskModel(taskModel: TaskModel)

    /**
     * Updates an existing TaskModel 
     *
     * This method:
     * - Updates all mutable fields of the TaskModel
     * - Maintains the taskModel's ID and references
     * - Triggers a sync with other devices
     *
     * @param taskModel The updated TaskModel object containing all changes
     * @throws Exception if the update operation fails
     */
    suspend fun updateTaskModel(taskModel: TaskModel)

    /**
     * Updates an existing TaskModel, setting the done field to true.
     *
     * This method:
     * - Updates the done field to true
     * - Maintains the taskModel's ID and references
     *
     * The id should be a valid _id of a TaskModel object
     *
     * @param id  The _id of the TaskModel object to update
     * @throws Exception if the insert operation fails
     */
    suspend fun toggleComplete(id: String)

    /**
     * Archives a TaskModel by setting its deleted flag to true.
     *
     * This method:
     * - Marks the taskModel as deleted instead of deleting it
     * - Removes it from active queries and views
     * - Maintains the data for historical purposes
     *
     * Archived TaskModel:
     * - Are excluded from the main TaskModel list
     *
     * @param id  The _id of the TaskModel object to archive
     * @throws Exception if the archive operation fails
     */
    suspend fun deleteTaskModel(id: String)
}

class DittoManagerImp(
    override val dittoConfig: DittoConfig,
    context: Context,
    private val errorService: ErrorService
) : DataManager {

    companion object {
        private const val TAG = "DittoManagerImp"
    }

    // The value of the Sync switch is stored in persistent settings
    private val Context.preferencesDataStore by preferencesDataStore("tasks_list_settings")
    private val _syncEnabledKey = booleanPreferencesKey("sync_enabled")
    private lateinit var preferencesDataStore: androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences>

    private val _syncEnabled = MutableLiveData(true)
    override val syncEnabled: LiveData<Boolean> = _syncEnabled

    private lateinit var ditto: Ditto
    private var subscription: DittoSyncSubscription? = null
    private var storeObserver: DittoStoreObserver? = null

    init {
        try {
            preferencesDataStore = context.preferencesDataStore

            DittoLogger.minimumLogLevel = DittoLogLevel.DEBUG
            val androidDependencies = DefaultAndroidDittoDependencies(context)

            //
            // CustomUrl is used because Connector is in Private Preview
            // and uses a different cluster than normal production
            //
            val customAuthUrl = "https://${dittoConfig.endpointUrl}"
            val webSocketUrl = "wss://${dittoConfig.endpointUrl}"

            //
            // TODO remove when Connector is out of Private Preview
            // https://docs.ditto.live/sdk/latest/install-guides/kotlin#integrating-and-initializing
            val identity = DittoIdentity.OnlinePlayground(
                androidDependencies,
                dittoConfig.appId,
                dittoConfig.authToken,
                false,
                customAuthUrl
            )
            this.ditto = Ditto(androidDependencies, identity)

            // Set the Ditto Websocket URL
            // Enable all P2P transports
            ditto.updateTransportConfig { config ->
                config.connect.websocketUrls.add(webSocketUrl)
                config.enableAllPeerToPeer()
            }

            // disable sync with v3 peers, required for syncing with the Ditto Cloud (Big Peer)
            this.ditto.disableSyncWithV3()

        } catch (e: DittoError) {
            errorService.showError("Failed to initialize Ditto: ${e.message}")
        }
    }

    /**
     * Populates the Ditto tasks collection with initial seed data if it's empty.
     * 
     * This method:
     * - Creates a set of predefined tasks with unique IDs and titles
     * - Inserts each task into the Ditto store using DQL (Ditto Query Language)
     * - Uses the INITIAL keyword to only insert if documents don't already exist
     * 
     * The initial tasks include common todo items like:
     * - Buy groceries
     * - Clean the kitchen
     * - Schedule dentist appointment
     * - Pay bills
     * 
     * Implementation details:
     * - Uses DQL INSERT statement with INITIAL keyword to prevent duplicates
     * - Each task document contains _id, title, done, and deleted fields
     * - Handles errors gracefully and logs them for debugging
     * 
     * @throws Exception if the insert operations fail
     * 
     * @see https://docs.ditto.live/sdk/latest/crud/write#inserting-documents
     */
    override suspend fun populateTaskCollection() {
        return withContext(Dispatchers.IO) {
            val tasks = listOf(
                TaskModel("50191411-4C46-4940-8B72-5F8017A04FA7", "Buy groceries"),
                TaskModel("6DA283DA-8CFE-4526-A6FA-D385089364E5", "Clean the kitchen"),
                TaskModel("5303DDF8-0E72-4FEB-9E82-4B007E5797F0", "Schedule dentist appointment"),
                TaskModel("38411F1B-6B49-4346-90C3-0B16CE97E174", "Pay bills")
            )

            tasks.forEach { task ->
                try {
                    ditto.store.execute(
                        "INSERT INTO tasks INITIAL DOCUMENTS (:task)",
                        mapOf(
                            "task" to mapOf(
                                "_id" to task._id,
                                "title" to task.title,
                                "done" to task.done,
                                "deleted" to task.deleted,
                            )
                        )
                    )
                } catch (e: Exception) {
                    errorService.showError("Unable to insert initial documents: ${e.message}")
                    Log.e(TAG, "Unable to insert initial document", e)
                }
            }
        }
    }

    /**
     * Enables or disables synchronization with the Ditto cloud (BigPeer).
     * 
     * This method:
     * - Manages the sync state of the Ditto instance
     * - Persists the sync preference in DataStore
     * - Updates the UI state through LiveData
     * 
     * Implementation details:
     * - Retrieves default sync state from preferences if not specified
     * - Starts sync if enabled and not already active
     * - Stops sync if disabled and currently active
     * - Updates both DataStore and LiveData to maintain state
     * 
     * @param enabled Boolean value to set sync state to. If null, uses stored preference
     * 
     * @see startSync()
     * @see stopSync()
     */
    override suspend fun setSyncEnabled(enabled: Boolean?) {
        //get default value from preferences if not set
        var isEnabled = enabled
        if (isEnabled == null) {
            isEnabled =
                preferencesDataStore.data.map { prefs -> prefs[_syncEnabledKey] ?: true }.first()
        }
        if (isEnabled && !ditto.isSyncActive) {
            startSync()
        } else {
            stopSync()
        }

        //update preferences
        preferencesDataStore.edit { settings ->
            settings[_syncEnabledKey] = isEnabled
        }
        _syncEnabled.value = isEnabled
    }

    /**
     * Starts the Ditto sync process and sets up the initial subscription.
     * 
     * This method:
     * - Initiates the sync process 
     * - Creates a subscription to the tasks collection
     * - Ensures real-time synchronization of task data across devices
     * 
     * The subscription:
     * - Queries all documents in the tasks collection
     * - Maintains the subscription until sync is stopped or the app is terminated
     * - Automatically syncs changes between the local store and the Ditto cloud
     * 
     * Implementation details:
     * - Uses Ditto's sync API to establish connection with the BigPeer
     * - Sets up a subscription using DQL to monitor the tasks collection
     * - Handles errors gracefully and logs them for debugging
     * 
     * @throws Exception if sync initialization or subscription setup fails
     * 
     * @see Ditto.startSync()
     * @see Ditto.sync.registerSubscription()
     */
    private suspend fun startSync() {
        return withContext(Dispatchers.IO) {
            try {
                ditto.startSync()

                val subscriptionQuery = "SELECT * from tasks"
                subscription = ditto.sync.registerSubscription(subscriptionQuery)
            } catch (e: Exception) {
                errorService.showError("Failed to start ditto sync: ${e.message}")
                Log.e(TAG, "Failed to start ditto sync", e)
            }
        }
    }

    /**
     * Stops the Ditto sync process and cleans up associated resources.
     * 
     * This method:
     * - Cancels any active subscriptions to the tasks collection
     * - Stops the sync process with the Ditto cloud (BigPeer)
     * - Cleans up resources to prevent memory leaks
     * 
     * Implementation details:
     * - Closes the subscription and sets it to null
     * - Calls Ditto's stopSync() to terminate the sync process
     * - Should be called when sync is no longer needed or app is terminating
     * 
     * @see Ditto.stopSync()
     * @see DittoSyncSubscription.close()
     */
    private suspend fun stopSync() {
        return withContext(Dispatchers.IO) {
            subscription?.close()
            subscription = null
            ditto.stopSync()
        }
    }

    /**
     * Creates a Flow that observes and emits changes to the tasks collection in Ditto.
     * https://docs.ditto.live/sdk/latest/crud/read#using-args-to-query-dynamic-values
     *
     * This method:
     * - Sets up a live query observer for the tasks collection
     * - Emits a new list of TaskModel objects whenever the data changes
     * - Automatically cleans up the observer when the flow is cancelled
     *
     * The query:
     * - Returns all non-archived TaskModel (done = false)
     * - Transforms raw Ditto data into TaskModel objects
     *
     * @return Flow<List<TaskModel>> A flow that emits updated lists of Tasks
     * whenever changes occur in the collection
     *
     */
    override fun getTaskModels(): Flow<List<TaskModel>> = callbackFlow {
        try {
            val query = "SELECT * FROM tasks WHERE NOT deleted"
            storeObserver = ditto.store.registerObserver(query) { results ->
                val items = results.items.map { item ->
                    TaskModel.fromMap(item.value)
                }
                trySend(items)
            }
            // Clean up the observer when the flow is cancelled
            awaitClose {
                storeObserver?.close()
                storeObserver = null
            }
        } catch (e: Exception) {
            errorService.showError("Failed to setup observer for getting taskModels: ${e.message}")
            Log.e(TAG, "Failed to setup observer for getting taskModels", e)
        }
    }

    override suspend fun getTaskModel(id: String): TaskModel? {
        withContext(Dispatchers.IO) {
            try {
                val item = ditto.store.execute(
                    "SELECT * FROM tasks WHERE _id = :_id AND NOT deleted",
                    mapOf("_id" to id)
                ).items.first()
                val taskModel = TaskModel.fromJson(item.jsonString())
                return@withContext taskModel
            } catch (e: Exception){
                errorService.showError("Failed to get taskModel: ${e.message}")
                Log.e(TAG, "Failed to get taskModel:", e)
            }
        }
        return null
    }

    /**
     * Creates a new TaskModel document in the Ditto store.
     * https://docs.ditto.live/sdk/latest/crud/create#creating-documents
     *
     * Implementation details:
     * - Inserts a complete document with all required fields using DQL
     *
     * @param taskModel The new TaskModel object to insert
     */
    override suspend fun insertTaskModel(taskModel: TaskModel) {
        return withContext(Dispatchers.IO) {
            try {
                val query = "INSERT INTO tasks DOCUMENTS (:newTask)"
                ditto.store.execute(
                    query,
                    mapOf(
                        "newTask" to mapOf(
                            "_id" to taskModel._id,
                            "title" to taskModel.title,
                            "done" to taskModel.done,
                            "deleted" to taskModel.deleted
                        )
                    )
                )
            } catch (e: Exception) {
                errorService.showError("Failed to add taskModel: ${e.message}")
                Log.e(TAG, "Failed to add taskModel:", e)
            }
        }
    }

    /**
     * Updates existing TaskModel's properties in the Ditto store.
     * https://docs.ditto.live/sdk/latest/crud/update#updating
     * Implementation details:
     * - Uses DQL to update the document
     *
     * @param taskModel The TaskModel object containing updated values
     */
    override suspend fun updateTaskModel(taskModel: TaskModel) {
        return withContext(Dispatchers.IO) {
            try {
                val query = """
                UPDATE tasks
                SET title = :title,
                    done = :done,
                    deleted = :deleted,
                WHERE _id = :_id 
                """
                ditto.store.execute(
                    query,
                    mapOf(
                        "title" to taskModel.title,
                        "done" to taskModel.done,
                        "deleted" to taskModel.deleted,
                        "_id" to taskModel._id
                    )
                )
            } catch (e: Exception) {
                errorService.showError("Failed to update taskModel: ${e.message}")
                Log.e(TAG, "Failed to update taskModel:", e)
            }
        }
    }

    /**
     * Updates an existing TaskModel in the Ditto store, setting the done
     * field to true.
     *
     * This method:
     * - Updates the done field to true
     * - Maintains the taskModel's ID and references
     * - Triggers a sync with other devices
     *
     * The taskModel object should have:
     * - A unique ID
     * - All required fields populated
     *
     * @param id The _id TaskModel object to update
     * @throws Exception if the insert operation fails
     */
    override suspend fun toggleComplete(id: String) {
        return withContext(Dispatchers.IO) {
            try {
                val doc = ditto.store.execute(
                    "SELECT * FROM tasks WHERE _id = :_id AND NOT deleted",
                    mapOf("_id" to id)
                ).items.first()

                val done = doc.value["done"] as Boolean

                val query = """
                UPDATE tasks
                SET done = :done 
                WHERE _id == :_id
                """

                ditto.store.execute(
                    query,
                    mapOf(
                        "done" to !done,
                        "_id" to id
                    )
                )
            } catch (e: Exception) {
                errorService.showError("Failed to update taskModel: ${e.message}")
                Log.e(TAG, "Failed to update taskModel:", e)
            }
        }
    }

    /**
     * Marks a TaskModel as archived in the Ditto store.
     * This uses the 'Soft-Delete' pattern:  https://docs.ditto.live/sdk/latest/crud/delete#soft-delete-pattern
     *
     * Implementation details:
     * - Uses a simple DQL UPDATE query to set done flag
     *
     * @param id The _id of the TaskModel to archive
     */
    override suspend fun deleteTaskModel(id: String) {
        return withContext(Dispatchers.IO) {
            try {
                val query = """
                UPDATE tasks
                SET done = true
                WHERE _id = :_id 
                """
                ditto.store.execute(
                    query,
                    mapOf(
                        "_id" to id,
                    )
                )
            } catch (e: Exception) {
                errorService.showError("Failed to archive taskModel: ${e.message}")
                Log.e(TAG, "Failed to archive taskModel:", e)
            }
        }
    }
}