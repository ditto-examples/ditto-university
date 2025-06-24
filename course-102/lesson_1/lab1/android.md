# Lab 1: Remove Websocket in TransportConfig to Disable Sync to Ditto Cloud   - Kotlin with Jetpack Compose

## Open the Project in Android Studio 

Open the project in Android Studio by opening the `android` folder in the [course-102](https://github.com/ditto-examples/ditto-university/tree/main/course-102/android/) directory.

## Comment out the `TransportConfig` 

To test our application syncing only to small peers (Ditto SDK), we will update the `TransportConfig` and remove setting the `WebsocketUrl` section.  This will disable sync to the Ditto Server/Cloud.


Find the [`init`](../../android/app/src/main/java/live/ditto/quickstart/data/DittoManager.kt#L53) function in the [`DittoManager.kt`](https://github.com/ditto-examples/ditto-university/blob/main/course-102/android/app/src/main/java/Data/DittoManager.kt#L53) file. This is the function that creates the Ditto instance.   

Now locate the following code:

```kotlin
// Set the Ditto Websocket URL
ditto?.updateTransportConfig { config ->
  config.connect.websocketUrls.add(dittoConfig.websocketUrl)
}
```

This is the configuration that allows the Ditto SDK to sync to the Ditto Server/Cloud.  We can comment out this code to disable sync to the Ditto Server/Cloud. Comment out the code by surrounding it with `/*` and `*/` like this:

```kotlin
/*
// Set the Ditto Websocket URL
ditto?.updateTransportConfig { config ->
  config.connect.websocketUrls.add(dittoConfig.websocketUrl)
}
*/
```

## Update the `dittoConfig.xml` file


Like in the Course 101 Lesson 2 Lab 1, we need to update the `dittoConfig.xml` file with the information from the Ditto Portal:

1. Log into the Ditto portal at <https://portal.ditto.live/>.  Make note of the App ID, Auth URL, Websocket URL, and Online Playground Authentication Token.  

2. Open the `dittoConfig.xml` file found in the `app/src/main/res/values/` folder:
	- Replace `put appID here` with your AppId listed on the portal
	- Replace `put Oline Playground Authentication Token here` with the Online Playground Authentication Token from the Ditto portal.
	- Replace `put AuthURL here` with the Auth URL from the Ditto portal.
	- Replace `put Websocket URL here` with the Websocket URL from the Ditto portal.

### Test the Funtionality in the Android Jetpack Compose App 

With this code updated, you can run the app from Andrid Studio with the updated code.  When you add, delete, or change tasks, you can log into the Ditto Portal and validate that the changes are not replicated to the Ditto Portal. 

Congratulations - virtual high five 🙏!

We are making great progress, you just learned how to disable sync to the Ditto Server/Cloud via the `TransportConfig`.  We now need to test the ability to sync between multiple devices using the Ditto Mesh.    

[Return to the lesson](../README.md) to continue.