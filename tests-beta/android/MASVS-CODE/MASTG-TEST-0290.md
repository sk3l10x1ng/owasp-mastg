---
title: Enforced Application Updates
platform: android
id: MASTG-TEST-0290
type: [static]
weakness: MASWE-0036
profiles: [L1, L2]
---

## Overview

Enforced updates on Android occur when an application requires users to install the latest version before they can continue using it. This behavior is commonly implemented via the Google Play Core In-App Update API using the Immediate update type with a value `1`. When the Immediate update flow is triggered, the application blocks access to its functionality until the update is fully downloaded and installed.

## Steps

1. Run a static analysis tool such as @MASTG-TOOL-0110 on codebase for usages of the calls to the Play Core in-app update API, specifically `startUpdateFlowForResult`, that are configured with the integer value `1`.

## Observation

The output file shows usages of the application calls `startUpdateFlowForResult` with `AppUpdateOptions.newBuilder(1).build()`. This is the exact signature for triggering a mandatory, non-cancellable update flow.

## Evaluation

The test fails if the app does not enforce updates and still allows users to skip or ignore them. This means users can continue running old or vulnerable versions of the app, which may expose them to security risks.
