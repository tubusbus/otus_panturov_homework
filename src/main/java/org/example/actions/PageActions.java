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

  public void clickOnElement(WebElement element) {
    for (int i = 0; i < 4; i++) {
      try {
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

  public void clickOnButton(String buttonName) {
    WebElement element = guiceScoped.driver.findElement(By.xpath(String.format("//button[contains(text(),'%s')]", buttonName)));
    clickOnElement(element);
  }

  public void openPage(PagesEnum page) {
    String url = page.getValue();
    guiceScoped.driver.get(url);
  }

  public String checkPage() {
    return guiceScoped.driver.getCurrentUrl();
  }

}
