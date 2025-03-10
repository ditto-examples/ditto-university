# 1.1 - Getting Started: Register Your Portal Account 🚀

## The Ditto Portal

The Ditto Portal is a web-based interface for managing your Ditto resources. It enables you to create and configure Ditto apps, manage data, and monitor usage.

### The Big Peer and Cloud Storage

The Big Peer is a trusted cloud deployment of Ditto's sync engine and associated services that enables advanced platform capabilities. Every Ditto Portal account includes a Big Peer instance that can optionally serve as a central hub for data storage and synchronization.

In our Project Management app, data is stored in several collections within the Big Peer instance. Each project and task is stored as a JSON document in its respective collection. The Ditto SDK provides a unified API for:
- Reading and writing data to the Big Peer
- Synchronizing with other devices (Small Peers) running the same app
- Managing offline data persistence
- Handling real-time updates

This architecture enables seamless data synchronization between:
- Your device and the cloud (Big Peer)
- Your device and other users' devices (Small Peers)
- The cloud and all connected devices

### Ditto App

A Ditto Portal App and App ID functions as a tenant in the traditional cloud sense. It provides a unique identifier for each distinct application environment, isolating data and configurations within that specific context.

### Lab 1 - Create a Ditto Portal Account and Ditto App

To get started, you'll need a Ditto Portal account to sync information with the Big Peer and a configured application to authenticate your app using the Ditto SDK.

1. Create a Ditto Portal account by following the [account creation guide](https://docs.ditto.live/cloud/portal/creating-a-ditto-account). If you already have an account on [portal.ditto.live](https://portal.ditto.live/), you can skip this step.

2. Create a new Ditto app by following the [creating a new app guide](https://docs.ditto.live/cloud/portal/creating-a-new-app).  If you already have an app registered, you can skip this step.

---
### Ditto Portal - Connect Tab

The Connect tab provides all the information needed to authenticate your application with Ditto's services. It contains several important sections:

#### Connect via SDK

The Connect via SDK section provides the authentication credentials for your application. You'll need the following information for future labs:
- App ID
- Auth URL
- Websocket URL
- Online Playground Authentication Token

#### Authentication and Authorization

Ditto's primary authentication mechanisms use the Big Peer as a central authentication server. All new devices and users must first connect to the Big Peer to authenticate before joining and syncing data with the mesh.

#### Online Playground Authentication Token

Online Playground mode is Ditto's authentication-light environment designed for development and testing, or use cases that don't require per-user permissions. It allows developers to quickly set up a Ditto-backed application with minimal configuration. In this mode, devices connect through Ditto's Big Peer using a shared App ID and Playground token, without requiring unique user authentication.

For our initial changes to the Project Management app, we'll use the Online Playground Authentication Token. This shared secret authenticates our app with the Big Peer and provides a convenient sandbox identity.

#### Connect via HTTP

Ditto offers an HTTP API for programmatic interaction with transactional data generated in the mesh network and stored within the Big Peer. The API follows remote procedure call (RPC) framework principles, allowing you to make requests as if they were local calls, despite occurring over a network.

The Connect via HTTP section provides the authentication information needed for using the Ditto HTTP REST API.

---
## ❓ Quiz 

1. What is the purpose of the Ditto Portal? 
   - a) A web-based interface for managing Ditto resources, configuring apps, and monitoring usage
   - b) A social media platform for developers to share Ditto experiences
   - c) A command-line tool for deploying Ditto applications
   - d) A cloud-based IDE for writing Ditto applications

2. What is an App ID used for?
   - a) A personal identification number (PIN) for developer authentication into the portal
   - b) A serial number for tracking a users session in the Ditto Portal 
   - c) A version number for the Ditto SDK
   - d) A unique identifier that isolates data and configurations for a specific application environment

3. What is an Online Playground Authentication Token used for?
   - a) A token used to track API usage and billing
   - b) A shared authentication token for development and testing without per-user permissions
   - c) A password for accessing the Ditto documentation
   - d) A license key for the Ditto SDK

The answers can be found in the [answer file](.answer).

---

## Summary

🎉 Congratulations 🙌! In this lesson, you have learned:
1. The purpose of the Ditto Portal and the Big Peer
2. How to create a Ditto Portal account
3. How to create a new Ditto app
4. How to use the Online Playground Authentication Token
5. The basics of the Ditto HTTP API

## Next Steps

Now that you have created your Ditto Portal account and configured your app, you're ready to start building a mobile app using the Ditto SDK. Let's go!

[Continue to 1.2 - Setting Up Your Development Environment →](../1.2/README.md)

