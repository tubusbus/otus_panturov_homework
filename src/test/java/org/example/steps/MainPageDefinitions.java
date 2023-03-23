package org.example.steps;

import com.google.inject.Inject;
import io.cucumber.java.bg.И;
import org.example.enums.CourseGroups;
import org.example.enums.PagesEnum;
import org.example.enums.TargetPrice;
import org.example.steps.inplementations.MainPageImplementations;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MainPageDefinitions {

  @Inject
  private MainPageImplementations steps;

  @И("^Открываю страницу '(.+)'$")
  public void openPage(PagesEnum pageName) {
    steps.openPage(pageName);
  }

  @И("^Открыта страница '(.+)'$")
  public void checkPage(PagesEnum pageName) {
    steps.checkPage(pageName);
  }

  @И("^Нажимаю на кнопку '(.+)'$")
  public void clickOnButton(String buttonName) {
    steps.clickOnButton(buttonName);
  }

  @И("^Нажимаю на курс с названием '(.+)'$")
  public void clickOnElement(String courseName) {
    steps.clickOnElement(courseName);
  }

  @И("^Вывожу в консоль курс стартующий в дату = '(\\d{2}.\\d{2}.\\d{4})'$")
  public void printCourseWithDate(String dateString) {
    LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    steps.printCourseWithDate(date);
  }

  @И("^Вывожу в консоль курсы стартующие после даты = '(\\d{2}.\\d{2}.\\d{4})'$")
  public void printCourseLatestDate(String dateString) {
    LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    steps.printCourseLatestDate(date);
  }

  @И("^Запоминаю курсы в разделе '(Все|Популярные курсы|Специализации|Рекомендации для вас)'$")
  public void findCourses(String coursesName) {
    try {
      steps.findCourses(CourseGroups.getGroup(coursesName), false);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @И("^Запоминаю курсы с их ценами в разделе '(Все|Популярные курсы|Специализации|Рекомендации для вас)'$")
  public void findCoursesWithPrices(String coursesName) {
    try {
      steps.findCourses(CourseGroups.getGroup(coursesName), true);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @И("^Выбираю самый '(дорогой|дешевый)' курс$")
  public void changeCourseByPrice(String targetPrice) {
    try {
      steps.changeCourseByPrice(TargetPrice.getTargetPrice(targetPrice));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}