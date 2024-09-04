---
title: r2frida
platform: generic
source: https://github.com/nowsecure/r2frida
---

[r2frida](https://github.com/nowsecure/r2frida "r2frida on Github") is a project that allows radare2 to connect to Frida, effectively merging the powerful reverse engineering capabilities of radare2 with the dynamic instrumentation toolkit of Frida. r2frida can be used in both on Android and iOS, allowing you to:

- Attach radare2 to any local process or remote frida-server via USB or TCP.
- Read/Write memory from the target process.
- Load Frida information such as maps, symbols, imports, classes and methods into radare2.
- Call r2 commands from Frida as it exposes the r2pipe interface into the Frida Javascript API.

Please refer to [r2frida's official installation instructions](https://github.com/nowsecure/r2frida/blob/master/README.md#installation "r2frida installation instructions").

With frida-server running, you should now be able to attach to it using the pid, spawn path, host and port, or device-id. For example, to attach to PID 1234:

```bash
r2 frida://1234
```

For more examples on how to connect to frida-server, [see the usage section in the r2frida's README page](https://github.com/nowsecure/r2frida/blob/master/README.md#usage "r2frida usage").

> The following examples were executed using an Android app but also apply to iOS apps.

Once in the r2frida session, all commands start with `:` or `=!`. For example, in radare2 you'd run `i` to display the binary information, but in r2frida you'd use `:i`.

> See all options with `r2 frida://?`.

```bash
[0x00000000]> :i
arch                x86
bits                64
os                  linux
pid                 2218
uid                 1000
objc                false
runtime             V8
java                false
cylang              false
pageSize            4096
pointerSize         8
codeSigningPolicy   optional
isDebuggerAttached  false
```

To search in memory for a specific keyword, you may use the search command `:/`:

```bash
[0x00000000]> :/ unacceptable
Searching 12 bytes: 75 6e 61 63 63 65 70 74 61 62 6c 65
Searching 12 bytes in [0x0000561f05ebf000-0x0000561f05eca000]
...
Searching 12 bytes in [0xffffffffff600000-0xffffffffff601000]
hits: 23
0x561f072d89ee hit12_0 unacceptable policyunsupported md algorithmvar bad valuec
0x561f0732a91a hit12_1 unacceptableSearching 12 bytes: 75 6e 61 63 63 65 70 74 61
```

To output the search results in JSON format, we simply add `j` to our previous search command (just as we do in the r2 shell). This can be used in most of the commands:

```bash
[0x00000000]> :/j unacceptable
Searching 12 bytes: 75 6e 61 63 63 65 70 74 61 62 6c 65
Searching 12 bytes in [0x0000561f05ebf000-0x0000561f05eca000]
...
Searching 12 bytes in [0xffffffffff600000-0xffffffffff601000]
hits: 23
{"address":"0x561f072c4223","size":12,"flag":"hit14_1","content":"unacceptable \
policyunsupported md algorithmvar bad valuec0"},{"address":"0x561f072c4275", \
"size":12,"flag":"hit14_2","content":"unacceptableSearching 12 bytes: 75 6e 61 \
63 63 65 70 74 61"},{"address":"0x561f072c42c8","size":12,"flag":"hit14_3", \
"content":"unacceptableSearching 12 bytes: 75 6e 61 63 63 65 70 74 61 "},
...
```

To list the loaded libraries use the command `:il` and filter the results using the internal grep from radare2 with the command `~`. For example, the following command will list the loaded libraries matching the keywords `keystore`, `ssl` and `crypto`:

```bash
[0x00000000]> :il~keystore,ssl,crypto
0x00007f3357b8e000 libssl.so.1.1
0x00007f3357716000 libcrypto.so.1.1
```

Similarly, to list the exports and filter the results by a specific keyword:

```bash
[0x00000000]> :iE libssl.so.1.1~CIPHER
0x7f3357bb7ef0 f SSL_CIPHER_get_bits
0x7f3357bb8260 f SSL_CIPHER_find
0x7f3357bb82c0 f SSL_CIPHER_get_digest_nid
0x7f3357bb8380 f SSL_CIPHER_is_aead
0x7f3357bb8270 f SSL_CIPHER_get_cipher_nid
0x7f3357bb7ed0 f SSL_CIPHER_get_name
0x7f3357bb8340 f SSL_CIPHER_get_auth_nid
0x7f3357bb7930 f SSL_CIPHER_description
0x7f3357bb8300 f SSL_CIPHER_get_kx_nid
0x7f3357bb7ea0 f SSL_CIPHER_get_version
0x7f3357bb7f10 f SSL_CIPHER_get_id
```

To list or set a breakpoint use the command db. This is useful when analyzing/modifying memory:

```bash
[0x00000000]> :db
```

Finally, remember that you can also run Frida JavaScript code with `:.` plus the name of the script:

```bash
[0x00000000]> :. agent.js
```

You can find more examples on [how to use r2frida](https://github.com/enovella/r2frida-wiki "Using r2frida") on their Wiki project.
