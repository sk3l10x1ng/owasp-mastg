---
platform: ios
title: Runtime Tracing of Permission API Calls
id: MASTG-TEST-0x69-3
type: [dynamic]
weakness: MASWE-0117
profiles: [L1, L2]
---

## Overview

This test is the dynamic counterpart to @MASTG-TEST-0x69 and @MASTG-TEST-0x69-2.

While static analysis identifies declared permissions in `Info.plist` and entitlements, dynamic analysis reveals which permissions are actually requested at runtime and under what conditions.

## Steps

1. Run a dynamic analysis tool like @MASTG-TOOL-0039 and hook iOS permission APIs such as `CLLocationManager.requestWhenInUseAuthorization`, `AVCaptureDevice.requestAccessForMediaType:completionHandler:`, `CNContactStore.requestAccessForEntityType:completionHandler:`, and `UNUserNotificationCenter.requestAuthorizationWithOptions:completionHandler:`.
2. Interact with the app to trigger permission requests.

## Observation

The output should contain a list of permission API calls made at runtime, including the permission type and the authorization status.

## Evaluation

The test fails if the app requests permissions at runtime that are not justified by its core functionality.
