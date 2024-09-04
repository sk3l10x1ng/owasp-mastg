---
title: Reviewing Disassembled Native Code
platform: ios
---

Analyzing disassembled native code requires a good understanding of the calling conventions and instructions used by the underlying platform. In this section we are looking in ARM64 disassembly of the native code. A good starting point to learn about ARM architecture is available at [Introduction to ARM Assembly Basics](https://azeria-labs.com/writing-arm-assembly-part-1/ "Introduction to ARM Assembly Basics") by Azeria Labs Tutorials. This is a quick summary of the things that we will be using in this section:

- In ARM64, a register is of 64 bit in size and referred to as Xn, where n is a number from 0 to 31. If the lower (LSB) 32 bits of the register are used then it's referred to as Wn.
- The input parameters to a function are passed in the X0-X7 registers.
- The return value of the function is passed via the X0 register.
- Load (LDR) and store (STR) instructions are used to read or write to memory from/to a register.
- B, BL, BLX are branch instructions used for calling a function.

As mentioned above as well, Objective-C code is also compiled to native binary code, but analyzing C/C++ native can be more challenging. In case of Objective-C there are various symbols (especially function names) present, which eases the understanding of the code. In the above section we've learned that the presence of function names like `setText`, `isEqualStrings` can help us in quickly understanding the semantics of the code. In case of C/C++ native code, if all the binaries are stripped, there can be very few or no symbols present to assist us into analyzing it.

Decompilers can help us in analyzing native code, but they should be used with caution. Modern decompilers are very sophisticated and among many techniques used by them to decompile code, a few of them are heuristics based. Heuristics based techniques might not always give correct results, one such case being, determining the number of input parameters for a given native function. Having knowledge of analyzing disassembled code, assisted with decompilers can make analyzing native code less error prone.

We will be analyzing the native function identified in `viewDidLoad` function in the previous section. The function is located at offset 0x1000080d4. The return value of this function used in the `setText` function call for the label. This text is used to compare against the user input. Thus, we can be sure that this function will be returning a string or equivalent.

<img src="Images/Chapters/0x06c/manual_reversing_ghidra_native_disassembly.png" width="100%" />

The first thing we can see in the disassembly of the function is that there is no input to the function. The registers X0-X7 are not read throughout the function. Also, there are multiple calls to other functions like the ones at 0x100008158, 0x10000dbf0 etc.

The instructions corresponding to one such function calls can be seen below. The branch instruction `bl` is used to call the function at 0x100008158.

```gnuassembler
1000080f0 1a 00 00 94     bl         FUN_100008158
1000080f4 60 02 00 39     strb       w0,[x19]=>DAT_10000dbf0
```

The return value from the function (found in W0), is stored to the address in register X19 (`strb` stores a byte to the address in register). We can see the same pattern for other function calls, the returned value is stored in X19 register and each time the offset is one more than the previous function call. This behavior can be associated with populating each index of a string array at a time. Each return value is been written to an index of this string array. There are 11 such calls, and from the current evidence we can make an intelligent guess that length of the hidden flag is 11. Towards the end of the disassembly, the function returns with the address to this string array.

```gnuassembler
100008148 e0 03 13 aa     mov        x0=>DAT_10000dbf0,x19
```

To determine the value of the hidden flag we need to know the return value of each of the subsequent function calls identified above. When analyzing the function 0x100006fb4, we can observe that this function is much bigger and more complex than the previous one we analyzed. Function graphs can be very helpful when analyzing complex functions, as it helps into better understanding the control flow of the function. Function graphs can be obtained in Ghidra by clicking the **Display function graph** icon in the sub-menu.

<img src="Images/Chapters/0x06c/manual_reversing_ghidra_function_graph.png" width="100%" />

Manually analyzing all the native functions completely will be time consuming and might not be the wisest approach. In such a scenario using a dynamic analysis approach is highly recommended (see @MASTG-TECH-0067). For instance, by using the techniques like hooking or simply debugging the application, we can easily determine the returned values. Normally it's a good idea to use a dynamic analysis approach and then fallback to manually analyzing the functions in a feedback loop. This way you can benefit from both approaches at the same time while saving time and reducing effort.
