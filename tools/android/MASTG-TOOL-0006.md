---
title: Android SDK
platform: android
---

Local Android SDK installations are managed via Android Studio. Create an empty project in Android Studio and select **Tools** -> **SDK Manager** to open the SDK Manager GUI. The **SDK Platforms** tab is where you install SDKs for multiple API levels. Recent API levels are:

- Android 11.0 (API level 30)
- Android 10.0 (API level 29)
- Android 9.0 (API level 28)
- Android 8.1 (API level 27)
- Android 8.0 (API level 26)

An overview of all Android codenames, their version number and API levels can be found in the [Android Developer Documentation](https://source.android.com/setup/start/build-numbers "Codenames, Tags, and Build Numbers").

<img src="Images/Chapters/0x05c/sdk_manager.jpg" width="100%" />

Installed SDKs are on the following paths:

Windows:

```bash
C:\Users\<username>\AppData\Local\Android\sdk
```

MacOS:

```bash
/Users/<username>/Library/Android/sdk
```

Note: On Linux, you need to choose an SDK directory. `/opt`, `/srv`, and `/usr/local` are common choices.
