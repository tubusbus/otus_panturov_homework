package org.example.pages;

import com.google.inject.Inject;
import org.example.actions.PageActions;
import org.example.componentClasses.Course;
import org.example.enums.CourseGroups;
import org.example.enums.Months;
import org.example.enums.Queues;
import org.example.utils.GuiceScoped;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

import static org.example.enums.CourseGroups.ALL;
import static org.example.enums.Queues.LAST;

public class MainPage extends PageActions {

  Set<Course> courses;

  private String popCourses = "//div[@class='container container-lessons']//a[contains(@class,'new-item')]/div/div[contains(@class ,'new-item__tags')]/parent::div";
  private String specCourses = "//div[@class='container container-lessons']//a[contains(@class,'new-item')]/div/div[contains(@class ,'bottom_spec')]/parent::div";
  private String recomendationCourses = "//div[@class='lessons'][2]/a/div";
  private String allCourses = "//div[@class='container container-lessons']//a[contains(@class,'new-item')]/div";
  private String coursesNames = "./div[contains(@class,'lessons__new-item-title')]";
  private String coursesDates = ".//div[@class='lessons__new-item-start'] | ./div[contains(@class,'lessons__new-item-bottom_spec')]/div[@class='lessons__new-item-time']";

  @Inject
  public MainPage(GuiceScoped guiceScoped) {
    super(guiceScoped);
  }

  public WebElement getCourseByQueue(String coursesName, String queue) {
    Queues queues;
    CourseGroups group;
    try {
      queues = Queues.getQueue(queue);
      group = CourseGroups.getGroup(coursesName);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return checkRepeatedDates(courses, queues);
  }

  public WebElement getCourseForName(String courseName) {
    getCourses(ALL);
    WebElement element = courses.stream()
            .filter(e -> e.getName().equals(courseName))
            .findFirst()
            .get()
            .getWebElement();
    return element;
  }

  public void printCourseWithDate(LocalDate courseDate) {
    getCourses(ALL);
    Course course = courses.stream()
            .filter(e -> e.getDate() != null && e.getDate().equals(courseDate))
            .findFirst()
            .get();
    System.out.println(course);
  }

  public void printCourseLatestDate(LocalDate courseDate) {
    getCourses(ALL);
    courses.forEach(e -> {
      if (e.getDate() != null && (e.getDate().equals(courseDate) || e.getDate().isAfter(courseDate)))
        System.out.println(e);
    });
  }

  private WebElement checkRepeatedDates(Set<Course> courses, Queues queue) {
    return courses
            .stream()
            .reduce((targetCourse, currentCourse) ->
                    (queue.equals(LAST) ? targetCourse.getDate().isAfter(currentCourse.getDate()) :
                            targetCourse.getDate().isBefore(currentCourse.getDate()))
                            ? targetCourse : currentCourse)
            .stream()
            .findFirst()
            .get()
            .getWebElement();
  }

  private LocalDate dateFormatter(String string) {
    LocalDate now = LocalDate.now();
    String dateString = getMonthNumberByName(string);
    if (dateString.matches("В.+\\d{4} года.+")) {
      dateString = now.getDayOfMonth() + getDateString(dateString, "\\d{6}");
    } else if (dateString.matches(".* \\d{1} .*")) {
      dateString = "0" + getDateString(dateString, "(\\d{3})") + now.getYear();
    } else if (dateString.matches(".*\\d{2}.*")) {
      dateString = getDateString(dateString, "(\\d{4})") + now.getYear();
    } else
      return null;
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

  private void getCourses(CourseGroups coursesName) {
    switch (coursesName) {
      case POPULAR:
        findElements(popCourses);
        break;
      case SPECIALISACION:
        findElements(specCourses);
        break;
      case RECOMENDATION:
        findElements(recomendationCourses);
        break;
      case ALL:
        findElements(allCourses);
        break;
      default:
        throw new RuntimeException("Несоответствующий раздел курсов");
    }
  }

  private void findElements(String xpath) {
    courses = new HashSet<>();
    guiceScoped.driver.findElements(By.xpath(xpath)).forEach(e -> {
      String name = e.findElement(By.xpath(coursesNames)).getText().trim();
      LocalDate date = dateFormatter(e.findElement(By.xpath(coursesDates)).getText());
      courses.add(new Course(e, name, date));
    });

    if (courses.size() == 0) {
      throw new RuntimeException("Элементы в разделе отсутствуют");
    }
  }

}
