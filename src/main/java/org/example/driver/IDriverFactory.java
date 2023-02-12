package org.example.driver;

import org.openqa.selenium.WebDriver;

public interface IDriverFactory {
  WebDriver getDriver() throws Exception;
}
