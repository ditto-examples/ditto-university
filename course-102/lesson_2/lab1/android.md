# Lab 1: Adding a Second Device with Kotlin and JetPack Compose

## Overview

In this lab, you'll learn how to deploy and run your Ditto-enabled Android application on multiple physical devices and emulators simultaneously. This is crucial for testing P2P communication between Android devices during development.

## Setting Up Physical Devices

### Enable Developer Options

1. **On each Android device:**
   - Go to **Settings** → **About phone**
   - Tap **Build number** 7 times
   - Enter your PIN/password when prompted
   - You'll see "You are now a developer!"

2. **Enable USB Debugging:**
   - Go to **Settings** → **System** → **Developer options**
   - Toggle **USB debugging** ON
   - Toggle **Install via USB** ON (if available)

3. **Connect devices to your computer:**
   - Use USB cables to connect each device
   - On each device, tap "Allow" when prompted to trust the computer
   - Check "Always allow from this computer" for convenience

## Running on Multiple Devices in Android Studio

### Method 1: Sequential Deployment

1. **Open your project in Android Studio**

2. **Verify connected devices:**
   - Look at the device dropdown in the toolbar
   - You should see all connected physical devices and running emulators

3. **Deploy to first device:**
   - Select the first device from the dropdown
   - Click Run (▶️) or press `Shift + F10`
   - Wait for the app to install and launch

4. **Deploy to additional devices:**
   - Without stopping the first app, select the next device from the dropdown
   - Click Run again
   - Repeat for each device

### Method 2: Using ADB Commands

For advanced users, deploy via command line:

```bash
# List connected devices
adb devices

# Install APK on specific device
adb -s <DEVICE_ID> install -r app/build/outputs/apk/debug/app-debug.apk

# Install on all connected devices
for device in $(adb devices | grep -v "List" | awk '{print $1}'); do
    adb -s $device install -r app/build/outputs/apk/debug/app-debug.apk
done

# Launch app on specific device
adb -s <DEVICE_ID> shell am start -n com.yourcompany.yourapp/.MainActivity
```

## Testing P2P Communication

Once devices are running your app:

1. **Verify Ditto initialization:**
   - Check logcat for Ditto startup messages
   - Look for peer discovery logs

2. **Monitor device connections:**
   - Use Android Studio's Logcat with filters:
   ```
   tag:Ditto level:verbose
   ```

3. **Test synchronization:**
   - Create data on one device
   - Verify it appears on other devices
   - Test with different network conditions

## Troubleshooting

### Physical Device Issues

- **Device not showing up:**
  - Try different USB cable/port
  - Revoke USB debugging authorizations and re-allow
  - Restart adb: `adb kill-server && adb start-server`

- **Installation fails:**
  - Check available storage on device
  - Uninstall previous versions
  - Disable Play Protect temporarily


## Next Steps

Now that you can run multiple simulators, try:
- Creating data on one device and watching it sync to others
- Testing conflict resolution by modifying the same data on multiple devices
- Simulating network partitions by turning sync mode on/off 

[Return to the lesson](../README.md) to continue.