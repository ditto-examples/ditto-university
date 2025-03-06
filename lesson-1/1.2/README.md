# 1.2 Launch The Basic Application

> [!NOTE] 
>This is a proof of concept for the Ditto iOS Introduction to Ditto Learning Module.  It is a simple to-do list app that uses Ditto to store and sync the task data.  This is not production ready code and is only meant to be used as a reference for the POC of Ditto University learning modules. 

## Prerequisites

You will need the following to complete this learning module:

- [Xcode](https://developer.apple.com/xcode/) 15 or greater
- Git installed on your machine 
- A Ditto Portal account with an app created (previous Step)
- Ditto SDK installed as a package dependency using Swift Package Manager (already done for you)


### Package Dependencies

The Ditto SDK is already included in this project as a Swift Package Manager dependency. When creating your own Ditto-enabled applications, adding the SDK as a package dependency is a required first step.

For detailed instructions on adding the Ditto SDK to your own projects, refer to the [Swift SDK installation guide](https://docs.ditto.live/sdk/latest/install-guides/swift#installing-package-dependencies).

### Clone the repository

Clone the repository to your local machine using the following command:

```bash
git clone https://github.com/ditto-examples/learning-swift-introduction
```


## Building and Running the iOS Application

To see the app in action running with mock data, let's build and run the app by following these steps:

1. Log into the Ditto portal at <https://portal.ditto.live/>.  Make note of the app ID and online playground token.
2. Launch Xcode and open the `/Tasks/Tasks.xcodeproj` project.
3. Open the `dittoConfig.plist` file and update your app ID and online playground token with the values from the Ditto portal.
4. Navigate to the project **Signing & Capabilities** tab and modify the **Team** and **Bundle Identifier** settings to your Apple developer account credentials to provision building to your device.
5. In Xcode, select a connected iOS device or iOS Simulator as the destination.
6. Choose the **Product > Run** menu item.

The app will build and run on the selected device or emulator.  You can add, edit, and delete tasks in the app.  But if you close the app and open it up, all your changes will be lost due to the mock data.  Let's start fixing that by replacing the mock data with a real Ditto database.

## Provide Authentication Credentials 

Open the `DittoManager.swift` file and find the `initialize()` method.  This is the place to setup Ditto Identity.

```swift
        //
        //TODO: setup Ditto Identity
        //
        //UPDATE CODE HERE
```
You can find this quickly by using the quick selection bar in Xcode.
![Quick Selection Bar](../assets/select-identity.gif)

Let's update the `TODO` comment to set up Ditto Identity. A Ditto Identity provides the SDK with your application credentials and connection details. These credentials - your app ID and online playground token - are stored in `dittoConfig.plist` and automatically loaded into the `DittoManager` through the `appManager`.

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

