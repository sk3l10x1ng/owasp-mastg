---
title: dsdump
platform: ios
source: https://github.com/DerekSelander/dsdump
---

[dsdump](https://github.com/DerekSelander/dsdump "dsdump") is a tool to dump Objective-C classes and Swift type descriptors (classes, structs, enums). It only supports Swift version 5 or higher and does not support ARM 32-bit binaries.

The following example shows how you can dump Objective-C classes and Swift type descriptors of an iOS application.

First verify if the app's main binary is a FAT binary containing ARM64:

```bash
$ otool -hv [APP_MAIN_BINARY_FILE]
Mach header
      magic cputype cpusubtype  caps    filetype ncmds sizeofcmds      flags
   MH_MAGIC     ARM         V7  0x00     EXECUTE    39       5016   NOUNDEFS DYLDLINK TWOLEVEL PIE
Mach header
      magic cputype cpusubtype  caps    filetype ncmds sizeofcmds      flags
MH_MAGIC_64   ARM64        ALL  0x00     EXECUTE    38       5728   NOUNDEFS DYLDLINK TWOLEVEL PIE
```

If yes, then we specify the "--arch" parameter to "arm64", otherwise it is not needed if the binary only contains an ARM64 binary.

```bash
# Dump the Objective-C classes to a temporary file
$ dsdump --objc --color --verbose=5 --arch arm64 --defined [APP_MAIN_BINARY_FILE] > /tmp/OBJC.txt

# Dump the Swift type descriptors to a temporary file if the app is implemented in Swift
$ dsdump --swift --color --verbose=5 --arch arm64 --defined [APP_MAIN_BINARY_FILE] > /tmp/SWIFT.txt
```

You can find more information about the inner workings of dsdump and how to programmatically inspect a Mach-O binary to display the compiled Swift types and Objective-C classes in [this article](https://derekselander.github.io/dsdump/ "Building a class-dump in 2020").
