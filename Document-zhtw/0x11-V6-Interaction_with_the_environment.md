# V6: 平台互動規範

## 控制目標

這個群組中的控制確保應用程式以安全的方式使用平台API和標準元件。 此外，控制還涵蓋應用程式（IPC）之間的通訊。

## 安全驗證要求

| # | MSTG-ID | 描述 | L1 | L2 |
| --- | --- | --- | --- | --- |
| **6.1** | MSTG‑PLATFORM‑1 | 應用程式僅請求必要的最小權限集。 | ✓ | ✓ |
| **6.2** | MSTG‑PLATFORM‑2 | 來自外部來源和用戶的所有輸入都經過驗證，並在必要時進行消毒。 這包括通過UI，IPC機制像是意圖、自定義URL和網路來源接收的資料。 | ✓ | ✓ |
| **6.3** | MSTG‑PLATFORM‑3 | 除非適當保護這些機制，否則應用程式不會通過自定義URL結構輸出機密功能。 | ✓ | ✓ |
| **6.4** | MSTG‑PLATFORM‑4 | 除非適當保護這些機制，否則應用程序不會通過IPC設備輸出機密功能。 | ✓ | ✓ |
| **6.5** | MSTG‑PLATFORM‑5 | 除非明確地要求，否則在 WebView 中禁止使用 JavaScript。 | ✓ | ✓ |
| **6.6** | MSTG‑PLATFORM‑6 | WebViews被設置為僅允許所需最少量協議處理程序（理想情況下，僅支援https）。 潛在危險的處理程序，例如f ile、tel 和 app-id 則不能使用。 | ✓ | ✓ |
| **6.7** | MSTG‑PLATFORM‑7 | 如果應用程式的 native 方法被暴露給 WebView，請驗證 WebView 是否僅提供應用程式套件中包含的JavaScript。 | ✓ | ✓ |
| **6.8** | MSTG‑PLATFORM‑8 | 如果有任何物件反序列化，要使用安全序列化 API 執行。 | ✓ | ✓ |
| **6.9** | MSTG‑PLATFORM‑9 | 行動應用程式需保護自己防衛螢幕覆蓋攻擊 (Screen Overlay Attack)。（僅針對 Android 系統） |  | ✓ |
| **6.10** | MSTG‑PLATFORM‑10 | WebView 的暫存，儲存，以及其他讀取的資源（如 JavaScript 等），需在 WebView 結束前全數清除。  |  | ✓ |
| **6.11** | MSTG‑PLATFORM‑11 | 確認當機敏資料被輸入前，並沒有使用任何其他廠商所製作的客製鍵盤應用程式。 | | ✓ |

## 參考

OWASP Mobile Security Testing Guide 提供了有關驗證本章節中列出的準則的詳細使用說明。

- Android - <https://github.com/OWASP/owasp-mstg/blob/master/Document/0x05h-Testing-Platform-Interaction.md>
- iOS - <https://github.com/OWASP/owasp-mstg/blob/master/Document/0x06h-Testing-Platform-Interaction.md>

更多資訊請參閱：

- OWASP Mobile Top 10: M1 (Improper Platform Usage) - <https://www.owasp.org/index.php/Mobile_Top_10_2016-M1-Improper_Platform_Usage>
- CWE 20 (Improper Input Validation) - <https://cwe.mitre.org/data/definitions/20.html>
- CWE 749 (Exposed Dangerous Method or Function) - <https://cwe.mitre.org/data/definitions/749.html>