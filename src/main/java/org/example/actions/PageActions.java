package org.example.actions;

import io.qameta.allure.Step;
import org.example.enums.PagesEnum;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class PageActions {

  private WebDriver driver;

  public PageActions(WebDriver driver) {
    this.driver = driver;
  }

  @Step("Проверяю что страница загружена")
  public void checkLoadPage() {
    driver.findElement(By.xpath("//img[@class='header3__logo-img']")).isEnabled();
  }

  @Step("Нажимаю на элемент")
  public void clickMouseOnElement(WebElement element) {
    MouseActions actions = new MouseActions(driver);
    assertTrue(element.isDisplayed());
    actions
            .lightingElement(element)
            .moveToElement(element)
            .click(element)
            .perform();
  }

  public void clickOnButton(String buttonName) {
    WebElement element = driver.findElement(By.xpath(String.format("//button[contains(text(),'%s')]", buttonName)));
    assertTrue(element.isEnabled());
    element.click();
  }

  @Step("Открываю страницу {page}")
  public void openPage(PagesEnum page) {
    String url = page.getValue();
    driver.get(url);
  }

  public void driverQuit() {
    driver.quit();
  }
}
