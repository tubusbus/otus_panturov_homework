package org.example.actions;

import com.google.inject.Inject;
import org.example.enums.PagesEnum;
import org.example.utils.GuiceScoped;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class PageActions {

  protected GuiceScoped guiceScoped;

  @Inject
  public PageActions(GuiceScoped guiceScoped) {
    this.guiceScoped = guiceScoped;
  }

  public void checkLoadPage() {
    guiceScoped.driver.findElement(By.xpath("//img[@class='header3__logo-img']")).isEnabled();
  }

  public void clickOnElement(WebElement element) {
    element.isDisplayed();
    element.click();
  }

  public void clickMouseOnElement(WebElement element) {
    MouseActions actions = new MouseActions(guiceScoped.driver);
    assertTrue(element.isDisplayed());
    actions
            .lightingElement(element)
            .moveToElement(element)
            .click(element)
            .perform();
  }

  public void clickOnButton(String buttonName) {
    for (int i = 0; i < 4; i++) {
      try {
        WebElement element = guiceScoped.driver.findElement(By.xpath(String.format("//button[contains(text(),'%s')]", buttonName)));
        assertTrue(element.isDisplayed());
        element.click();
        break;
      } catch (Exception e) {
        try {
          Thread.sleep(200);
        } catch (InterruptedException ex) {
          throw new RuntimeException(ex);
        }
      }
    }
  }

  public void openPage(PagesEnum page) {
    String url = page.getValue();
    guiceScoped.driver.get(url);
  }

  public String checkPage() {
    return guiceScoped.driver.getCurrentUrl();
  }

  public void driverQuit() {
    guiceScoped.driver.quit();
  }
}
