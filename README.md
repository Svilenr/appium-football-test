# Appium Football Test ⚽️📱

Automated end-to-end UI tests for the Sportempire Android app using **Appium**, **TestNG**, and the **Page Object Model**.

----------------------------------------------------------------------  
## 📖 About 
----------------------------------------------------------------------

This project demonstrates advanced mobile UI automation for live football events on the Sportempire app.
It extracts and sorts live football event data (including odds), prints rich reports to both console and file, and captures screenshots for full test traceability.

----------------------------------------------------------------------  
## 📦 Key Technologies  
----------------------------------------------------------------------

- Java 17+
- Appium Java Client
- Selenium WebDriver
- TestNG
- Maven
- Page Object Model (POM)
- Custom reporting & screenshot capture

----------------------------------------------------------------------  
## 🗂️ Project Structure  
----------------------------------------------------------------------

appium-football-test/  
├── src/  
│   └── test/  
│       ├── java/  
│       │   ├── base/         # Base test setup (driver init, teardown)  
│       │   ├── pages/        # Page Object Model classes (selectors, flows)  
│       │   ├── tests/        # Test classes (main entry points)  
│       │   └── utils/        # Utilities, helpers, reporting, data models  
├── screenshots/              # Test screenshots (auto-created, .gitignored)  
├── test-output/              # Custom test reports (auto-created, .gitignored)  
├── pom.xml                   # Maven dependencies/configuration  
├── .gitignore  
└── README.md

----------------------------------------------------------------------  
## 🛠️ Setup & Install Dependencies  
----------------------------------------------------------------------

**Prerequisites:**  
- Java 17 or newer  
- Maven 3.x  
- Node.js  
- Appium (`npm install -g appium`)  
- Android emulator or real device (with Sportempire app installed and recognized by `adb devices`)

**Clone the Repo:**  
    git clone https://github.com/Svilenr/appium-football-test.git  
    cd appium-football-test  

**Install Dependencies:**  
    mvn clean install  

----------------------------------------------------------------------  
## ▶️ Running the Tests  
----------------------------------------------------------------------

1. Start Appium Server  
       appium

2. Start Android emulator or connect your Android device (with USB debugging).

3. Run the tests  
       mvn test  

----------------------------------------------------------------------  
## 📊 Output & Reports  
----------------------------------------------------------------------

- **Console Output:** Key test steps, extracted data, and event filtering.  
- **Screenshot:** Each run saves a screenshot in `screenshots/` (timestamped).  
- **Custom Report:** Test output is saved in `test-output/` as a timestamped `.txt` file.  
- **Maven/TestNG reports:** Default HTML/XML reports in `target/surefire-reports/`.  

----------------------------------------------------------------------  
## How It Works  
----------------------------------------------------------------------

- Navigates the Sportempire app to the Live Football section.  
- Extracts visible match events and odds.  
- Filters events by odds, sorts by sum of odds, and finds events with equal scores.  
- Prints all output to console **and** saves to a file.  
- Takes a screenshot at the end of the test.  

----------------------------------------------------------------------  
## Extending the Framework  
----------------------------------------------------------------------

- Add new tests in `src/test/java/tests/`  
- Add page selectors/flows in `src/test/java/pages/`  
- Add helpers/utilities in `src/test/java/utils/`  

----------------------------------------------------------------------  
## .gitignore  
----------------------------------------------------------------------

By default, these folders are gitignored:  

    **/screenshots/
    **/test-output/
    **/target/

----------------------------------------------------------------------  
## 🧑‍💻 Author  
----------------------------------------------------------------------

Svilen Bogdanov

QA Lead, Mobile Automation Enthusiast

----------------------------------------------------------------------  
## 📝 License  
----------------------------------------------------------------------

MIT License
