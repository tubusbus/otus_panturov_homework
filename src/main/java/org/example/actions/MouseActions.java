package org.example.actions;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class MouseActions extends Actions {

  WebDriver driver;

  public MouseActions(WebDriver driver) {
    super(driver);
    this.driver = driver;
  }

  @Override
  public Actions click(WebElement element) {
    unLightingElement(element);
    super.click(element);
    return this;
  }


  public Actions lightingElement(WebElement element) {
    ((JavascriptExecutor) driver)
            .executeScript("arguments[0].setAttribute('style','border: 6px solid red;');", element);
    return this;
  }

  public Actions unLightingElement(WebElement element) {
    ((JavascriptExecutor) driver)
            .executeScript("arguments[0].setAttribute('style','');", element);
    return this;
  }
}
