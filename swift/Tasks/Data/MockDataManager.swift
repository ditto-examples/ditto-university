import Foundation

@MainActor class MockDataManager : ObservableObject, DataManager {
    @Published var tasks: [TaskModel] = []
    @Published var isPresentingEditScreen: Bool = false
    @Published var selectedTaskModel: TaskModel?
    
    var appManager: AppManager
    
    init(appManager: AppManager) {
        self.appManager = appManager
    }
    
    func initialize() async throws {
        //do fake initialization
        print ("doing lots of intialization work")
        
        do {
            try await populateTaskCollection()
        } catch let error {
            appManager.setError(error)
        }
    }
    
    func populateTaskCollection() async throws {
        
        let initialTasks: [TaskModel] = [
            TaskModel(
                _id: "50191411-4C46-4940-8B72-5F8017A04FA7",
                title: "Buy groceries"),
            TaskModel(
                _id: "6DA283DA-8CFE-4526-A6FA-D385089364E5",
                title: "Clean the kitchen"),
            TaskModel(
                _id: "5303DDF8-0E72-4FEB-9E82-4B007E5797F0",
                title: "Schedule dentist appointment"),
            TaskModel(
                _id: "38411F1B-6B49-4346-90C3-0B16CE97E174",
                title: "Pay bills"),
        ]
        
        for task in initialTasks {
            tasks.append(task)
        }
        
    }
    
    func setSyncEnabled(_ newValue: Bool) throws {
        //do fake set sync enabled
        print ("doing lots of work to check and enable or disable sync")
    }
    
    func insertTaskModel(_ task: TaskModel) async {
        tasks.append(task)
    }
    
    func updateTaskModel(_ task: TaskModel) async {
        removeTask(task)
        if (!task.deleted){
            tasks.append(task)
        }
    }
    
    func toggleComplete(task: TaskModel) async {
        var newTask = task
        newTask.done.toggle()
        await updateTaskModel(newTask)
    }
    
    func deleteTaskModel(_ task: TaskModel) async {
        removeTask(task)
    }
    
    private func removeTask(_ task: TaskModel) {
        tasks.remove(at: tasks.firstIndex(where: { $0._id == task._id })!)
    }
    
}
