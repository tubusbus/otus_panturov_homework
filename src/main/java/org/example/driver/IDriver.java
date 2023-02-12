package org.example.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public interface IDriver {
  String REMOTE_URL = System.getProperty("webdriver.remote.url");
  boolean HEADLESS = Boolean.valueOf(System.getProperty("webdriver.headless"));

  public WebDriver newDriver();

  default URL getRemoteUrl() {
    try {
      return new URL(REMOTE_URL);
    } catch (MalformedURLException e) {
      return null;
    }
  }

  default void downloadLocalWebDriver(DriverManagerType driverType) throws Exception {

    String browserVersion = System.getProperty("browser.version", "");

    if (!browserVersion.isEmpty()) {
      switch (driverType) {
        case CHROME:
          WebDriverManager.chromedriver().config().setChromeDriverVersion(browserVersion);
          break;
        case OPERA:
          WebDriverManager.operadriver().config().setOperaDriverVersion(browserVersion);
          break;
        case FIREFOX:
          WebDriverManager.chromedriver().config().setFirefoxVersion(browserVersion);
          break;
        default:
          throw new Exception(String.valueOf(driverType));
      }
    }

    WebDriverManager.getInstance(driverType).setup();
  }
}
