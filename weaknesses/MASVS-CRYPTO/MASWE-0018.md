---
title: Cryptographic Keys Access Not Restricted
id: MASWE-0018
alias: crypto-key-access-not-restricted
platform: [android, ios]
profiles: [L2]
mappings:
  masvs-v2: [MASVS-CRYPTO-2, MASVS-AUTH-2, MASVS-AUTH-3]

refs:
- https://developer.android.com/reference/android/security/keystore/KeyGenParameterSpec.Builder#setUnlockedDeviceRequired(boolean)
- https://developer.apple.com/documentation/security/ksecattraccessiblewhenunlockedthisdeviceonly
- https://developer.android.com/training/sign-in/biometric-auth#prompt-the-user-to-authenticate-with-biometrics
- https://developer.apple.com/documentation/security/keychain_services/keychain_items/restricting_keychain_item_accessibility#2974973
draft:
  description: Ensuring that cryptographic keys are accessible only under strict conditions,
    such as when the device is unlocked by an authenticated user, within secure application
    contexts, or for limited periods of time, is critical to maintaining the confidentiality
    and integrity of encrypted data.
  topics:
  - from a Background Process
  - locked device (iOS kSecAttrAccessibleWhenUnlockedThisDeviceOnly, Android setUnlockedDeviceRequired)
  - time-based access (duration)
  - Require User Presence
  - application-specific password
  - biometric authentication
  - key use restricted e.g. requiring user auth with biometrics, User Presence.
  - especially for sensitive operations
  - keys restricted/authorized for a duration of time or specific crypto operation,
    etc.
status: draft

---

