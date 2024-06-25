---
title: Waiting for the Debugger
platform: android
---

The [UnCrackable App for Android Level 1](../../apps/android/MASTG-APP-0003.md) is not stupid: it notices that it has been run in debuggable mode and reacts by shutting down. A modal dialog is shown immediately, and the crackme terminates once you tap "OK".

Fortunately, Android's "Developer options" contain the useful "Wait for Debugger" feature, which allows you to automatically suspend an app during startup until a JDWP debugger connects. With this feature, you can connect the debugger before the detection mechanism runs, and trace, debug, and deactivate that mechanism. It's really an unfair advantage, but, on the other hand, reverse engineers never play fair!

<img src="Images/Chapters/0x05c/debugger_detection.png" width="400px" />

In the Developer options, pick `Uncrackable1` as the debugging application and activate the "Wait for Debugger" switch.

<img src="Images/Chapters/0x05c/developer-options.png" width="400px" />

Note: Even with `ro.debuggable` set to "1" in `default.prop`, an app won't show up in the "debug app" list unless the `android:debuggable` flag is set to `"true"` in the Android Manifest.
