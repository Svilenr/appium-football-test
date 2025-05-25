package pages;

import org.openqa.selenium.By;

public class LivePage {
    // Selector for the Football sport button on Live page
    public static final By liveFootballCta = By.xpath("(//android.widget.TextView[@text='Football'])[1]");

    // Selector for each sport row (bonus task: all sports with events)
    public static final By footballLiveEventRow = By.xpath("//android.view.View[@content-desc]");

    public static final By firstEventResultBlock = By.xpath(
            "(//android.view.ViewGroup[.//android.widget.TextView[contains(@text,'Match Result')]])[1]");

    public static final By eventBlockTextView = By.className("android.widget.TextView");
}