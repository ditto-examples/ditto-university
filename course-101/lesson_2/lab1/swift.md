# Lab 1: Building and Running the Swift UI iOS Application

To see the app in action running with mock data, let's build and run the app by following these steps:

1. Log into the Ditto portal at <https://portal.ditto.live/>.  Make note of the App ID, Auth URL, Websocket URL, and Online Playground Authentication Token.  You should have already done this in the previous lab in [Lesson 1](../../lesson_1/README.md).

2. Launch Xcode and open the `/course-101/swift/Tasks.xcodeproj` project.

3. Open the `dittoConfig.plist` file:
	- Replace `put App ID here` in the appID key with your AppId listed on the portal
	- Replace `put Online Playground Authentication Token here` in the authToken key with the Online Playground Authentication Token from the Ditto portal.
	- Replace `put Auth URL here` in the authURL keywith the Auth URL from the Ditto portal.
	- Replace `put Websocket URL here` in the websocketURL key with the Websocket URL from the Ditto portal.

4. In order to run on a physical device, you will need to update the `Team` that the project is signed with.  Navigate to the project **Signing & Capabilities** tab and modify the **Team** and **Bundle Identifier** settings to your Apple developer account credentials to provision building to your device.

5. In Xcode, select a connected iOS device or iOS Simulator as the destination.

6. Choose the **Product > Run** menu item.

The app will build and run on the selected device or emulator.  You can add, edit, and delete tasks in the app.  But if you close the app and open it up, all your changes will be lost due to the mock data.  

Let's start fixing that by replacing the mock data with a real Ditto database.  [Return to the lesson](../README.md) to continue.