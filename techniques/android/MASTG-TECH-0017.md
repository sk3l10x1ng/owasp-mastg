---
title: Decompiling Java Code
platform: android
---

In Android app security testing, if the application is based solely on Java and doesn't have any native code (C/C++ code), the reverse engineering process is relatively easy and recovers (decompiles) almost all the source code. In those cases, black-box testing (with access to the compiled binary, but not the original source code) can get pretty close to white-box testing.

Nevertheless, if the code has been purposefully obfuscated (or some tool-breaking anti-decompilation tricks have been applied), the reverse engineering process may be very time-consuming and unproductive. This also applies to applications that contain native code. They can still be reverse engineered, but the process is not automated and requires knowledge of low-level details.

If you want to look directly into Java source code on a GUI, simply open your APK using [jadx](0x08a-Testing-Tools.md#jadx) or [Bytecode Viewer](0x08a-Testing-Tools.md#bytecode-viewer).

Android decompilers go one step further and attempt to convert Android bytecode back into Java source code, making it more human-readable. Fortunately, Java decompilers generally handle Android bytecode well. The above mentioned tools embed, and sometimes even combine, popular free decompilers such as:

- [JD](http://java-decompiler.github.io/ "JD")
- [JAD](http://www.javadecompilers.com/jad "JAD")
- [jadx](https://github.com/skylot/jadx "jadx")
- [Procyon](https://github.com/mstrobel/procyon "Procyon")
- [CFR](https://www.benf.org/other/cfr/ "CFR")

Alternatively you can use the [APKLab](0x08a-Testing-Tools.md#apklab) extension for Visual Studio Code or run [apkx](0x08a-Testing-Tools.md#apkx) on your APK or use the exported files from the previous tools to open the reversed source code on your preferred IDE.

In the following example we'll be using [UnCrackable App for Android Level 1](../../apps/android/MASTG-APP-0003.md). First, let's install the app on a device or emulator and run it to see what the crackme is about.

<img src="Images/Chapters/0x05c/crackme-1.png" width="400px" />

Seems like we're expected to find some kind of secret code!

We're looking for a secret string stored somewhere inside the app, so the next step is to look inside. First, unzip the APK file (`unzip UnCrackable-Level1.apk -d UnCrackable-Level1`) and look at the content. In the standard setup, all the Java bytecode and app data is in the file `classes.dex` in the app root directory (`UnCrackable-Level1/`). This file conforms to the Dalvik Executable Format (DEX), an Android-specific way of packaging Java programs. Most Java decompilers take plain class files or JARs as input, so you need to convert the classes.dex file into a JAR first. You can do this with `dex2jar` or `enjarify`.

Once you have a JAR file, you can use any free decompiler to produce Java code. In this example, we'll use the [CFR decompiler](https://www.benf.org/other/cfr/ "CFR decompiler"). CFR releases are available on the author's website. CFR was released under an MIT license, so you can use it freely even though its source code is not available.

The easiest way to run CFR is through [apkx](0x08a-Testing-Tools.md#apkx), which also packages `dex2jar` and automates extraction, conversion, and decompilation. Run it on the APK and you should find the decompiled sources in the directory `Uncrackable-Level1/src`. To view the sources, a simple text editor (preferably with syntax highlighting) is fine, but loading the code into a Java IDE makes navigation easier. Let's import the code into IntelliJ, which also provides on-device debugging functionality.

Open IntelliJ and select "Android" as the project type in the left tab of the "New Project" dialog. Enter "Uncrackable1" as the application name and "vantagepoint.sg" as the company name. This results in the package name "sg.vantagepoint.uncrackable1", which matches the original package name. Using a matching package name is important if you want to attach the debugger to the running app later on because IntelliJ uses the package name to identify the correct process.

<img src="Images/Chapters/0x05c/intellij_new_project.jpg" width="100%" />

In the next dialog, pick any API number; you don't actually want to compile the project, so the number doesn't matter. Click "next" and choose "Add no Activity", then click "finish".

Once you have created the project, expand the "1: Project" view on the left and navigate to the folder `app/src/main/java`. Right-click and delete the default package "sg.vantagepoint.uncrackable1" created by IntelliJ.

<img src="Images/Chapters/0x05c/delete_package.jpg" width="400px" />

Now, open the `Uncrackable-Level1/src` directory in a file browser and drag the `sg` directory into the now empty `Java` folder in the IntelliJ project view (hold the "alt" key to copy the folder instead of moving it).

<img src="Images/Chapters/0x05c/drag_code.jpg" width="100%" />

You'll end up with a structure that resembles the original Android Studio project from which the app was built.

<img src="Images/Chapters/0x05c/final_structure.jpg" width="400px" />

See the section "[Reviewing Decompiled Java Code](#reviewing-decompiled-java-code "Reviewing Decompiled Java Code")" below to learn on how to proceed when inspecting the decompiled Java code.
