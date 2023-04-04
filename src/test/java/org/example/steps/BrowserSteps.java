package org.example.steps;

import com.google.inject.Inject;
import io.cucumber.java.bg.И;
import org.example.driver.DriverFactory;
import org.example.utils.GuiceScoped;

public class BrowserSteps {

  @Inject
  public GuiceScoped guiceScoped;

  @И("^Открываю браузер '(.+)'$")
  public void openBrowser(String browserName) {
    guiceScoped.driver = new DriverFactory().getDriver(browserName);
  }

}