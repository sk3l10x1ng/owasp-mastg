---
title: Mandatory In-App Update Enforcement on Android
platform: android
id: MASTG-TEST-0x36
type: [static]
weakness: MASWE-0075
profiles: [L2]
---

## Overview

Enforced updates on Android occur when users must install the latest version of an application before they are allowed to continue using it. This mechanism is typically implemented through the [Google Play Core In-App Update API](https://developer.android.com/guide/playcore/in-app-updates/kotlin-java), using the Immediate update type (value `1`). When this flow is triggered, the application restricts access to its functionality until the update has been successfully downloaded and installed.

## Steps

1. Run a static analysis tool such as @MASTG-TOOL-0110 on codebase for usages of the calls to the Play Core in-app update API, specifically `startUpdateFlowForResult`, that are configured with the integer value `1`.

## Observation

The output should contains usage of the application calls `startUpdateFlowForResult` with `AppUpdateOptions.newBuilder(1).build()`. This is the exact signature for triggering a mandatory, non-cancellable update flow.

## Evaluation

The test fails if the app does not enforce updates and still allows users to skip or ignore them.
