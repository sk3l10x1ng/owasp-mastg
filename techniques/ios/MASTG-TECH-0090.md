---
title: Patching
platform: ios
---

IPA files are actually ZIP archives, so you can use any ZIP tool to unpack the archive.

```bash
unzip UnCrackable-Level1.ipa
```

## Patching Example: Installing Frida Gadget

IF you want to use Frida on non-jailbroken devices you'll need to include `FridaGadget.dylib`. Download it first:

```bash
curl -O https://build.frida.re/frida/ios/lib/FridaGadget.dylib
```

Copy `FridaGadget.dylib` into the app directory and use [optool](../../tools/ios/MASTG-TOOL-0059.md "optool") to add a load command to the "UnCrackable Level 1" binary.

```bash
$ unzip UnCrackable_Level1.ipa
$ cp FridaGadget.dylib Payload/UnCrackable\ Level\ 1.app/
$ optool install -c load -p "@executable_path/FridaGadget.dylib"  -t Payload/UnCrackable\ Level\ 1.app/UnCrackable\ Level\ 1
Found FAT Header
Found thin header...
Found thin header...
Inserting a LC_LOAD_DYLIB command for architecture: arm
Successfully inserted a LC_LOAD_DYLIB command for arm
Inserting a LC_LOAD_DYLIB command for architecture: arm64
Successfully inserted a LC_LOAD_DYLIB command for arm64
Writing executable to Payload/UnCrackable Level 1.app/UnCrackable Level 1...
```

## Patching Example: Making an App Debuggable

By default, an app available on the Apple App Store is not debuggable. In order to debug an iOS application, it must have the `get-task-allow` entitlement enabled. This entitlement allows other processes (like a debugger) to attach to the app. Xcode is not adding the `get-task-allow` entitlement in a distribution provisioning profile; it is only whitelisted and added in a development provisioning profile.

Thus, to debug an iOS application obtained from the App Store, it needs to be re-signed with a development provisioning profile with the `get-task-allow` entitlement. How to re-sign an application is discussed in the next section.
