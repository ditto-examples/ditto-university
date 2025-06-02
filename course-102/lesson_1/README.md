# Lesson 1 🚀 Networking and Discovery

## Multiple Edge Peers (Small Peers)

In order to experience the full power of Ditto, we'll want to run our application on multiple devices, syncing to each other without using the Ditto Cloud. This will enable us to see how changes made on one device are seamlessly replicated to the other devices. To keep things simple, in this course we'll just be using two devices, but you are welcome to apply this to as many devices as you like. 

## The Ditto Mesh

We refer to multiple devices that are connected together and synchronising as being in a `Mesh`. 

In Ditto, the "mesh" is an underlying layer for data synchronization that operates independently of queries and subscriptions. When an application initiates the sync process, devices with the same app ID automatically form a mesh network. This network uses a combination of communication transports (like Wi-Fi, Bluetooth LE, LAN, P2P Wi-Fi, and WebSockets) to allow devices to discover and connect to each other directly. Unlike traditional networks, a peer-to-peer mesh network offers multiple pathways for communication. 

When you you see the word Mesh referenced, we will be referring to a collection of connected devices using the Ditto Edge SDK. In a later course we will show you how to incorporate tools into your applciations so you can see which devices are participating in the mesh in real time. In this course, our Mesh will consist of two devices. 

## AWDL and Wi-Fi Aware

In addition to Bluetooth LE, LAN, and WebSocket transports, Ditto’s mesh technology takes advantage of platform-specific peer-to-peer technologies to establish fast, local connections — even when devices are not on the same traditional Wi-Fi network.

### AWDL (Apple Wireless Direct Link)

On Apple platforms (iOS, iPadOS, macOS, tvOS, watchOS, visionOS), Ditto can use Apple Wireless Direct Link (AWDL), the underlying technology that powers features like AirDrop and AirPlay. AWDL allows Apple devices to discover and connect to each other directly over Wi-Fi without requiring a shared Wi-Fi network or router.

Key points about AWDL:
	•	Available on most modern iPhones, iPads, and Macs (usually from ~2014 onward).
	•	Operates over Wi-Fi interfaces but works in parallel with Bluetooth for service discovery.
	•	Enables high-throughput, low-latency peer-to-peer communication.

With AWDL, Apple devices running the Ditto SDK can form mesh connections even when not connected to the same Wi-Fi access point, making it ideal for close-range, infrastructure-free networking.

### Wi-Fi Aware (Neighbor Awareness Networking, NAN)

On other platforms — particularly Android — Ditto can leverage Wi-Fi Aware, also known as Neighbor Awareness Networking (NAN).

Key points about Wi-Fi Aware:
	•	Supported on many modern Android devices (typically from Android 8.0+ with compatible chipsets).
	•	Allows devices to discover and communicate directly with nearby devices without an internet connection or Wi-Fi access point.
	•	Provides peer-to-peer communication over Wi-Fi, enabling efficient data transfer with low latency.
	•	Requires both hardware and firmware support on the device; not all Android phones include it.

With Wi-Fi Aware, Android devices running Ditto can form mesh connections in a local area, enhancing performance and connectivity even when Bluetooth or LAN connections are unavailable.

### Compatibility Summary

| Technology | Platform | Compatible Devices|
|----------|------------|-------------|
| AWDL | iOS, iPadOS, macOS, tvOS, watchOS, visionOS | Most Apple Devices ~ 2014+ |
| WI-FI Aware | Android, PC, Linux | Many modern Android devices (Android 8+, hardware-supported), Windows 10+, Linux |

## The Ditto Multiplexer  

The Ditto Multiplexer functions by seamlessly switching between active transport types as needed, such as Bluetooth LE, LAN, P2P Wi-Fi, and WebSockets. It automatically chooses the fastest transport available at any one time to send chunks of data over a combination of networks. Crucially, it does this efficiently switching active transports without duplicating data. 

In addition to managing these diverse transports, the multiplexer also breaks down data packets into small fragments before transmission and then reassembles them once they are received on the other side. When these various communication transports collaborate in parallel and the multiplexer automatically switches between them to find the most optimal connection, these diverse transport types collectively forms the `Rainbow Connection`. This "rainbow connection" represents a heterogeneous link using different communication channels to maximize the mesh's general availability and enable multihop links where data can pass through intermediate peers.

## Device Discovery

The process of network discovery involves peers attempting to create a presence graph by advertising and forming connections. This advertising and forming of connections utilizes technologies specific to the transports, such as Bluetooth advertisements and multicast advertisements on the local area network. Ditto uses the [mDNS](https://en.wikipedia.org/wiki/Multicast_DNS) and [DNS-SD](https://en.wikipedia.org/wiki/Zero-configuration_networking#DNS-SD) protocols to discover devices on the network. 

Once devices discover each other through direct connections, they then share information about these direct connections with other peers throughout the mesh. This sharing helps build and regularly update the presence graph on each device, which acts like a map. This "map" allows devices to find routes to other peers, even if they aren't directly connected, enabling multihop links. This constant process of discovery and connection management ensures continuous connectivity and helps prevent the isolation of device groups, known as `islanding`.

## Collection Sync Scopes

User Collection Sync Scopes allow fine-grained control over how data in each collection is shared with other peers that are connected to the Peer-to-Peer Mesh. You can choose to:
	•	Prevent a collection from syncing entirely
	•	Sync only with Ditto Server (a “Big Peer”)
	•	Sync only with nearby devices (Small Peers)

This configuration helps reduce unnecessary data transfer, control document flow, and improve performance.

Note: Sync scopes are defined per collection. Even if a document matches a peer’s subscription, it will not sync unless the collection’s scope allows it.

## Lab 1: Configuring Collection Sync 

In this lab, we will configure `Collection Sync Scopes` — settings that control how data from each collection is shared with connected peers.  To test our application syncing only to other peers, we will update the `Collection Sync Scopes` for the `task` collection and set it to sync only with nearby devices rather than sync with the Ditto Cloud (`Big Peer`).

Follow the instructions based on what platform you are using:

- [Configuring Collection Sync Scopes - Swift UI iOS Application](lab1/swift.md)
- [Configuring Collection Sync Scopes - Android JetPack Compose with Kotlin Application](lab1/android.md)
- [Configuring Collection Sync Scopes - Flutter Application](lab1/flutter.md)

## ❓ Knowledge Check 

1. What is a Mesh?
2. Which is not a communication method used to create the mesh?
3. What is the purpose of the Ditto Multiplexer?
4. What does Collection Sync Scopes control?

The answers can be found in the [answer file](.answer).

## Summary

🎉 Congratulations 🙌! In this lesson, you have learned:
- The Ditto Mesh and how it works
- The Ditto Multiplexer and how it works
- The process of device discovery and how it works
- How to configure Collection Sync Scopes

## 📚 References
TODO
- [Ditto Mesh Networking](https://docs.ditto.live/key-concepts/mesh-networking)
- [Collection Sync Scopes](https://docs.ditto.live/sdk/latest/sync/sync-scopes)

## Next Steps

In the next lesson we will setup multiple devices so we can see how data is sync'd between them using the Ditto Mesh.

[Continue to Lesson 2 - Setting up Multiple Devices →](../lesson_2/README.md)

