package base;

import org.openqa.selenium.WebElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.net.MalformedURLException;
import java.net.URL;

public class BaseTest {
    protected AndroidDriver driver;

    @BeforeClass
    public void setUp() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("deviceName", "adb-R5CT21BJ71W-LgjyV9._adb-tls-connect._tcp.");
        caps.setCapability("automationName", "UiAutomator2");
        caps.setCapability("appPackage", "app.sport.empire");
        caps.setCapability("appActivity", "app.sport.empire.MainActivity");
        caps.setCapability("noReset", true);
        caps.setCapability("fullReset", false);

        driver = new AndroidDriver(new URL("http://localhost:4723"), caps);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}