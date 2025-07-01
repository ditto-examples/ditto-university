import SwiftUI
import Combine
import DittoPresenceViewer
import DittoSwift

// MARK: ContentView
struct ContentView: View {
    private static let SYNC_ENABLED_KEY = "syncEnabled"
    
    //TODO: update to DittoManager
    @EnvironmentObject private var dataManager:  DittoManager
    @State private var viewModel: ViewModel = ViewModel()
    @State private var syncEnabled: Bool = Self.loadSyncEnabledState()
    
    var body: some View {
        NavigationView {
            List {
                Section(
                    header: VStack {
                        Text("App ID: \(dataManager.appManager.appConfig.appID)")
                        Text("Token: \(dataManager.appManager.appConfig.authToken)")
                    }
                        .font(.caption)
                        .textCase(nil)
                        .padding(.bottom)
                ) {
                    ForEach(viewModel.tasks) { task in
                        TaskModelRowView(
                            task: task,
                            onToggle: { task in
                                viewModel.toggleComplete(task: task)
                            },
                            onClickEdit: { task in
                                viewModel.onEdit(task: task)
                            }
                        )
                    }
                    .onDelete(perform: deleteTaskItems)
                }
            }
            .animation(.default, value: viewModel.tasks)
            .navigationTitle("Ditto Tasks")
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    HStack {
                        Toggle("Sync", isOn: $syncEnabled)
                            .toggleStyle(SwitchToggleStyle())
                            .onChange(of: syncEnabled) { newSyncEnabled in
                                Self.saveSyncEnabledState(newSyncEnabled)
                                do {
                                    try viewModel.setSyncEnabled(newSyncEnabled)
                                } catch {
                                    syncEnabled = false
                                }
                            }
                    }
                }
                ToolbarItem(placement: .topBarLeading) {
                    Button {
                        viewModel.isPresentingPresenceView.toggle()
                    } label: {
                        Image(systemName: "info.circle.fill")
                    }
                }
                ToolbarItem(placement: .bottomBar) {
                    HStack {
                        Spacer()
                        Button(action: {
                            viewModel.onNewTask()
                        }) {
                            HStack {
                                Image(systemName: "plus")
                                Text("New Task")
                            }
                        }
                        .buttonStyle(.borderedProminent)
                        .padding(.bottom)
                    }
                }
            }
            .sheet(
                isPresented: $dataManager.isPresentingEditScreen,
                content: {
                        EditTaskModelView(taskModel: dataManager.selectedTaskModel)
                })
            .sheet(isPresented: $viewModel.isPresentingPresenceView) {
                if let ditto = dataManager.ditto {
                    PresenceSheetView(ditto: ditto) {
                        viewModel.isPresentingPresenceView = false
                    }
                }
            }
        }
        .task(id: ObjectIdentifier(dataManager)) {
            await viewModel.initialize(dataManager: dataManager)
            do {
                try viewModel.setSyncEnabled(syncEnabled)
            } catch {
                syncEnabled = false
            }
            
        }
    }
    
    private func PresenceSheetView(ditto: Ditto, onDismiss: @escaping () -> Void) -> some View {
        VStack {
            HStack {
                Spacer()
                Text("Presence Viewer")
                    .font(.title3)
                    .padding()
                Spacer()
                Button("Done") {
                    onDismiss()
                }
                .padding()
            }
            PresenceView(ditto: ditto)
        }
    }
    
    private func deleteTaskItems(at offsets: IndexSet) {
        let deletedTasks = offsets.map { viewModel.tasks[$0] }
        for task in deletedTasks {
            Task { @MainActor in
                await viewModel.deleteTask(task)
            }
        }
    }
    
    private static func loadSyncEnabledState() -> Bool {
        if UserDefaults.standard.object(forKey: SYNC_ENABLED_KEY) == nil {
            return true
        } else {
            return UserDefaults.standard.bool(forKey: SYNC_ENABLED_KEY)
        }
    }
    
    private static func saveSyncEnabledState(_ state: Bool) {
        UserDefaults.standard.set(state, forKey: SYNC_ENABLED_KEY)
        UserDefaults.standard.synchronize()
    }
}

#Preview {
    ContentView()
}

// MARK: ContentView ViewModel
extension ContentView {
    @Observable
    @MainActor
    class ViewModel {
        @ObservationIgnored private var cancellables = Set<AnyCancellable>()
        //TODO: update to DittoManager
        private var dataManager:  DittoManager?
        var tasks: [TaskModel] = []
        var isPresentingPresenceView = false
        
        deinit {
            cancellables.removeAll()
        }
        
        //TODO: update to DittoManager
        func initialize(dataManager: DittoManager) async {
            self.dataManager = dataManager
            
            dataManager.$tasks
                .receive(on: RunLoop.main)
                .sink { [weak self] updatedTasks in
                    self?.tasks = updatedTasks
                }
                .store(in: &cancellables)
        }
        
        func setSyncEnabled(_ newValue: Bool) throws {
            dataManager?.setSyncEnabled(newValue)
        }
        
        func toggleComplete(task: TaskModel) {
            Task { @MainActor in
                await dataManager?.toggleComplete(task: task)
            }
        }
        
        func saveEditedTask(_ task: TaskModel) async {
            await dataManager?.updateTaskModel(task)
        }
        
        func saveNewTask(_ task: TaskModel) async {
            await dataManager?.insertTaskModel(task)
        }
        
        func deleteTask(_ task: TaskModel) async {
            await dataManager?.deleteTaskModel(task)
        }
        
        func onEdit(task: TaskModel) {
            dataManager?.selectedTaskModel = task
            dataManager?.isPresentingEditScreen = true
        }
        
        func onNewTask() {
            dataManager?.selectedTaskModel = nil
            dataManager?.isPresentingEditScreen = true
        }
    }
}
