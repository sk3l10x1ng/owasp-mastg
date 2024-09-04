---
title: adb
platform: android
---

[adb](https://developer.android.com/studio/command-line/adb "Android Debug Bridge") (Android Debug Bridge), shipped with the Android SDK, bridges the gap between your local development environment and a connected Android device. You'll usually leverage it to test apps on the emulator or a connected device via USB or Wi-Fi. Use the `adb devices` command to list the connected devices and execute it with the `-l` argument to retrieve more details on them.

```bash
$ adb devices -l
List of devices attached
090c285c0b97f748 device usb:1-1 product:razor model:Nexus_7 device:flo
emulator-5554    device product:sdk_google_phone_x86 model:Android_SDK_built_for_x86 device:generic_x86 transport_id:1
```

adb provides other useful commands such as `adb shell` to start an interactive shell on a target and `adb forward` to forward traffic on a specific host port to a different port on a connect device.

```bash
adb forward tcp:<host port> tcp:<device port>
```

```bash
$ adb -s emulator-5554 shell
root@generic_x86:/ # ls
acct
cache
charger
config
...
```

You'll come across different use cases on how you can use adb commands when testing later in this book. Note that you must define the serialnummer of the target device with the `-s` argument (as shown by the previous code snippet) in case you have multiple devices connected.
