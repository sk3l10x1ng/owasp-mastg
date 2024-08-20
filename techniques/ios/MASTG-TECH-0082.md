---
title: Get Loaded Native Libraries
platform: ios
---

## Using Objection

You can use the `list_frameworks` command in @MASTG-TOOL-0038 to list all the application's bundles that represent Frameworks.

```bash
...itudehacks.DVIAswiftv2.develop on (iPhone: 13.2.3) [usb] # ios bundles list_frameworks
Executable      Bundle                                     Version    Path
--------------  -----------------------------------------  ---------  -------------------------------------------
Bolts           org.cocoapods.Bolts                        1.9.0      ...8/DVIA-v2.app/Frameworks/Bolts.framework
RealmSwift      org.cocoapods.RealmSwift                   4.1.1      ...A-v2.app/Frameworks/RealmSwift.framework
                                                                      ...ystem/Library/Frameworks/IOKit.framework
...
```

## Using Frida

In Frida REPL process related information can be obtained using the `Process` command. Within the `Process` command the function `enumerateModules` lists the libraries loaded into the process memory.

```bash
[iPhone::com.iOweApp]-> Process.enumerateModules()
[
    {
        "base": "0x10008c000",
        "name": "iOweApp",
        "path": "/private/var/containers/Bundle/Application/F390A491-3524-40EA-B3F8-6C1FA105A23A/iOweApp.app/iOweApp",
        "size": 49152
    },
    {
        "base": "0x1a1c82000",
        "name": "Foundation",
        "path": "/System/Library/Frameworks/Foundation.framework/Foundation",
        "size": 2859008
    },
    {
        "base": "0x1a16f4000",
        "name": "libobjc.A.dylib",
        "path": "/usr/lib/libobjc.A.dylib",
        "size": 200704
    },

    ...
```

Similarly, information related to various threads can be obtained.

```bash
Process.enumerateThreads()
[
    {
        "context": {
            ...
       },
        "id": 1287,
        "state": "waiting"
    },

    ...
```

The `Process` command exposes multiple functions which can be explored as per needs. Some useful functions are `findModuleByAddress`, `findModuleByName` and `enumerateRanges` besides others.
