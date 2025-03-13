# Lesson 2 🚀 Setting Up Your Development Environment

## Ditto SDK

The Ditto SDK helps develoeprs build powerful, offline-capable applications with real-time data sync. Designed for flexibility and performance, Ditto's SDK enables seamless peer-to-peer communication, efficient data synchronization, and robust offline support—no complex infrastructure required.

Whether you’re building for mobile, desktop, or embedded systems, the Ditto SDK gives you the tools to create reliable and connected applications that work anywhere.

The Ditto SDK provides a unified API for:

- Managing offline data persistence
- Synchronizing data with other devices (called `Small Peers`) running the same `Ditto app`
- Reading and writing data to the Big Peer
- Handling real-time updates

This architecture enables seamless data synchronization between:
- Your device and other users' devices (Small Peers)
- Your device and the cloud (Big Peer)
- The cloud and all connected devices

## Package Dependencies

The Ditto SDK is already included in this project. When creating your own Ditto-enabled applications, adding the SDK as a package dependency is a required first step.

For detailed instructions on adding the Ditto SDK to your own projects, refer to the following documentation:
- [Swift SDK installation guide](https://docs.ditto.live/sdk/latest/install-guides/swift#installing-package-dependencies)
- [Kotlin SDK installation guide](https://docs.ditto.live/sdk/latest/install-guides/kotlin#installing-package-dependencies)
- [Flutter SDK installation guide](https://docs.ditto.live/sdk/latest/install-guides/flutter#installing-package-dependencies)

## Lab 1: Clone the repository and run the App with Mock Data

To work with all future labs, you will are required to clone the Ditto University repository to your local machine using the following command:

```bash
git clone https://github.com/ditto-examples/ditto-university
```

### Building and Running the Application

To see the app in action running with mock data - follow the instructions based on what platform you are using:

- [Building and Running the Swift UI iOS Application](lab1/swift.md)
- [Building and Running the Android JetPack Compose with Kotlin Application](lab1/android.md)
- [Building and Running the Flutter Application](lab1/flutter.md)

## Ditto Identity

The Ditto SDK provides a Ditto Identity to the SDK.  This identity provides the SDK with various identity configurations that you can use when initializing a Ditto instance. Several identity types are supported including:
- `offlinePlayground`: Develop peer-to-peer apps with no cloud connection. This mode offers no security and must only be used for development.
- `onlineWithAuthentication`: Run Ditto in secure production mode, logging on to Ditto Cloud or on on-premises authentication server. User permissions are centrally managed.
- `onlinePlayground`: Test a Ditto Cloud app with weak shared token authentication (“Playground mode”). This mode is not secure and must only be used for development.

## Lab 2: Update Initialization of the Ditto instance

In Lab 2, we will update our app to use the `onlinePlayground` identity type.  To Update the initialization of the Ditto instance - follow the instructions based on what platform you are using:

- [Update Initialization of the Ditto instance - Swift UI iOS Application](lab2/swift.md)
- [Update Initialization of the Ditto instance - Android JetPack Compose with Kotlin Application](lab2/android.md)
- [Update Initialization of the Ditto instance - Flutter Application](lab2/flutter.md)

If you are stuck, you can refer to the `.finished` files in the [lab2](./lab2) directory with the corresponding platform name. 

## ❓ Knowledge Check 

1. Which of the following is `NOT` a feature of the Ditto SDK? 
   - a) Offline data persistence
   - b) Synchronizing data with other devices (Small Peers)
   - c) Authentication users with a username and password
   - d) Handling real-time updates

2. What is the purpose of the Ditto Identity? 
   - a) provides a unique identifier for a Ditto instance
   - b) provides various identity configurations that you can use when initializing a Ditto instance. 
   - c) provides Ditto with information about the user that is logged into the device. 
   - d) provides Ditto with information about the device that the app is running on. 

The answer can be found in the [answer file](.answer).

## Summary

🎉 Congratulations 🙌! In this lesson, you have learned:
1. The purpose of the Ditto SDK 
2. What the Ditto Identity is and how it is used to initialize a Ditto instance


## 📚 References

- [Ditto SDK](https://docs.ditto.live/sdk/overview)
- [SDK Install Guides](https://docs.ditto.live/sdk/latest/install-guides/install-guides)

## Next Steps

Now that you have created setup the Ditto SDK and initialized the Ditto instance, you're ready to start loading initial data into the Ditto database using the Ditto SDK. Let's go!

[Continue to Lesson 3 - Try Out The Sample Data →](../lesson_3/README.md)