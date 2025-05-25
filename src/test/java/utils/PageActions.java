package utils;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class PageActions {
    private AndroidDriver driver;
    private String storedElementText;
    private Double storedElementValue;

    public PageActions(AndroidDriver driver) {
        this.driver = driver;
    }

    public By getLocator(String selector) {
        if (selector.startsWith("//")) {
            return By.xpath(selector);
        } else if (selector.startsWith("id=")) {
            return By.id(selector.substring(3));
        } else if (selector.startsWith("acc=")) { // Accessibility id
            return AppiumBy.accessibilityId(selector.substring(4));
        } else if (selector.startsWith("class=")) {
            return By.className(selector.substring(6));
        } else if (selector.startsWith("css=")) {
            return By.cssSelector(selector.substring(4));
        } else {
            // Fallback to xpath for Appium, or you can throw
            return By.xpath(selector);
        }
    }

    public WebElement getElement(By selector) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(selector));
    }

    public List<WebElement> getElements(By selector) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(selector));
    }

    public void tap(By selector) {
        getElement(selector).click();
    }

    public void set(By selector, String value) {
        WebElement el = getElement(selector);
        el.clear();
        el.sendKeys(value);
    }

    public boolean isDisplayed(By selector) {
        try {
            return getElement(selector).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void waitFor(By selector) {
        getElement(selector); // Waits for visibility
    }

    public void expectText(By selector, String expected) {
        String text = getElement(selector).getText();
        if (!text.contains(expected)) {
            throw new AssertionError("Text not found! Expected: " + expected + ", but got: " + text);
        }
    }

    public void storeTextFromElement(By selector) {
        storedElementText = getElement(selector).getText().trim();
    }

    public void compareTextWithStoredElement(By selector) {
        String text = getElement(selector).getText();
        if (!text.contains(storedElementText)) {
            throw new AssertionError(
                    "Stored text is not present in the element. Stored: " + storedElementText + ", Element: " + text);
        }
    }

    public void storeNumericValueFromElement(By selector) {
        String text = getElement(selector).getText().replaceAll("[^\\d.]", "");
        storedElementValue = Double.parseDouble(text);
    }

    public void compareNumericValueWithStoredElement(By selector) {
        String text = getElement(selector).getText().replaceAll("[^\\d.]", "");
        double value = Double.parseDouble(text);
        if (value < storedElementValue) {
            throw new AssertionError(
                    "Stored value is greater than new value! Stored: " + storedElementValue + ", New: " + value);
        }
    }

    public void waitForElementToDisappear(By selector) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(selector));
    }

    public void print(String msg) {
        System.out.println(msg);
    }

    public void printSportsWithMoreThan10Events(By selector) {
        List<WebElement> sportRows = getElements(selector);
        for (WebElement row : sportRows) {
            String contentDesc = row.getAttribute("content-desc");
            if (contentDesc != null && contentDesc.trim().length() > 0) {
                int lastSpace = contentDesc.lastIndexOf(' ');
                if (lastSpace > 0) {
                    String sport = contentDesc.substring(0, lastSpace);
                    String countStr = contentDesc.substring(lastSpace + 1);
                    try {
                        int count = Integer.parseInt(countStr);
                        if (count > 10) {
                            // ADD THIS LINE:
                            System.out.println("------");
                            System.out.println(sport + " has " + count + " events.");
                        }
                    } catch (NumberFormatException e) {
                        // Skip if not a number
                    }
                }
            }
        }
    }

    public List<utils.EventData> extractVisibleEvents(By eventBlockSelector) {
        List<utils.EventData> events = new ArrayList<>();
        List<WebElement> blocks = getElements(eventBlockSelector);
        for (WebElement block : blocks) {
            try {
                List<WebElement> texts = block.findElements(By.className("android.widget.TextView"));
                String homeTeam = texts.size() > 0 ? texts.get(0).getText() : "N/A";
                String awayTeam = texts.size() > 1 ? texts.get(1).getText() : "N/A";
                // Extract scores (first two digit-only elements)
                int homeScore = -1, awayScore = -1, found = 0;
                for (WebElement t : texts) {
                    if (t.getText().matches("\\d+")) {
                        if (found == 0) {
                            homeScore = Integer.parseInt(t.getText());
                            found++;
                        } else if (found == 1) {
                            awayScore = Integer.parseInt(t.getText());
                            found++;
                            break;
                        }
                    }
                }
                // Market name: look for "Match Result" or fallback
                String marketName = texts.stream().filter(t -> t.getText().contains("Match Result"))
                        .findFirst().map(WebElement::getText).orElse("N/A");
                // Odds: all numbers with dot
                List<Double> odds = new ArrayList<>();
                texts.stream()
                        .map(WebElement::getText)
                        .filter(txt -> txt.matches("\\d+\\.\\d+"))
                        .forEach(txt -> odds.add(Double.parseDouble(txt)));
                if (odds.size() == 0)
                    odds.add(0.0); // always have at least 1

                events.add(new utils.EventData(homeTeam, awayTeam, homeScore, awayScore, marketName, odds));
            } catch (Exception e) {
                System.out.println("Error extracting event block: " + e.getMessage());
            }
        }
        return events;
    }

    public void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }
}