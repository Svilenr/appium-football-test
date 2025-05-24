package tests;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.testng.annotations.Test;
import base.BaseTest;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import utils.PageActions;
import utils.EventData;
import pages.HomePage;
import pages.LivePage;

public class SimpleAppiumTest extends BaseTest {

        @Test
        public void openMenuAndGoToFootballLive() {
                PageActions actions = new PageActions(driver);

                // 1. Open side menu and go to Live page
                actions.sleep(3000); // 3 seconds
                actions.waitFor(HomePage.MENU_BUTTON);
                actions.tap(HomePage.MENU_BUTTON);
                actions.waitFor(HomePage.LIVE_BUTTON);
                actions.tap(HomePage.LIVE_BUTTON);

                // 2. Wait for Football button and sports event rows to load
                actions.waitFor(LivePage.FOOTBALL_BUTTON);
                actions.waitFor(LivePage.SPORTS_EVENT_ROW);

                // 3. Print all sports with more than 10 events (bonus)
                actions.printSportsWithMoreThan10Events(LivePage.SPORTS_EVENT_ROW);

                // 4. Tap Football to see all football live events
                actions.tap(LivePage.FOOTBALL_BUTTON);
                actions.sleep(3000); // Ensure events loaded

                List<EventData> allEvents = new ArrayList<>();

                // 5. Extract and print all events from the first visible block
                try {
                        // Find first block containing events
                        WebElement block = driver.findElement(By.xpath(
                                        "(//android.view.ViewGroup[.//android.widget.TextView[contains(@text,'Match Result')]])[1]"));

                        List<WebElement> texts = block.findElements(By.className("android.widget.TextView"));
                        List<String> values = new ArrayList<>();
                        for (WebElement t : texts)
                                values.add(t.getText());

                        // Known non-team keywords
                        String[] skipWords = { "chevron_right", "HT", "", "FT" };

                        // Helper to check if a string is a skip word
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
                                                homeScore = !homeScoreStr.isEmpty() ? Integer.parseInt(homeScoreStr)
                                                                : -1;
                                                awayScore = !awayScoreStr.isEmpty() ? Integer.parseInt(awayScoreStr)
                                                                : -1;
                                        } catch (Exception e) {
                                        }

                                        // Find Home/Away Team: scan backwards for two valid team names
                                        String homeTeam = "", awayTeam = "";
                                        int ptr = scoreIdx1 - 1;
                                        List<String> foundNames = new ArrayList<>();
                                        while (ptr >= 0 && foundNames.size() < 2) {
                                                String val = values.get(ptr);
                                                if (!isSkipWord.test(val)) {
                                                        foundNames.add(0, val); // Insert at start (so order is home,
                                                                                // away)
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

                                        allEvents.add(new EventData(homeTeam, awayTeam, homeScore, awayScore,
                                                        "Match Result", odds));
                                }
                        }

                        System.out.println("--------------------------");

                } catch (Exception e) {
                        System.out.println("Error extracting event: " + e.getMessage());
                }

                // ---- STEP 1: Filter all events by odd range 1.50 - 2.50 ----
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

                // ---- STEP 2: Sort events by sum of odds (highest to lowest) ----
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

                // ---- STEP 3: Filter events where home and away score are equal ----
                System.out.println("------ Events with Equal Scores (e.g. 1:1, 2:2, ...) ------");
                for (EventData event : allEvents) {
                        if (event.homeScore == event.awayScore) {
                                System.out.println(event);
                        }
                }
                System.out.println("-----------------------------------------------------------");

                actions.print("Test finished successfully!");
        }
}