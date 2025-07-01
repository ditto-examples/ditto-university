# Lab 1: Adding a Second Device with SwiftUI iOS Application

## Overview

In this lab, you'll learn how to run your Ditto-enabled SwiftUI application on multiple iOS simulators simultaneously. This is essential for testing P2P communication between devices during development.

## Prerequisites

- Xcode 15.0 or later
- Your Ditto-enabled SwiftUI app from previous lessons
- macOS with sufficient resources to run multiple simulators

## Running Multiple iOS Simulators

### Method 1: Using Xcode's Built-in Tools

1. **Open your project in Xcode**
   - Launch Xcode and open your Ditto SwiftUI project

2. **Run the first simulator**
   - Select your target device from the device selector in the toolbar (e.g., "iPhone 14 Pro")
   - Click the Run button (▶️) or press `Cmd + R`
   - Wait for the app to launch in the first simulator

3. **Launch additional simulators**
   - Keep the first simulator running
   - In Xcode, go to **Window** → **Devices and Simulators** (or press `Shift + Cmd + 2`)
   - Click the **Simulators** tab
   - Click the **+** button at the bottom left
   - Create a new simulator with a different device type (e.g., "iPhone 15")
   - Close the Devices and Simulators window

4. **Run on the second simulator**
   - With your app still running on the first simulator, change the device selector to your newly created simulator
   - Click the Run button again
   - Xcode will build and deploy to the second simulator without stopping the first one

### Method 2: Using the Simulator App Directly

1. **Open Simulator manually**
   - Open the Simulator app directly from `/Applications/Xcode.app/Contents/Developer/Applications/Simulator.app`
   - Or use Spotlight search (Cmd + Space) and type "Simulator"

2. **Create multiple simulator instances**
   - In the Simulator app, go to **Device** → **New Simulator**
   - Or use **File** → **New Simulator** (Cmd + N)
   - Select different device types for each instance

3. **Deploy from Xcode**
   - In Xcode, select each simulator from the device dropdown
   - Run the app on each one sequentially

### Method 3: Command Line Approach

For advanced users, you can launch simulators via Terminal:

```bash
# List available simulators
xcrun simctl list devices

# Boot a specific simulator (replace UUID with actual device ID)
xcrun simctl boot <DEVICE_UUID>

# Open Simulator app
open -a Simulator

# Install your app (replace with your app bundle path)
xcrun simctl install booted /path/to/your/app.app

# Launch your app (replace with your bundle identifier)
xcrun simctl launch booted com.yourcompany.yourapp
```

## Testing P2P Communication

Once you have multiple simulators running:

1. **Verify Ditto is initialized** on each simulator
2. **Check the network indicator** to ensure devices can see each other
3. **Perform actions** in one simulator and verify they sync to others
4. **Monitor the console** for Ditto peer discovery logs

## Troubleshooting Tips

- **Performance**: Running multiple simulators can be resource-intensive. Close unnecessary apps to free up memory
- **Network Issues**: Ensure all simulators are on the same network (they share your Mac's network by default)
- **Simulator Limit**: macOS typically supports 2-3 simultaneous simulators depending on your hardware

## Best Practices

1. **Use different device types** to test various screen sizes
2. **Reset simulators** between test sessions to ensure clean state: Device → Erase All Content and Settings
3. **Enable console logging** to monitor Ditto's P2P discovery process
4. **Test offline scenarios** by disabling WiFi on individual simulators

## Next Steps

Now that you can run multiple simulators, try:
- Creating data on one device and watching it sync to others
- Testing conflict resolution by modifying the same data on multiple devices
- Simulating network partitions by turning airplane mode on/off 