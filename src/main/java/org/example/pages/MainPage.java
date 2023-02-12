package org.example.pages;

import org.example.actions.PageActions;
import org.example.enums.Months;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainPage extends PageActions {

  public WebDriver driver;

  private String popCourses = "//div[@class='container container-lessons']//div[@class='lessons__new-item-container']/div[contains(@class,'lessons__new-item-title_with-tags')]/parent::div";
  private String specCourses = "//div[@class='container container-lessons']//div[contains(@class,'lessons__new-item-bottom_spec')]/parent::div";
  private String recomendationCourses = "//div[@class='container container-lessons']//div[@class='lessons__new-item-container']/div[contains(@class,'lessons__new-item-title  lessons__new-item-title_with-bg')]/parent::div";
  private String allCourses = "//div[@class='container container-lessons']//a[contains(@class,'lessons__new-item')]/div";
  private String coursesNames = "./div[contains(@class,'lessons__new-item-title')]";
  private String coursesDates = ".//div[@class='lessons__new-item-start'] | ./div[contains(@class,'lessons__new-item-bottom_spec')]/div[@class='lessons__new-item-time']";

  public MainPage(WebDriver driver) {
    super(driver);
    this.driver = driver;
  }

  public WebElement getCourseByQueue(String coursesName, String queue) {
    return getCourseForQueue(coursesName, queue).findElement(By.xpath("./parent::a"));
  }

  private WebElement getCourseForQueue(String coursesName, String queue) {
    List<WebElement> courses = getCoursesList(coursesName);
    List<LocalDate> dates = getDatesCourses(courses);
    int index = checkRepeatedDates(dates, queue);
    return courses.get(index);
  }

  public WebElement getCourseForName(String courseName) {
    List<WebElement> courses = getCoursesList("Все");
    return getCourseByName(courses, courseName);
  }

  private int checkRepeatedDates(List<LocalDate> dates, String queue) {
    LocalDate minMax = null;
    if (queue.equals("ранний")) {
      minMax = Collections.min(dates);
    } else if (queue.equals("поздний")) {
      minMax = Collections.max(dates);
    }
    int index;
    List<Integer> list = new ArrayList<>();
    for (int i = 0; i < dates.size(); i++) {
      if (dates.get(i).equals(minMax)) {
        list.add(i);
      }
    }
    if (list.size() > 1) {
      index = (int) (Math.random() * list.size());
    } else {
      index = list.get(0);
    }
    return index;
  }

  private List<LocalDate> getDatesCourses(List<WebElement> courses) {
    List<LocalDate> list = new ArrayList<>();
    for (WebElement c : courses) {
      String preText = c.findElement(By.xpath(coursesDates)).getText()
              .trim();
      String[] texts = preText.split("\n", 0);
      String text = Arrays.stream(texts)
              .filter(t -> !t.equals(""))
              .findFirst()
              .get();
      list.add(dateFormatter(text));
    }
    return list;
  }

  private WebElement getCourseByName(List<WebElement> courses, String courseName) {
    for (WebElement c : courses) {
      String actualCourse = c.findElement(By.xpath(coursesNames)).getText().trim();
      if (courseName.equals(actualCourse)) {
        return c.findElement(By.xpath(String.format(".//div[contains(text(), '%s')]", courseName)));
      }
    }
    throw new RuntimeException(String.format("Не найден курс с именем %s", courseName));
  }

  private LocalDate dateFormatter(String string) {
    if (string.matches("С \\d{2}.+")) {
      String[] datePeaces = string
              .replaceAll("С ", "")
              .split("\\s");
      return dateBuilder(datePeaces[0], datePeaces[1], null);
    } else if (string.matches("\\d{2}.+")) {
      String[] datePeaces = string
              .split("\\s");
      return dateBuilder(datePeaces[0], datePeaces[1], null);
    } else if (string.matches("В.+\\d{4} года.+")) {
      String[] datePeaces = string
              .replaceAll(" года", "")
              .replaceAll("В ", "")
              .split("\\s");
      return dateBuilder(null, datePeaces[0], datePeaces[1]);
    }
    throw new RuntimeException("Не удалось преобразовать дату курса");
  }

  private LocalDate dateBuilder(String day, String month, String year) {
    LocalDate now = LocalDate.now();
    if (day==null) {
      day = "01";
    }
    if (month==null) {
      month = now.format(DateTimeFormatter.ofPattern("MM"));
    } else {
      try {
        month = Months.getMonthNumber(month);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    if (year==null) {
      year = String.valueOf(now.getYear());
    }
    return LocalDate.parse(day + month + year, DateTimeFormatter.ofPattern("ddMMyyyy"));
  }

  private List<WebElement> getCoursesList(String coursesName) {
    switch (coursesName) {
      case "Популярные курсы":
        return findElements(popCourses);
      case "Специализации":
        return findElements(specCourses);
      case "Рекомендации для вас":
        return findElements(recomendationCourses);
      case "Все":
        return findElements(allCourses);
      default:
        throw new RuntimeException("Несоответствующий раздел курсов");
    }
  }

  private List<WebElement> findElements(String xpath) {
    List<WebElement> elements = driver.findElements(By.xpath(xpath));
    if (elements.size()==0) {
      throw new RuntimeException("Элементы в разделе отсутствуют");
    }
    return elements;
  }

}
