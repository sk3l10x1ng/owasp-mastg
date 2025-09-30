---
title: Mandatory In-App Update Enforcement on Android
platform: android
id: MASTG-TEST-0x36
type: [static]
weakness: MASWE-0075
profiles: [L2]
---

## Overview

The goal of this test is to verify whether the application enforces updates in a way that blocks usage until the latest version is installed. This is typically achieved using the [Google Play Core In-App Update API](https://developer.android.com/guide/playcore/in-app-updates/kotlin-java) with the Immediate update type (value `1`). The test involves exercising the application when an update is available and observing whether access to functionality is restricted until the update has been successfully downloaded and installed.

## Steps

1. Run a static analysis tool such as @MASTG-TOOL-0110 on codebase for usages of the calls to the Play Core in-app update API, specifically `startUpdateFlowForResult`, that are configured with the integer value `1`.

## Observation

The output should contains usage of the application calls `startUpdateFlowForResult` with `AppUpdateOptions.newBuilder(1).build()`. This is the exact signature for triggering a mandatory, non-cancellable update flow.

## Evaluation

The test fails if the app does not enforce updates and still allows users to skip or ignore them.
