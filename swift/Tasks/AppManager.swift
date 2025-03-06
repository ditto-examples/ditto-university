import Foundation

class AppManager : ObservableObject {
    @Published var appConfig: DittoConfig
    @Published var error: Error? = nil
    
    init(configuration: DittoConfig) {
        appConfig = configuration
    }
    
    func setError(_ error: Error?) {
        DispatchQueue.main.sync {
            self.error = error
        }
    }
}
