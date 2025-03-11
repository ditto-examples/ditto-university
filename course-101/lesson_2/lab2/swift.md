# Lab 2: Update Initialization of the Ditto instance with Swift

Open the [`DittoManager.swift`](../../swift/Tasks/Data/DittoManager.swift#L50) file and find the [initialize()](https://github.com/ditto-examples/ditto-university/blob/main/course-101/swift/Tasks/Data/DittoManager.swift#L50) function.  This is the place to setup Ditto Identity.

```swift
        //
        //TODO: setup Ditto Identity
        //
        //UPDATE CODE HERE
```
You can find this quickly by using the quick selection bar in Xcode.

![Quick Selection Bar](./assets/select-identity-swift.gif)

Let's update the `TODO` comment to set up Ditto Identity. The Ditto Identity information - your app ID, online playground token, websocket URL, and auth URL - are stored in `dittoConfig.plist` and automatically loaded into the `DittoManager` through the `appManager`.

Since the credentials are already available through the `appManager`, we can use them to create our Ditto Identity. Replace the `TODO` comment with this code:

```swift
ditto = Ditto(
	identity: .onlinePlayground(
		appID: appManager.appConfig.appId,
		token: appManager.appConfig.authToken,
		enableDittoCloudSync: true
	)
)
```

As you can see we are using the identity type of `onlinePlayground`.  This tells the Ditto SDK to connect to the Ditto Cloud and use the app ID and online playground token for authentication.  The enableDittoCloudSync flag tells the SDK to use the Ditto Cloud for sync. 

[Return to the lesson](../README.md) to continue.