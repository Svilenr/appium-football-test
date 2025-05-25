package pages;

import org.openqa.selenium.By;

public class HomePage {
    // All selectors as public static final By fields
    public static final By hamburgerMenuButton = By.xpath("//android.view.ViewGroup/android.widget.TextView[1]");
    public static final By liveButton = By.xpath("(//android.widget.TextView[@text='Live'])[1]");
}
