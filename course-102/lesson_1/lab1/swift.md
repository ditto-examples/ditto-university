# Lab 1: Remove Websocket in TransportConfig to Disable Sync to Ditto Cloud  - Swift UI iOS Application

## Open the Project in Xcode

Open the project in Xcode by opening the `Tasks.xcodeproj` file in the [course-102/swift](https://github.com/ditto-examples/ditto-university/tree/main/course-102/swift/) directory.

## Comment out the `TransportConfig` 

To test our application syncing only to small peers (Ditto SDK), we will update the `TransportConfig` and remove setting the `WebsocketUrl` section.  This will disable sync to the Ditto Server/Cloud.

Find the [`initialize`](../../swift/Tasks/Data/DittoManager.swift#L62) function in the [`DittoManager.swift`](https://github.com/ditto-examples/ditto-university/blob/main/course-102/swift/Tasks/Data/DittoManager.swift#L62) file. This is the function that creates the Ditto instance.   

Now locate the following code:

```swift
// Set the Ditto Websocket URL
ditto.updateTransportConfig { config in
  config.connect.webSocketURLs.insert(
    appManager.appConfig.websocketURL
  )
}
```

This is the configuration that allows the Ditto SDK to sync to the Ditto Server/Cloud.  We can comment out this code to disable sync to the Ditto Server/Cloud. Comment out the code by surrounding it with `/*` and `*/` like this:

```swift
/*
ditto.updateTransportConfig { config in
  config.connect.webSocketURLs.insert(
    appManager.appConfig.websocketURL
  )
}
*/
```

## Update the `dittoConfig.plist` file

Like in the Course 101 Lesson 2 Lab 1, we need to update the `dittoConfig.plist` file with the information from the Ditto Portal:

1. Log into the Ditto portal at <https://portal.ditto.live/>.  Make note of the App ID, Auth URL, Websocket URL, and Online Playground Authentication Token.  

2. Open the `dittoConfig.plist` file:
	- Replace `put App ID here` in the appID key with your AppId listed on the portal
	- Replace `put Online Playground Authentication Token here` in the authToken key with the Online Playground Authentication Token from the Ditto portal.
	- Replace `put Auth URL here` in the authURL keywith the Auth URL from the Ditto portal.
	- Replace `put Websocket URL here` in the websocketURL key with the Websocket URL from the Ditto portal.

### Test the Funtionality in the SwiftUI App 

With this code updated, you can run the app from XCode with the updated code. When you add, delete, or change tasks, you can log into the Ditto Portal and validate that the changes are not replicated to the Ditto Portal. 

Congratulations - virtual high five 🙏!

We are making great progress, you just learned how to disable sync to the Ditto Server/Cloud via the `TransportConfig`.  We now need to test the ability to sync between multiple devices using the Ditto Mesh.  

[Return to the lesson](../README.md) to continue.