---
title: Native Code Tracing
platform: ios
---

As discussed earlier in this chapter, iOS applications can also contain native code (C/C++ code) and it can be traced using the `frida-trace` CLI as well. For example, you can trace calls to the `open` function by running the following command:

```bash
frida-trace -U -i "open" sg.vp.UnCrackable1
```

The overall approach and further improvisation for tracing native code using Frida is similar to the one discussed in ["Native Code Tracing"](../../techniques/android/MASTG-TECH-0034.md "Native Code Tracing").

Unfortunately, there are no tools such as `strace` or `ftrace` available to trace syscalls or function calls of an iOS app. Only `DTrace` exists, which is a very powerful and versatile tracing tool, but it's only available for MacOS and not for iOS.
