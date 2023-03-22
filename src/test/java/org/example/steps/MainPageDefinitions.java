package org.example.steps;

import com.google.inject.Inject;
import io.cucumber.java.bg.И;
import org.example.enums.PagesEnum;
import org.example.pages.MainPage;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MainPageDefinitions {

  @Inject
  private MainPage page;

  @И("^Открываю страницу '(.+)'$")
  public void openPage(PagesEnum pageName) {
    page.openPage(pageName);
  }

  @И("^Открыта страница '(.+)'$")
  public void checkPage(PagesEnum pageName) {
    Assertions.assertEquals(page.checkPage(), pageName.getValue());
  }

  @И("^Нажимаю на кнопку '(.+)'$")
  public void clickOnButton(String buttonName) {
    page.clickOnButton(buttonName);
  }

  @И("^Нажимаю на курс с названием '(.+)'$")
  public void clickOnElement(String courseName) {
    page.clickOnElement(page.getCourseForName(courseName));
  }

  @И("^Вывожу в консоль курс стартующий в дату = '(\\d{2}.\\d{2}.\\d{4})'$")
  public void printCourseWithDate(String dateString) {
    LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    page.printCourseWithDate(date);
  }

  @И("^Вывожу в консоль курсы стартующие после даты = '(\\d{2}.\\d{2}.\\d{4})'$")
  public void printCourseLatestDate(String dateString) {
    LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    page.printCourseLatestDate(date);
  }

  @И("^Запоминаю цены курсов$")
  public void checkPrices() {
//    page.printCourseLatestDate(date);
  }

}