package pages;

import org.openqa.selenium.By;

public class LivePage {
    // Selector for the Football sport button on Live page
    public static final By FOOTBALL_BUTTON = By.xpath("(//android.widget.TextView[@text='Football'])[1]");

    // Selector for each sport row (bonus task: all sports with events)
    public static final By SPORTS_EVENT_ROW = By.xpath("//android.view.View[@content-desc]");

    public static final By EVENT_BLOCKS = By
            .xpath("//android.view.ViewGroup[.//android.widget.TextView[contains(@text, 'Match Result')]]");
    // If you want you can remove these (now extracted dynamically):
    // public static final By HOME_TEAM_NAME = ...;
    // public static final By AWAY_TEAM_NAME = ...;
    // public static final By HOME_SCORE = ...;
    // public static final By AWAY_SCORE = ...;
    // public static final By MARKET_NAME = ...;
    // public static final By ODDS = ...;
}