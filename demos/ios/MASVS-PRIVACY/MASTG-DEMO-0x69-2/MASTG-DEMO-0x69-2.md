---
platform: ios
title: Declaring Sensitive Permissions in entitlements.plist
code: [swift]
id: MASTG-DEMO-0x69-2
test: MASTG-TEST-0x69-2
---

### Sample

The code snippet below shows sample code that accesses protected resources requiring entitlements. The app's `entitlements.plist` file declares multiple permissions that the app uses to request special access.

{{ ../MASTG-DEMO-0x69/MastgTest.swift }}

### Steps

1. Unzip the app package and locate the main app binary (@MASTG-TECH-0058), which in this case is `./Payload/MASTestApp.app/MASTestApp`.
2. Run `run.sh` to extract the entitlements using @MASTG-TECH-0111.

{{ run.sh }}

### Observation

The output reveals the entitlements declared in the app's entitlements.plist file.

{{ output.txt }}

### Evaluation

The test fails because the app declares sensitive entitlements that is excessive for its core functionality.
