---
title: Runtime Reverse Engineering
platform: ios
---

Runtime reverse engineering can be seen as the on-the-fly version of reverse engineering where you don't have the binary data to your host computer. Instead, you'll analyze it straight from the memory of the app.

We'll keep using the [iGoat-Swift](0x08b-Reference-Apps.md#igoat-swift) app, open a session with r2frida `r2 frida://usb//iGoat-Swift` and you can start by displaying the target binary information by using the `:i` command:

```bash
[0x00000000]> :i
arch                arm
bits                64
os                  darwin
pid                 2166
uid                 501
objc                true
runtime             V8
java                false
cylang              true
pageSize            16384
pointerSize         8
codeSigningPolicy   optional
isDebuggerAttached  false
cwd                 /
```

Search all symbols of a certain module with `:is <lib>`, e.g. `:is libboringssl.dylib`.

The following does a case-insensitive search (grep) for symbols including "aes" (`~+aes`).

```bash
[0x00000000]> :is libboringssl.dylib~+aes
0x1863d6ed8 s EVP_aes_128_cbc
0x1863d6ee4 s EVP_aes_192_cbc
0x1863d6ef0 s EVP_aes_256_cbc
0x1863d6f14 s EVP_has_aes_hardware
0x1863d6f1c s aes_init_key
0x1863d728c s aes_cipher
0x0 u ccaes_cbc_decrypt_mode
0x0 u ccaes_cbc_encrypt_mode
...
```

Or you might prefer to look into the imports/exports. For example:

- List all imports of the main binary: `:ii iGoat-Swift`.
- List exports of the libc++.1.dylib library: `:iE /usr/lib/libc++.1.dylib`.

> For big binaries it's recommended to pipe the output to the internal less program by appending `~..`, i.e. `:ii iGoat-Swift~..` (if not, for this binary, you'd get almost 5000 lines printed to your terminal).

The next thing you might want to look at are the classes:

```bash
[0x00000000]> :ic~+passcode
PSPasscodeField
_UITextFieldPasscodeCutoutBackground
UIPasscodeField
PasscodeFieldCell
...
```

List class fields:

```bash
[0x19687256c]> :ic UIPasscodeField
0x000000018eec6680 - becomeFirstResponder
0x000000018eec5d78 - appendString:
0x000000018eec6650 - canBecomeFirstResponder
0x000000018eec6700 - isFirstResponder
0x000000018eec6a60 - hitTest:forEvent:
0x000000018eec5384 - setKeyboardType:
0x000000018eec5c8c - setStringValue:
0x000000018eec5c64 - stringValue
...
```

Imagine that you are interested into `0x000000018eec5c8c - setStringValue:`. You can seek to that address with `s 0x000000018eec5c8c`, analyze that function `af` and print 10 lines of its disassembly `pd 10`:

```bash
[0x18eec5c8c]> pd 10
╭ (fcn) fcn.18eec5c8c 35
│   fcn.18eec5c8c (int32_t arg1, int32_t arg3);
│ bp: 0 (vars 0, args 0)
│ sp: 0 (vars 0, args 0)
│ rg: 2 (vars 0, args 2)
│           0x18eec5c8c      f657bd         not byte [rdi - 0x43]      ; arg1
│           0x18eec5c8f      a9f44f01a9     test eax, 0xa9014ff4
│           0x18eec5c94      fd             std
│       ╭─< 0x18eec5c95      7b02           jnp 0x18eec5c99
│       │   0x18eec5c97      a9fd830091     test eax, 0x910083fd
│           0x18eec5c9c      f30300         add eax, dword [rax]
│           0x18eec5c9f      aa             stosb byte [rdi], al
│       ╭─< 0x18eec5ca0      e003           loopne 0x18eec5ca5
│       │   0x18eec5ca2      02aa9b494197   add ch, byte [rdx - 0x68beb665] ; arg3
╰           0x18eec5ca8      f4             hlt
```

Finally, instead of doing a full memory search for strings, you may want to retrieve the strings from a certain binary and filter them, as you'd do _offline_ with radare2. For this you have to find the binary, seek to it and then run the `:iz` command.

> It's recommended to apply a filter with a keyword `~<keyword>`/`~+<keyword>` to minimize the terminal output. If just want to explore all results you can also pipe them to the internal less `:iz~..`.

```bash
[0x00000000]> :il~iGoa
0x00000001006b8000 iGoat-Swift
[0x00000000]> s 0x00000001006b8000
[0x1006b8000]> :iz
Reading 2.390625MB ...
Do you want to print 8568 lines? (y/N) N
[0x1006b8000]> :iz~+hill
Reading 2.390625MB ...
[0x1006b8000]> :iz~+pass
Reading 2.390625MB ...
0x00000001006b93ed  "passwordTextField"
0x00000001006bb11a  "11iGoat_Swift20KeychainPasswordItemV0C5ErrorO"
0x00000001006bb164  "unexpectedPasswordData"
0x00000001006d3f62  "Error reading password from keychain - "
0x00000001006d40f2  "Incorrect Password"
0x00000001006d4112  "Enter the correct password"
0x00000001006d4632  "T@"UITextField",N,W,VpasswordField"
0x00000001006d46f2  "CREATE TABLE IF NOT EXISTS creds (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT);"
0x00000001006d4792  "INSERT INTO creds(username, password) VALUES(?, ?)"
```

To learn more, please refer to the [r2frida wiki](https://github.com/enovella/r2frida-wiki/blob/master/README.md "r2frida Wiki").
