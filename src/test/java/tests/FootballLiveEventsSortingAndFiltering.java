package tests;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import base.BaseTest;
import utils.PageActions;
import utils.HelperModules;
import pages.HomePage;
import pages.LivePage;
import java.io.File;
import java.io.FileNotFoundException;

public class FootballLiveEventsSortingAndFiltering extends BaseTest {

        // Ensures the output folder exists before writing the report
        public static void ensureFolderExists(String folderPath) {
                File folder = new File(folderPath);
                if (!folder.exists()) {
                        folder.mkdirs();
                }
        }

        @BeforeSuite
        public void setupConsoleAndFilePrinter() throws FileNotFoundException {
                String dateStr = new java.text.SimpleDateFormat("dd-MM-yyyy-HH-mm").format(new java.util.Date());
                String folderPath = "test-output";
                String filePath = folderPath + "/custom-report-" + dateStr + ".txt";
                ensureFolderExists(folderPath); // Ensure the output folder exists!
                System.setOut(new utils.ConsoleAndFilePrinter(filePath));
        }

        @Test
        public void openMenuAndGoToFootballLive() {
                PageActions actions = new PageActions(driver);

                // 1. Open side menu and go to Live page
                actions.sleep(3000); // 3 seconds
                actions.waitFor(HomePage.hamburgerMenuButton);
                actions.tap(HomePage.hamburgerMenuButton);
                actions.waitFor(HomePage.liveButton);
                actions.tap(HomePage.liveButton);

                // 2. Wait for Football button and sports event rows to load
                actions.waitFor(LivePage.liveFootballCta);
                actions.waitFor(LivePage.footballLiveEventRow);

                // 3. Print all sports with more than 10 events (bonus)
                actions.printSportsWithMoreThan10Events(LivePage.footballLiveEventRow);

                // 4. Tap Football to see all football live events
                actions.tap(LivePage.liveFootballCta);
                actions.sleep(3000); // Ensure events loaded

                // 5. All the work done in HelperModules!
                HelperModules.processAndPrintLiveEvents(driver);

                // 6. Take a screenshot
                String screenshotName = "football_live_events_" + HelperModules.getCurrentTimestamp() + ".png";
                HelperModules.takeScreenshot(driver, screenshotName);

                actions.print("Test finished successfully!");
        }
}