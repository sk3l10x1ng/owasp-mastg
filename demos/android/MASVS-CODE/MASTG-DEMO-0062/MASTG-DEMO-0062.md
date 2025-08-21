---
platform: android
title: Enforced Immediate Updates with Play Core API detected by semgrep
id: MASTG-DEMO-0062
code: [kotlin]
test: MASTG-TEST-0290
profiles: [L1, L2]
---

### Sample

The follwoing code implements immediate in-app updates using the Google Play Core API by calling `startUpdateFlowForResult` with `AppUpdateOptions.newBuilder(1)`.

### Steps

Let's run @MASTG-TOOL-0110 rules against the sample code.

{{ ../../../../rules/mastg-android-enforced-updating.yml }}

{{ run.sh }}

### Observation

The output file shows usages of the Google Play Core API enforcing immediate update.

{{ output.txt }}

### Evaluation

The test fails because the app forces users to update immediately and does not provide a fallback. 