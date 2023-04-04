package org.example.steps;

import com.google.inject.Inject;
import io.cucumber.java.bg.И;
import org.example.componentClasses.Course;
import org.example.componentClasses.Courses;
import org.example.enums.CourseGroups;
import org.example.enums.PagesEnum;
import org.example.enums.TargetPrice;
import org.example.pages.MainPage;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MainPageSteps {

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
    WebElement element = Courses.getCourse().stream()
            .filter(e -> e.getName().equals(courseName))
            .findFirst()
            .get()
            .getWebElement();
    page.clickOnElement(element);
  }

  @И("^Вывожу в консоль курс стартующий в дату = '(\\d{2}.\\d{2}.\\d{4})'$")
  public void printCourseWithDate(String dateString) {
    LocalDate courseDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    Course course = Courses.getCourse().stream()
            .filter(e -> e.getDate() != null && e.getDate().equals(courseDate))
            .findFirst()
            .get();
    System.out.println(course);
  }

  @И("^Вывожу в консоль курсы стартующие после даты = '(\\d{2}.\\d{2}.\\d{4})'$")
  public void printCourseLatestDate(String dateString) {
    LocalDate courseDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    Courses.getCourse().forEach(e -> {
      if (e.getDate() != null && (e.getDate().equals(courseDate) || e.getDate().isAfter(courseDate)))
        System.out.println(e);
    });
  }

  @И("^Запоминаю курсы в разделе '(Все|Популярные курсы|Специализации|Рекомендации для вас)'$")
  public void findCourses(String coursesName) {
    try {
      Courses.setCourse(page.findCourses(CourseGroups.getGroup(coursesName), false));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @И("^Запоминаю курсы с их ценами в разделе '(Все|Популярные курсы|Специализации|Рекомендации для вас)'$")
  public void findCoursesWithPrices(String coursesName) {
    try {
      Courses.setCourse(page.findCourses(CourseGroups.getGroup(coursesName), true));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @И("^Выбираю самый '(дорогой|дешевый)' курс$")
  public void changeCourseByPrice(TargetPrice targetPrice) {
    Course course;
    switch (targetPrice) {
      case EXPENSIVE:
        course = Courses.getCourse().stream()
                .filter(c -> c.getPrice() != null)
                .reduce((target, current) -> target.getPrice() > current.getPrice() ? target : current).get();
        System.out.println(course);
        break;
      case CHEAP:
        course = Courses.getCourse().stream()
                .filter(c -> c.getPrice() != null)
                .reduce((target, current) -> target.getPrice() < current.getPrice() ? target : current).get();
        System.out.println(course);
        break;
      default:
        throw new RuntimeException("Непонятная цена");
    }
  }

}