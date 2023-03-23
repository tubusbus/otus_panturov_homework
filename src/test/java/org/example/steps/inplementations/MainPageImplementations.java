package org.example.steps.inplementations;

import com.google.inject.Inject;
import org.example.componentClasses.Course;
import org.example.componentClasses.Courses;
import org.example.enums.CourseGroups;
import org.example.enums.PagesEnum;
import org.example.enums.Queues;
import org.example.enums.TargetPrice;
import org.example.pages.MainPage;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.util.Set;

import static org.example.enums.Queues.LAST;

public class MainPageImplementations {

  @Inject
  private MainPage page;

  public void openPage(PagesEnum pageName) {
    page.openPage(pageName);
  }

  public void checkPage(PagesEnum pageName) {
    Assertions.assertEquals(page.checkPage(), pageName.getValue());
  }

  public void clickOnButton(String buttonName) {
    page.clickOnButton(buttonName);
  }

  public void clickOnElement(String courseName) {
    page.clickOnElement(getCourseForName(courseName));
  }

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

  public WebElement getCourseForName(String courseName) {
    WebElement element = Courses.getCourse().stream()
            .filter(e -> e.getName().equals(courseName))
            .findFirst()
            .get()
            .getWebElement();
    return element;
  }

  public void printCourseWithDate(LocalDate courseDate) {
    Course course = Courses.getCourse().stream()
            .filter(e -> e.getDate() != null && e.getDate().equals(courseDate))
            .findFirst()
            .get();
    System.out.println(course);
  }

  public void printCourseLatestDate(LocalDate courseDate) {
    Courses.getCourse().forEach(e -> {
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

  public void findCourses(CourseGroups coursesName, boolean withPrices) {
    Courses.setCourse(page.findCourses(coursesName, withPrices));
  }

}