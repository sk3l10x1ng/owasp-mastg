---
title: Keychain-Dumper
platform: ios
source: https://github.com/mechanico/Keychain-Dumper
---

[Keychain-dumper](https://github.com/mechanico/Keychain-Dumper "keychain-dumper") is an iOS tool to check which keychain items are available to an attacker once an iOS device has been jailbroken. The easiest way to get the tool is to download the binary from its GitHub repo and run it from your device:

```bash
$ git clone https://github.com/ptoomey3/Keychain-Dumper
$ scp -P 2222 Keychain-Dumper/keychain_dumper root@localhost:/tmp/
$ ssh -p 2222 root@localhost
iPhone:~ root# chmod +x /tmp/keychain_dumper
iPhone:~ root# /tmp/keychain_dumper
```

For usage instructions please refer to the [Keychain-dumper](https://github.com/mechanico/Keychain-Dumper "keychain-dumper") GitHub page.
