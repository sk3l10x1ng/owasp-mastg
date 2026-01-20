---
platform: ios
title: Runtime Tracing of Permission API Calls with Frida
code: [swift]
id: MASTG-DEMO-0x69-3
test: MASTG-TEST-0x69-3
---

### Sample

The following sample code below requests access to protected resources requiring permissions.

{{ ../MASTG-DEMO-0x69/MastgTest.swift }}

### Steps

1. Install the app on a device (@MASTG-TECH-0056).
2. Make sure you have @MASTG-TOOL-0039 installed on your machine and the frida-server running on the device.
3. Run `run.sh` to spawn the app with Frida and load the permission tracing script.
4. Interact with the app to trigger permission requests (e.g., tap **Request All Permissions** button).
5. Stop the script by pressing `Ctrl+C`.

{{ run.sh # frida_permission_tracer.js }}

### Observation

The output reveals runtime permission API calls including:

- **Permission checks**: Calls to methods like `authorizationStatusForMediaType:`, `authorizationStatusForEntityType:`, and `CLLocationManager.authorizationStatus`.
- **Permission requests**: Calls to methods like `requestAccessForMediaType:completionHandler:`, `requestWhenInUseAuthorization`, and `requestAuthorizationWithOptions:completionHandler:`.
- **Permission status**: The granted/denied status returned by the system for each permission type.

{{ output.txt }}

### Evaluation

The test fails because the app requests multiple sensitive permissions (Location, Camera, Contacts, Calendar, etc.) at runtime that may be excessive for its core functionality. The dynamic analysis confirms that the statically declared permissions in Info.plist and entitlements are actually being requested during app execution.