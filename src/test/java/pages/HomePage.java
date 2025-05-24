package pages;

import org.openqa.selenium.By;

public class HomePage {
    // All selectors as public static final By fields
    public static final By MENU_BUTTON = By.xpath("//android.view.ViewGroup/android.widget.TextView[1]");
    public static final By LIVE_BUTTON = By.xpath("(//android.widget.TextView[@text='Live'])[1]");
}
