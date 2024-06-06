---
platform: ios
title: Hardcoded-crypto-keys-usage
tools: [semgrep]
code: [swift]
---

### Sample

{{ hardcoded_keys.swift }}

### Steps

Let's run our semgrep rule against the sample code.

{{ ../rules/mastg-hardcoded-keys.yml }}

{{ run.sh }}

### Observation

The rule has identified two instances in the code file where hardcoded keys is used. The specified line numbers can be located in the original code for further investigation and remediation.

{{ output.txt }}

### Evaluation

Review each of the reported instances.

- Line 6 , the variable 'keyString'  stores the hardcoded encryption key.
