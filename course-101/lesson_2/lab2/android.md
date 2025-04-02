# Lab 2: Update Initialization of the Ditto instance with Kotlin and Jetpack Compose for Android 

# Lab 2: Update Initialization of the Ditto instance with Kotlin 

Open the [`DittoManager.kt`](../../android/app/src/main/java/data/DittoManager.kt#L42) file and find the [init](https://github.com/ditto-examples/ditto-university/blob/main/course-101/android/app/src/main/java/data/DittoManager.kt#L42) function.  This is the place to setup Ditto Identity.

```kotlin
  //
  //TODO: setup Ditto Identity
  //
  //UPDATE CODE HERE
```
You can find this quickly by using the Structure toolbar in Android Studio.  To open the Structure toolbar, you can use the shortcut `Ctrl+7` (Windows/Linux) or `Cmd+7` (macOS).

![Structure toolbar](./assets/select-identity-kotlin.gif)

Let's update the `TODO` comment to set up Ditto Identity. The Ditto Identity information - your app ID, online playground token, websocket URL, and auth URL - are stored in `dittoConfig.xml` and automatically loaded into the `DittoManager` through the `DittoConfig` object passed into the DittoManager when it's initialized.

On the Android platform, Ditto also requires an instance of `AndroidDittoDependencies` to be passed into the Ditto constructor.  Our instance of `androidDittoDependencies` is automatically created and passed into the DittoManager when it's initialized from the TasksApplication class.  

Since the credentials are already available through the `dittoConfig`, we can use them to create our Ditto Identity. Replace the `TODO` comment with this code:

```kotlin
val identity = DittoIdentity.OnlinePlayground(
  androidDittoDependencies,
  dittoConfig.appId,
  dittoConfig.authToken,
  false,
  dittoConfig.authUrl
)
this.ditto = Ditto(androidDittoDependencies, identity)

 // Set the Ditto Websocket URL
this.ditto?.updateTransportConfig { config ->
  config.connect.websocketUrls.add(dittoConfig.websocketUrl)
  // TODO: enable all P2P transports
}
```

As you can see we are using the identity type of `OnlinePlayground`.  This tells the Ditto SDK to connect to the Ditto Cloud and use the app ID, online playground token, and auth URL for authentication. The enableDittoCloudSync flag is required to be false in order for it to use the customAuthURL provided by the dittoConfig.

Note we also set the Ditto Websocket URL and have a TODO comment about P2P that we will handle in a future lab. 

[Return to the lesson](../README.md) to continue.