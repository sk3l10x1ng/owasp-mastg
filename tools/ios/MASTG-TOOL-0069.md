---
title: Usbmuxd
platform: ios
source: https://github.com/libimobiledevice/usbmuxd
---

[usbmuxd](https://github.com/libimobiledevice/usbmuxd "usbmuxd") is a socket daemon that monitors USB iPhone connections. You can use it to map the mobile device's localhost listening sockets to TCP ports on your host computer. This allows you to conveniently SSH into your iOS device without setting up an actual network connection. When usbmuxd detects an iPhone running in normal mode, it connects to the phone and begins relaying requests that it receives via `/var/run/usbmuxd`.
