---
title: Reviewing Disassembled Objective-C and Swift Code
platform: ios
---

In this section we will be exploring iOS application's binary code manually and perform static analysis on it. Manual analysis can be a slow process and requires immense patience. A good manual analysis can make the dynamic analysis more successful.

There are no hard written rules for performing static analysis, but there are few rules of thumb which can be used to have a systematic approach to manual analysis:

- Understand the working of the application under evaluation - the objective of the application and how it behaves in case of wrong input.
- Explore the various strings present in the application binary, this can be very helpful, for example in spotting interesting functionalities and possible error handling logic in the application.
- Look for functions and classes having names relevant to our objective.
- Lastly, find the various entry points into the application and follow along from there to explore the application.

> Techniques discussed in this section are generic and applicable irrespective of the tools used for analysis.

## Objective-C

In addition to the techniques learned in the "[Disassembling and Decompiling](#disassembling-and-decompiling "Disassembling and Decompiling")" section, for this section you'll need some understanding of the [Objective-C runtime](https://developer.apple.com/documentation/objectivec/objective-c_runtime "Objective-C runtime"). For instance, functions like `_objc_msgSend` or `_objc_release` are specially meaningful for the Objective-C runtime.

We will be using the [UnCrackable App for iOS Level 1](../../apps/ios/MASTG-APP-0025.md "UnCrackable App for iOS Level 1"), which has the simple goal of finding a _secret string_ hidden somewhere in the binary. The application has a single home screen and a user can interact via inputting custom strings in the provided text field.

<img src="Images/Chapters/0x06c/manual_reversing_app_home_screen2.png" width="400px" />

When the user inputs the wrong string, the application shows a pop-up with the "Verification Failed" message.

<img src="Images/Chapters/0x06c/manual_reversing_app_wrong_input.png" width="400px" />

You can keep note of the strings displayed in the pop-up, as this might be helpful when searching for the code where the input is processed and a decision is being made. Luckily, the complexity and interaction with this application is straightforward, which bodes well for our reversing endeavors.

> For static analysis in this section, we will be using Ghidra 9.0.4. Ghidra 9.1_beta auto-analysis has a bug and does not show the Objective-C classes.

We can start by checking the strings present in the binary by opening it in Ghidra. The listed strings might be overwhelming at first, but with some experience in reversing Objective-C code, you'll learn how to _filter_ and discard the strings that are not really helpful or relevant. For instance, the ones shown in screenshot below, which are generated for the Objective-C runtime. Other strings might be helpful in some cases, such as those containing symbols (function names, class names, etc.) and we'll be using them when performing static analysis to check if some specific function is being used.

<img src="Images/Chapters/0x06c/manual_reversing_ghidra_objc_runtime_strings.png" width="100%" />

If we continue our careful analysis, we can spot the string, "Verification Failed", which is used for the pop-up when a wrong input is given. If you follow the cross-references (Xrefs) of this string, you will reach `buttonClick` function of the `ViewController` class. We will look into the `buttonClick` function later in this section. When further checking the other strings in the application, only a few of them look a likely candidate for a _hidden flag_. You can try them and verify as well.

<img src="Images/Chapters/0x06c/manual_reversing_ghidra_strings.png" width="100%" />

Moving forward, we have two paths to take. Either we can start analyzing the `buttonClick` function identified in the above step, or start analyzing the application from the various entry points. In real world situation, most times you will be taking the first path, but from a learning perspective, in this section we will take the latter path.

An iOS application calls different predefined functions provided by the iOS runtime depending on its the state within the [application life cycle](https://developer.apple.com/documentation/uikit/app_and_environment/managing_your_app_s_life_cycle "Managing Your App\'s Life Cycle"). These functions are known as the entry points of the app. For example:

- `[AppDelegate application:didFinishLaunchingWithOptions:]` is called when the application is started for the first time.
- `[AppDelegate applicationDidBecomeActive:]` is called when the application is moving from inactive to active state.

Many applications execute critical code in these sections and therefore they're normally a good starting point in order to follow the code systematically.

Once we're done with the analysis of all the functions in the `AppDelegate` class, we can conclude that there is no relevant code present. The lack of any code in the above functions raises the question - from where is the application's initialization code being called?

Luckily the current application has a small code base, and we can find another `ViewController` class in the **Symbol Tree** view. In this class, function `viewDidLoad` function looks interesting. If you check the documentation of [`viewDidLoad`](https://developer.apple.com/documentation/uikit/uiviewcontroller/1621495-viewdidload "viewDidLoad()"), you can see that it can also be used to perform additional initialization on views.

<img src="Images/Chapters/0x06c/manual_reversing_ghidra_viewdidload_decompile.png" width="600px" />

If we check the decompilation of this function, there are a few interesting things going on. For instance, there is a call to a native function at line 31 and a label is initialized with a `setHidden` flag set to 1 in lines 27-29. You can keep a note of these observations and continue exploring the other functions in this class. For brevity, exploring the other parts of the function is left as an exercise for the readers.

In our first step, we observed that the application verifies the input string only when the UI button is pressed. Thus, analyzing the `buttonClick` function is an obvious target. As earlier mentioned, this function also contains the string we see in the pop-ups. At line 29 a decision is being made, which is based on the result of `isEqualString` (output saved in `uVar1` at line 23). The input for the comparison is coming from the text input field (from the user) and the value of the `label`. Therefore, we can assume that the hidden flag is stored in that label.

<img src="Images/Chapters/0x06c/manual_reversing_ghidra_buttonclick_decompiled.png" width="600px" />

Now we have followed the complete flow and have all the information about the application flow. We also concluded that the hidden flag is present in a text label and in order to determine the value of the label, we need to revisit `viewDidLoad` function, and understand what is happening in the native function identified. Analysis of the native function is discussed in "[Reviewing Disassembled Native Code](#reviewing-disassembled-native-code "Reviewing Disassembled Native Code")".
