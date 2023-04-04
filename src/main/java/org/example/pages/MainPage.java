package org.example.pages;

import com.google.inject.Inject;
import org.example.actions.PageActions;
import org.example.componentClasses.Course;
import org.example.enums.CourseGroups;
import org.example.enums.Months;
import org.example.enums.PagesEnum;
import org.example.enums.Queues;
import org.example.utils.GuiceScoped;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

public class MainPage extends PageActions {

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

  private void waitPage(String urlElement) {
    for (int i = 0; i < 4; i++) {
      try {
        if (guiceScoped.driver.getCurrentUrl().contains(urlElement))
          break;
      } catch (Exception e) {
        try {
          Thread.sleep(500);
        } catch (InterruptedException ex) {
          throw new RuntimeException(ex);
        }
      }
    }
  }

  private LocalDate dateFormatter(String string) {
    LocalDate now = LocalDate.now();
    String dateString = getMonthNumberByName(string);
    if (dateString.matches("В.+\\d{4} года.+")) {
      dateString = "01" + getDateString(dateString, "\\d{6}");
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

  public Set<Course> findCourses(CourseGroups coursesName, boolean withPrices) {
    switch (coursesName) {
      case POPULAR:
        return findElements(popCourses, withPrices);
      case SPECIALISACION:
        return findElements(specCourses, withPrices);
      case RECOMENDATION:
        return findElements(recomendationCourses, withPrices);
      case ALL:
        return findElements(allCourses, withPrices);
      default:
        throw new RuntimeException("Несоответствующий раздел курсов");
    }
  }

  private Set<Course> findElements(String xpath, boolean withPrices) {
    Set<Course> courses = new HashSet<>();
    int size = guiceScoped.driver.findElements(By.xpath(xpath)).size();
    for (int i = 0; i < size; i++) {
      WebElement element = guiceScoped.driver.findElements(By.xpath(xpath)).get(i);
      String name = element.findElement(By.xpath(coursesNames)).getText().trim();
      LocalDate date = dateFormatter(element.findElement(By.xpath(coursesDates)).getText());
      if (withPrices) {
        super.clickOnElement(element);
        Integer price = getPrice();
        openPage(PagesEnum.MAIN_PAGE);
        courses.add(new Course(element, name, date, price));
        System.out.println(name + "\n" + date + "\n" + price + "\n");
      } else {
        courses.add(new Course(element, name, date));
      }
    }
    if (courses.size() == 0) {
      throw new RuntimeException("Элементы в разделе отсутствуют");
    }
    return courses;
  }

  public Integer getPrice() {
    WebElement element = checkPricePath("//p[contains(.,'Стоимость обучения')]/parent::div/div/div");
    if (element == null) {
      element = checkPricePath("(//div[contains(.,'Стоимость обучения')])[last()]/parent::div/div/nobr");
    }
    if (element == null) {
      return null;
    }
    String price = element.getText();
    return Integer.parseInt(price.replaceAll("[\\D]", ""));
  }

  private WebElement checkPricePath(String xpath) {
    List<WebElement> element = guiceScoped.driver.findElements(By.xpath(xpath));
    if (element.size() != 0) {
      return guiceScoped.driver.findElement(By.xpath(xpath));
    }
    return null;
  }

}