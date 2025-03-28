# Lab 1: Building and Running the Kotlin Jetpack Compose for Android Application

To see the app in action running with mock data, let's build and run the app by following these steps:

1. Log into the Ditto portal at <https://portal.ditto.live/>.  Make note of the App ID, Auth URL, Websocket URL, and Online Playground Authentication Token.  You should have already done this in the previous lab in [Lesson 1](../../lesson_1/README.md).

2. Launch Android Studio and open the `/course-101/android/` gradle project.

3. Open the `dittoConfig.xml` file found in the `app/src/main/res/values/` folder:
	- Replace `put appID here` with your AppId listed on the portal
	- Replace `put Oline Playground Authentication Token here` with the Online Playground Authentication Token from the Ditto portal.
	- Replace `put AuthURL here` with the Auth URL from the Ditto portal.
	- Replace `put Websocket URL here` with the Websocket URL from the Ditto portal.

4. In Android Studio, select a connected device or emulator as the destination.

6. Choose the **Run > Run 'app'** menu item, or click the Run icon on the main header bar.

The app will build and run on the selected device or emulator.  You can add, edit, and delete tasks in the app.  But if you close the app and open it up, all your changes will be lost due to the mock data.  

Let's start fixing that by replacing the mock data with a real Ditto database.  [Return to the lesson](../README.md) to continue.