import SwiftUI

@main
struct TasksApp: App {
    //TODO: update to DittoManager
    @StateObject private var dataManager: MockDataManager = {
        
        //TODO: update to DittoManager
        let manager = MockDataManager(appManager: AppManager(configuration: loadAppConfig()))
        Task {
            do {
                try await manager.initialize()
            }
            catch let error {
                manager.appManager.setError(error)
            }
        }
        return manager
    }()
    
    var body: some Scene {
        WindowGroup {
            ContentView()
                .alert("Error", isPresented: Binding(
                    get: { dataManager.appManager.error != nil },
                    set: { if !$0 { dataManager.appManager.error = nil } }
                )) {
                    Button("OK", role: .cancel) {
                        dataManager.appManager.error = nil
                    }
                } message: {
                    Text(dataManager.appManager.error?.localizedDescription ?? "Unknown Error")
                }
                .environmentObject(dataManager)
        }
    }
}
