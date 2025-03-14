# Lesson 5 🚀 Syncing Data Across Devices using the Big Peer

## Syncing Data

Ditto’s synchronization model is designed for real-time, offline-first, and peer-to-peer (P2P) data propagation. Unlike traditional client-server architectures where clients must connect to a centralized database, Ditto enables direct communication between devices, ensuring that data stays available and up-to-date even when offline. Data synchronization works the same whether the device is synchronizing with other devices in the mesh, or with the Big Peer. Simply by subscribing to new data, Ditto ensures that data flows from elsewhere in the mesh to the subscribing device.  ￼

The Big Peer serves as a central coordination and data storage point, facilitating synchronization across disconnected meshes and integrating with centralized servers or cloud infrastructures.  ￼

In practice, when a device registers a subscription query, Ditto ensures that any data matching the query’s criteria is synchronized to that device, regardless of whether the data resides on another device within the mesh or on the Big Peer. This mechanism ensures that devices receive only the data they are interested in, optimizing bandwidth and resource usage while maintaining real-time data consistency across the network.

## Subscriptions

Ditto’s synchronization is event-driven, using subscription queries to automatically receive real-time updates. These queries specify the desired data, whether it’s an entire collection or a filtered subset. Once subscribed, Ditto continuously syncs matching documents across connected peers.

# Conflict Resolution

Ditto uses Conflict-Free Replicated Data Types (CRDTs) for conflict resolution, ensuring all peers converge to the same document value even after independent edits. This process is deterministic, meaning updates merge in any order while still reaching a consistent value. The resolution is also predictable and meaningful, preserving the original input in a rational way rather than defaulting to arbitrary values.

## Lab 1: Configure a Subscription Query

In this lab, we will confgure a DQL SELECT query to create a subscription that will define what data we want to synchronize. 

Follow the instructions based on what platform you are using:

- [Configure a Subscription Query - Swift UI iOS Application](lab1/swift.md)
- [Configure a Subscription Query - Android JetPack Compose with Kotlin Application](lab1/android.md)
- [Configure a Subscription Query - Flutter Application](lab1/flutter.md)

If you are stuck, you can refer to the `.finished` files in the [lab1](./lab1) directory with the corresponding platform name. 

# The Ditto Portal Query Editor 

The Ditto Portal provides a query editor that allows you to examine the data in your BigPeer database that is hosted in the Ditto Cloud.  You can use DQL to insert, update, delete, or query data in the BigPeer database.  We can use the query editor to validate that the subscription query is working as expected.  

## Lab 2: Validate Sync Process 

In this lab, we will start the sync process and validate that the data is being synchronized between the mobile app and the Ditto Portal.  

Follow the instructions based on what platform you are using:

- [Validate Sync Process - Swift UI iOS Application](lab2/swift.md)
- [Validate Sync Process - Android JetPack Compose with Kotlin Application](lab2/android.md)
- [Validate Sync Process - Flutter Application](lab2/flutter.md)

## ❓ Knowledge Check 

1. What does a subscription query do?
   - a) It tells Ditto what data we want to synchronize between the mobile app and the Big Peer
   - b) It tells Ditto what data we want to synchronize between the mobile app and other Small Peers
   - c) both A and B
   - d) None of the above 

2. What does CRDT stand for?
   - a) Conflict-Frictionless Replication Data Type
   - b) Conflict-Free Replicated Data Type
   - c) Conflict-Free Reduced Data Transmission
   - d) Conflict-Free Replicated Data Transaction 

The answer can be found in the [answer file](.answer).

## Summary

🎉 Congratulations 🙌! In this lesson, you have learned:
- What Ditto Subscription are and how they work 
- Conflict-Free Replicated Data Types (CRDTs) are used for conflict resolution 
- How to use the Ditto Portal Query Editor to validate the sync process between the mobile app and the Ditto Cloud

## 📚 References

- [Syncing Data](https://docs.ditto.live/key-concepts/syncing-data)
- [Managing Subscriptions](https://docs.ditto.live/sdk/latest/sync/syncing-data)
- [Starting and Stopping Sync](https://docs.ditto.live/sdk/latest/sync/start-and-stop-sync)
- [DQL](https://docs.ditto.live/dql/dql)
- [Data Browser and Editor](https://docs.ditto.live/cloud/portal/data-browser-and-editor)

## 🎉 Course Completion

You have now completed the 101 course and got the mobile app up and running! This is a major accomplishment and you should be proud of yourself!  Our work is not done yet, we need to look into syncing information using Peer-to-Peer syncing and adding more functionality to our mobile application.

## Next Steps

Head back to our [course listing](../README.md) to start the next course! 