package org.example.pages;

import io.qameta.allure.Step;
import org.example.actions.PageActions;
import org.example.enums.CourseGroups;
import org.example.enums.Months;
import org.example.enums.Queues;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.example.enums.CourseGroups.ALL;
import static org.example.enums.Queues.LAST;

public class MainPage extends PageActions {

  public WebDriver driver;

  private String popCourses = "//div[@class='container container-lessons']//a[contains(@class,'new-item')]/div/div[contains(@class ,'new-item__tags')]/parent::div";
  private String specCourses = "//div[@class='container container-lessons']//a[contains(@class,'new-item')]/div/div[contains(@class ,'bottom_spec')]/parent::div";
  private String recomendationCourses = "//div[@class='lessons'][2]/a/div";
  private String allCourses = "//div[@class='container container-lessons']//a[contains(@class,'new-item')]/div";
  private String coursesNames = "./div[contains(@class,'lessons__new-item-title')]";
  private String coursesDates = ".//div[@class='lessons__new-item-start'] | ./div[contains(@class,'lessons__new-item-bottom_spec')]/div[@class='lessons__new-item-time']";

  public MainPage(WebDriver driver) {
    super(driver);
    this.driver = driver;
  }

  @Step("В кадекогории {coursesName} нахожу {queue} курс")
  public WebElement getCourseByQueue(String coursesName, String queue) {
    Queues queues;
    CourseGroups group;
    try {
      queues = Queues.getQueue(queue);
      group = CourseGroups.getGroup(coursesName);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    Set<WebElement> courses = getCoursesSet(group);
    return checkRepeatedDates(courses, queues);
  }

  private WebElement checkRepeatedDates(Set<WebElement> courses, Queues queue) {
    return queue.equals(LAST) ? checkMaxRepeatedDates(courses) : checkMinRepeatedDates(courses);
  }

  private WebElement checkMaxRepeatedDates(Set<WebElement> courses) {
    return courses
            .stream()
            .reduce((maxDate, curDate) -> getDateFromElement(maxDate).isAfter(getDateFromElement(curDate)) ? maxDate : curDate)
            .stream()
            .findFirst()
            .get();
  }

  private WebElement checkMinRepeatedDates(Set<WebElement> courses) {
    return courses
            .stream()
            .reduce((maxDate, curDate) -> getDateFromElement(maxDate).isBefore(getDateFromElement(curDate)) ? maxDate : curDate)
            .stream()
            .findFirst()
            .get();
  }

  private LocalDate getDateFromElement(WebElement course) {
    String date = course.findElement(By.xpath(coursesDates)).getText();
    return dateFormatter(date);
  }

  @Step ("Нахожу курс с названием {courseName}")
  public WebElement getCourseForName(String courseName) {
    Set<WebElement> courses = getCoursesSet(ALL);
    return getCourseByName(courses, courseName);
  }

  private WebElement getCourseByName(Set<WebElement> courses, String courseName) {
    for (WebElement c : courses) {
      String actualCourse = c.findElement(By.xpath(coursesNames)).getText().trim();
      if (courseName.equals(actualCourse)) {
        return c.findElement(By.xpath(String.format(".//div[contains(text(), '%s')]", courseName)));
      }
    }
    throw new RuntimeException(String.format("Не найден курс с именем %s", courseName));
  }

  private LocalDate dateFormatter(String string) {
    LocalDate now = LocalDate.now();
    String dateString = getMonthNumberByName(string);
    if (dateString.matches("В.+\\d{4} года.+")) {
      dateString = now.getDayOfMonth() + getDateString(dateString, "\\d{6}");
    } else if (dateString.matches(".*\\d{2}.*")) {
      dateString = getDateString(dateString, "(\\d{4})") + now.getYear();
    }
    return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("ddMMyyyy"));
  }

  private String getMonthNumberByName(String name) {
    for (Months month : Months.values()) {
      try {
        name = name.replaceAll(month.getMonthName(), month.getMonthNumber());
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    return name;
  }

  private String getDateString(String string, String regex) {
    string = string.replaceAll("[\\D]", "");
    Pattern pattern = Pattern.compile(regex);
    return pattern.matcher(string)
            .results()
            .map(s -> s.group())
            .findFirst()
            .get();
  }

  private Set<WebElement> getCoursesSet(CourseGroups coursesName) {
    switch (coursesName) {
      case POPULAR:
        return findElements(popCourses);
      case SPECIALISACION:
        return findElements(specCourses);
      case RECOMENDATION:
        return findElements(recomendationCourses);
      case ALL:
        return findElements(allCourses);
      default:
        throw new RuntimeException("Несоответствующий раздел курсов");
    }
  }

  private Set<WebElement> findElements(String xpath) {
    Set<WebElement> elements = driver.findElements(By.xpath(xpath)).stream()
            .collect(Collectors.toSet());
    if (elements.size() == 0) {
      throw new RuntimeException("Элементы в разделе отсутствуют");
    }
    return elements;
  }

}
