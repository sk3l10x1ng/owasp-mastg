---
title: Dynamic Analysis on Non-Jailbroken Devices
platform: ios
---

If you don't have access to a jailbroken device, you can patch and repackage the target app to load a dynamic library at startup (e.g. the [Frida gadget](https://www.frida.re/docs/gadget/ "Frida Gadget") to enable dynamic testing with Frida and related tools such as objection). This way, you can instrument the app and do everything you need to do for  dynamic analysis (of course, you can't break out of the sandbox this way). However, this technique only works if the app binary isn't FairPlay-encrypted (i.e., obtained from the App Store).

## Automated Repackaging

[Objection](0x08a-Testing-Tools.md#objection) automates the process of app repackaging. You can find exhaustive documentation on the official [wiki pages](https://github.com/sensepost/objection/wiki "Objection - Documentation").

Using objection's repackaging feature is sufficient for most of use cases. However, in some complex scenarios you might need more fine-grained control or a more customizable repackaging process. In that case, you can read a detailed explanation of the repackaging and resigning process in ["Manual Repackaging"](#manual-repackaging).

## Manual Repackaging

Thanks to Apple's confusing provisioning and code-signing system, re-signing an app is more challenging than you would expect. iOS won't run an app unless you get the provisioning profile and code signature header exactly right. This requires learning many concepts-certificate types, Bundle IDs, application IDs, team identifiers, and how Apple's build tools connect them. Getting the OS to run a binary that hasn't been built via the default method (Xcode) can be a daunting process.

We'll use [optool](0x08a-Testing-Tools.md#optool), Apple's build tools, and some shell commands. Our method is inspired by [Vincent Tan's Swizzler project](https://github.com/vtky/Swizzler2/ "Swizzler"). [The NCC group](https://research.nccgroup.com/2016/10/12/ios-instrumentation-without-jailbreak/ "NCC blog - iOS instrumentation without jailbreak") has described an alternative repackaging method.

To reproduce the steps listed below, download [UnCrackable App for iOS Level 1](../../apps/ios/MASTG-APP-0025.md "UnCrackable App for iOS Level 1") from the OWASP Mobile Testing Guide repository. Our goal is to make the UnCrackable app load `FridaGadget.dylib` during startup so we can instrument the app with Frida.

> Please note that the following steps apply to macOS only, as Xcode is only available for macOS.

## Getting a Developer Provisioning Profile and Certificate

The _provisioning profile_ is a plist file signed by Apple, which adds your code-signing certificate to its list of accepted certificates on one or more devices. In other words, this represents Apple explicitly allowing your app to run for certain reasons, such as debugging on selected devices (development profile). The provisioning profile also includes the _entitlements_ granted to your app. The _certificate_ contains the private key you'll use to sign.

Depending on whether you're registered as an iOS developer, you can obtain a certificate and provisioning profile in one of the following ways:

**With an iOS developer account:**

If you've developed and deployed iOS apps with Xcode before, you already have your own code-signing certificate installed. Use the [`security`](0x08a-Testing-Tools.md#security) command (macOS only) to list your signing identities:

```bash
$ security find-identity -v
 1) 61FA3547E0AF42A11E233F6A2B255E6B6AF262CE "iPhone Distribution: Company Name Ltd."
 2) 8004380F331DCA22CC1B47FB1A805890AE41C938 "iPhone Developer: Bernhard Müller (RV852WND79)"
```

Log into the Apple Developer portal to issue a new App ID, then issue and download the profile. An App ID is a two-part string: a Team ID supplied by Apple and a bundle ID search string that you can set to an arbitrary value, such as `com.example.myapp`. Note that you can use a single App ID to re-sign multiple apps. Make sure you create a _development_ profile and not a _distribution_ profile so that you can debug the app.

In the examples below, I use my signing identity, which is associated with my company's development team. I created the App ID "sg.vp.repackaged" and the provisioning profile "AwesomeRepackaging" for these examples. I ended up with the file `AwesomeRepackaging.mobileprovision`-replace this with your own filename in the shell commands below.

**With a Regular Apple ID:**

Apple will issue a free development provisioning profile even if you're not a paying developer. You can obtain the profile via Xcode and your regular Apple account: simply create an empty iOS project and extract `embedded.mobileprovision` from the app container, which is in the Xcode subdirectory of your home directory: `~/Library/Developer/Xcode/DerivedData/<ProjectName>/Build/Products/Debug-iphoneos/<ProjectName>.app/`. The [NCC blog post "iOS instrumentation without jailbreak"](https://www.nccgroup.trust/au/about-us/newsroom-and-events/blogs/2016/october/ios-instrumentation-without-jailbreak/ "iOS instrumentation without jailbreak") explains this process in great detail.

Once you've obtained the provisioning profile, you can check its contents with the [`security`](0x08a-Testing-Tools.md#security) command. You'll find the entitlements granted to the app in the profile, along with the allowed certificates and devices. You'll need these for code-signing, so extract them to a separate plist file as shown below. Have a look at the file contents to make sure everything is as expected.

```bash
$ security cms -D -i AwesomeRepackaging.mobileprovision > profile.plist
$ /usr/libexec/PlistBuddy -x -c 'Print :Entitlements' profile.plist > entitlements.plist
$ cat entitlements.plist
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
 <key>application-identifier</key>
 <string>LRUD9L355Y.sg.vantagepoint.repackage</string>
 <key>com.apple.developer.team-identifier</key>
 <string>LRUD9L355Y</string>
 <key>get-task-allow</key>
 <true/>
 <key>keychain-access-groups</key>
 <array>
   <string>LRUD9L355Y.*</string>
 </array>
</dict>
</plist>
```

Note the application identifier, which is a combination of the Team ID (LRUD9L355Y) and Bundle ID (sg.vantagepoint.repackage). This provisioning profile is only valid for the app that has this App ID. The `get-task-allow` key is also important: when set to `true`, other processes, such as the debugging server, are allowed to attach to the app (consequently, this would be set to `false` in a distribution profile).
