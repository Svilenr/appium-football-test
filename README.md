# Appium Football Test ⚽️📱

Automated end-to-end UI tests for the Sportempire Android app using **Appium**, **TestNG**, and the **Page Object Model**.

## 📖 About

This project demonstrates advanced mobile UI automation for live football events on the Sportempire app.  
It extracts and sorts live football event data (including odds), prints rich reports to both console and file, and captures screenshots for full test traceability.

Key Features:
- Built with **Java 17**, [Appium Java Client 9.x](https://github.com/appium/java-client), and [Selenium 4.x](https://www.selenium.dev/).
- **TestNG** for test structure.
- Page Object Model for scalable, maintainable code.
- **Custom reporting:** Prints sorted, filtered live football event info to both the console and a file on every test run.
- **Automatic screenshots** after each test.
- .gitignore covers `screenshots/` and Maven’s `target/` directory.

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3.6+
- Node.js & Appium server installed (`npm install -g appium`)
- Android device or emulator running (and recognized by `adb devices`)
- [Sportempire](https://sportempire.com) Android app installed or available as an APK

### Clone the Project

```bash
git clone https://github.com/Svilenr/appium-football-test.git
cd appium-football-test

