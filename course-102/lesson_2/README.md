# Lesson 2 🚀 Running on Multiple Devices

## Emulators, Simulators and Physical Devices

When developing applications for multiple devices, you have several options to debug and test your application. Each platform has its own tools and limitations that are important to understand when working with Ditto's peer-to-peer mesh networking.

### Android Emulators

Android Emulators are virtual devices that run on your computer, allowing you to test applications across different device configurations without physical hardware. While convenient for general development, they have specific limitations when working with Ditto's mesh networking:

- **Network Isolation**: On MacOS, Linux, and sometimes Windows, Android Emulators run behind a virtual router that isolates them from your development machine's network interfaces
- **IP Address Limitations**: All emulator instances use the same IP address (10.0.2.15), making direct communication between emulators impossible
- **Bluetooth/WiFi Limitations**: Emulators cannot use Bluetooth or WiFi to connect to other devices, which is essential for Ditto's mesh networking
- **Multicast Support**: The emulator currently doesn't support multicast protocols (mDNS and DNS-SD) used for device discovery

> [!NOTE] 
> On Windows, you can use the Android Emulator with the [Hyper-V](https://learn.microsoft.com/en-us/dotnet/maui/android/emulator/hardware-acceleration?view=net-maui-9.0#accelerate-with-hyper-v) virtual machine manager if you use [Visual Studio 2022](https://learn.microsoft.com/en-us/dotnet/maui/android/emulator/?view=net-maui-9.0) to make the Android Emulator.  By theory Hyper-V could allow you to create a virtual network that allows emulators to communicate with each other, however, is not officially supported by Google and may not work as expected. 

For testing Ditto's mesh networking with Android, we recommend at least two physical Android devices.

### iOS/iPadOS Simulators 

Apple's iOS/iPadOS Simulator provides a convenient way to test applications, but with some important considerations:

- **Hardware Simulation**: The simulator only partially simulates the operating system and hardware capabilities
- **Bluetooth Limitations**: Full Bluetooth functionality is not supported
- **Mesh Networking**: The simulator does support Ditto's mesh networking using:
  - WiFi connections
  - AWDL (Apple Wireless Direct Link)
  - Communication between multiple iOS/iPadOS/macOS devices

### Cross Platform Development

When developing across different platforms, there are important considerations for testing Ditto's mesh networking:

- **iOS Simulator Limitations**: Cannot test mesh networking with Android, Linux, or Windows devices
- **Physical Device Requirements**: Cross-platform testing requires physical devices for:
  - iOS to Android communication
  - iOS to Windows communication
  - Android to Windows communication
  - Android to Android communication
- **Platform-Specific Features**: Each platform may have unique networking capabilities that affect mesh behavior.

## Lab 1: Adding a Second Device

In this lab, we'll explore how to add multiple devices to our Ditto mesh network by running the same application on different devices. This will demonstrate the real-time synchronization capabilities of Ditto across devices.

Follow the platform-specific instructions:

- [Adding a Second device - Swift UI iOS Application](lab1/swift.md)
- [Adding a Second device - Android JetPack Compose with Kotlin Application](lab1/android.md)
- [Adding a Second device - Flutter Application](lab1/flutter.md)

## ❓ Knowledge Check 

1. What is AWDL? 
2. What is Wi-Fi Aware? 
3. True or False; An Android Emulator can use Bluetooth or WiFi to connect to other devices. 

The answers can be found in the [answer file](.answer).

## Summary

🎉 Congratulations 🙌! In this lesson, you have learned:
- The limitations and capabilities of different development environments for mesh networking
- Best practices for cross-platform testing and development
- How to set up and install your Ditto-powered application on multiple devices


## 📚 References
- [Ditto Key Concepts - Mesh Networking](https://docs.ditto.live/key-concepts/mesh-networking)
- [Android Emulator Networking](https://developer.android.com/studio/run/emulator-networking)
- [iOS Simulator Documentation](https://developer.apple.com/documentation/xcode/testing-in-simulator-versus-testing-on-hardware-devices)

## Next Steps

Now that we have multiple devices set up, we can explore how data synchronization works across the mesh network. In the next lesson, we'll dive into:
- Real-time data synchronization
- Subscription management
- Observer patterns
- Conflict resolution

[Continue to Lesson 2 - Sync, Subscriptions and Observers →](../lesson_2/README.md)

