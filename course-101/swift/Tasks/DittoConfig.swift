import Foundation

/// Store the ditto config details to use when instantiating the app
struct DittoConfig {
    var appID: String
    var authToken: String
    var authURL: String
    var websocketURL: String
}

/// Read the dittoConfig.plist file and store the appId, endpointUrl, and authToken to use elsewhere.
func loadAppConfig() -> DittoConfig {
    guard let path = Bundle.main.path(forResource: "dittoConfig", ofType: "plist") else {
        fatalError("Could not load dittoConfig.plist file!")
    }
    
    // Any errors here indicate that the dittoConfig.plist file has not been formatted properly.
    // Expected key/values:
    //      "endpointUrl": "your BigPeer Cloud URL Endpoint"
    //      "appId": "your BigPeer appId"
    //      "authToken": "your Online Playground Authentication Token"
    let data = NSData(contentsOfFile: path)! as Data
    let dittoConfigPropertyList = try! PropertyListSerialization.propertyList(from: data, format: nil) as! [String: Any]
    let authURL = dittoConfigPropertyList["authURL"]! as! String
    let websocketURL = dittoConfigPropertyList["websocketURL"]! as! String
    let appID = dittoConfigPropertyList["appID"]! as! String
    let authToken = dittoConfigPropertyList["authToken"]! as! String
    
    return DittoConfig(
        appID: appID,
        authToken: authToken,
        authURL: authURL,
        websocketURL: websocketURL
    )
}

