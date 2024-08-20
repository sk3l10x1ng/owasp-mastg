---
title: Information Gathering - API Usage
platform: ios
---

The iOS platform provides many built-in libraries for frequently used functionalities in applications, for example cryptography, Bluetooth, NFC, network and location libraries. Determining the presence of these libraries in an application can give us valuable information about its underlying working.

For instance, if an application is importing the `CC_SHA256` function, it indicates that the application will be performing some kind of hashing operation using the SHA256 algorithm. Further information on how to analyze iOS's cryptographic APIs is discussed in the section "[iOS Cryptographic APIs](../../Document/0x06e-Testing-Cryptography.md "iOS Cryptographic APIs")".

Similarly, the above approach can be used to determine where and how an application is using Bluetooth. For instance, an application performing communication using the Bluetooth channel must use functions from the Core Bluetooth framework such as `CBCentralManager` or `connect`. Using the [iOS Bluetooth documentation](https://developer.apple.com/documentation/corebluetooth "iOS Bluetooth documentation") you can determine the critical functions and start analysis around those function imports.
