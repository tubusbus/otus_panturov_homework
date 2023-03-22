package org.example.driver;

import org.example.driver.brausers.ChromeWebDriver;
import org.example.driver.brausers.FirefoxWebDriver;
import org.example.driver.brausers.OperaWebDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.util.Locale;

public class DriverFactory implements IDriverFactory {

  @Override
  public EventFiringWebDriver getDriver(String browserType) {
    switch (browserType) {
      case "chrome": {
        WebDriver driver = new ChromeWebDriver().newDriver();
        driver.manage().window().maximize();
        return new EventFiringWebDriver(driver);
      }
      case "opera": {
        WebDriver driver = new OperaWebDriver().newDriver();
        driver.manage().window().maximize();
        return new EventFiringWebDriver(driver);
      }
      case "firefox": {
        WebDriver driver = new FirefoxWebDriver().newDriver();
        driver.manage().window().maximize();
        return new EventFiringWebDriver(driver);
      }
      default:
        try {
          throw new Exception(browserType);
        } catch (Exception e) {
          e.printStackTrace();
          return null;
        }
    }
  }
}
