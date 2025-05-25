package utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.LivePage;
import java.io.File;
import java.io.IOException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.apache.commons.io.FileUtils;
import io.appium.java_client.android.AndroidDriver;

public class HelperModules {

    public static void processAndPrintLiveEvents(WebDriver driver) {
        List<EventData> allEvents = new ArrayList<>();
        try {
            // Use selectors from LivePage
            WebElement block = driver.findElement(LivePage.firstEventResultBlock);
            List<WebElement> texts = block.findElements(LivePage.eventBlockTextView);
            List<String> values = new ArrayList<>();

            // Always fetch text values freshly, avoid stale elements
            for (WebElement t : texts)
                values.add(t.getText());

            String[] skipWords = { "chevron_right", "HT", "", "FT" };
            java.util.function.Predicate<String> isSkipWord = s -> s == null
                    || s.isEmpty()
                    || s.matches("\\d+:\\d+") // Time like 56:56
                    || s.matches("\\d{1,3}") // Pure number (could be time, id, etc)
                    || java.util.Arrays.asList(skipWords).contains(s);

            for (int i = 0; i < values.size(); i++) {
                if ("Match Result".equals(values.get(i))) {
                    // Odds: next 3 values
                    List<Double> odds = new ArrayList<>();
                    for (int k = 1; k <= 3; k++) {
                        if ((i + k) < values.size()) {
                            String oddStr = values.get(i + k);
                            try {
                                if (oddStr.matches("\\d+(\\.\\d+)?"))
                                    odds.add(Double.parseDouble(oddStr));
                            } catch (Exception ignore) {
                            }
                        }
                    }

                    // Home/Away Score: previous 2 digits before Match Result
                    int homeScore = -1, awayScore = -1;
                    String homeScoreStr = "", awayScoreStr = "";
                    int scoreIdx2 = i - 1, scoreIdx1 = i - 2;
                    if (scoreIdx2 >= 0 && values.get(scoreIdx2).matches("-?\\d+"))
                        awayScoreStr = values.get(scoreIdx2);
                    if (scoreIdx1 >= 0 && values.get(scoreIdx1).matches("-?\\d+"))
                        homeScoreStr = values.get(scoreIdx1);
                    try {
                        homeScore = !homeScoreStr.isEmpty() ? Integer.parseInt(homeScoreStr) : -1;
                        awayScore = !awayScoreStr.isEmpty() ? Integer.parseInt(awayScoreStr) : -1;
                    } catch (Exception e) {
                    }

                    // Find Home/Away Team: scan backwards for two valid team names
                    String homeTeam = "", awayTeam = "";
                    int ptr = scoreIdx1 - 1;
                    List<String> foundNames = new ArrayList<>();
                    while (ptr >= 0 && foundNames.size() < 2) {
                        String val = values.get(ptr);
                        if (!isSkipWord.test(val)) {
                            foundNames.add(0, val); // Insert at start (home, then away)
                        }
                        ptr--;
                    }
                    if (foundNames.size() == 2) {
                        homeTeam = foundNames.get(0);
                        awayTeam = foundNames.get(1);
                    }

                    // Find League: scan backwards for last "expand_more" (get previous)
                    String league = "";
                    int leagueIdx = ptr;
                    while (leagueIdx >= 0) {
                        if ("expand_more".equals(values.get(leagueIdx))) {
                            league = leagueIdx - 1 >= 0 ? values.get(leagueIdx - 1) : "";
                            break;
                        }
                        leagueIdx--;
                    }

                    // Defensive: Only add if odds found and team names not empty
                    if (odds.size() == 3 && !homeTeam.isEmpty() && !awayTeam.isEmpty()) {
                        allEvents.add(new EventData(homeTeam, awayTeam, homeScore, awayScore, "Match Result", odds));
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error extracting event: " + e.getMessage());
        }

        // STEP 1: Filter events with any odd in [1.50, 2.50]
        System.out.println("------ Events with at least one odd between 1.50 and 2.50 ------");
        for (EventData event : allEvents) {
            boolean hasOddInRange = false;
            for (Double odd : event.odds) {
                if (odd >= 1.50 && odd <= 2.50) {
                    hasOddInRange = true;
                    break;
                }
            }
            if (hasOddInRange) {
                System.out.println(event);
            }
        }
        System.out.println("---------------------------------------------------------------");

        // STEP 2: Sort events by sum of odds (highest to lowest)
        allEvents.sort((a, b) -> {
            double sumB = b.odds.stream().mapToDouble(Double::doubleValue).sum();
            double sumA = a.odds.stream().mapToDouble(Double::doubleValue).sum();
            return Double.compare(sumB, sumA);
        });
        System.out.println("------ Events sorted by sum of odds (descending) ------");
        for (EventData event : allEvents) {
            double sum = event.odds.stream().mapToDouble(Double::doubleValue).sum();
            System.out.println(String.format("%.2f | %s", sum, event));
        }
        System.out.println("------------------------------------------------------");

        // STEP 3: Print only events where homeScore == awayScore
        System.out.println("------ Events with Equal Scores (e.g. 1:1, 2:2, ...) ------");
        for (EventData event : allEvents) {
            if (event.homeScore == event.awayScore) {
                System.out.println(event);
            }
        }
        System.out.println("-----------------------------------------------------------");
    }

    // Screenshot logic is now fully separated:
    public static void takeScreenshot(WebDriver driver, String fileName) {
        // Format: screenshots/football_live_events_DD-MM-YYYY-HH:MM.png
        try {
            File screenshotDir = new File("screenshots");
            if (!screenshotDir.exists())
                screenshotDir.mkdirs();
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dest = new File(screenshotDir, fileName);
            FileUtils.copyFile(src, dest);
            System.out.println("Screenshot saved: " + dest.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Failed to save screenshot: " + e.getMessage());
        }
    }

    public static String getCurrentTimestamp() {
        // Format: DD-MM-YYYY-HH:MM
        return new SimpleDateFormat("dd-MM-yyyy-HH:mm").format(new Date());
    }
}