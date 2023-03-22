package org.example.utils;

import io.cucumber.guice.ScenarioScoped;
import org.example.driver.DriverFactory;
import org.openqa.selenium.WebDriver;

@ScenarioScoped
public class GuiceScoped {

  public WebDriver driver = new DriverFactory().getDriver(System.getProperty("browser","chrome"));

}