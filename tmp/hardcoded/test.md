---
platform: ios
title: hardcoded-crypto-keys-usage
tools: [semgrep]
type: [static]
---

## Overview

The application appears to use hardcoded encryption key in the code, making it susceptible to multiple vulnerabilities and potential security breaches.

## Steps

1. Run a static analysis tool (semgrep) on the app and look for uses of insecure algorithm.

## Observation

The output should contain a **list of locations where hardcoded key is used in the code**.

## Evaluation

The test case fails if you can find the hardcoded key is just stored and not used.
