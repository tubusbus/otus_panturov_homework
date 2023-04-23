import org.example.driver.DriverFactory;
import org.example.pages.MainPage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.example.enums.PagesEnum.LESSONS_PAGE;
import static org.example.enums.PagesEnum.MAIN_PAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MainPageTest {
  private MainPage page;
  private WebDriver driver;

  public MainPageTest() {
    driver = new DriverFactory().getDriver();
    this.page = new MainPage(driver);
  }

  @BeforeEach
  public void preface() {
    page.openPage(MAIN_PAGE);
    page.checkLoadPage();
  }

  @ParameterizedTest(name = "{index} - Выбор курса названием {0}")
  @CsvSource({
          "BI-аналитика,/bi-analytics-course/",
          "Специализация iOS Developer,/ios-specialization/"
  })
  public void clickCourseByName(String courseName, String expectedUrl) {
    WebElement element = page.getCourseForName(courseName);
    page.clickMouseOnElement(element);
    String baseUrl = LESSONS_PAGE.getValue();
    String actualUrl = baseUrl + expectedUrl;
    String currentUrl = driver.getCurrentUrl();
    assertEquals(actualUrl, currentUrl);
  }

  @ParameterizedTest(name = "{index} - Выбор курса в категории Популярные курсы где курс самый {0}")
  @ValueSource(strings = {"Первый", "Последний"})
  public void clickPopularCourseByQueueDate(String queue) {
    WebElement element = page.getCourseByQueue("Популярные курсы", queue);
    page.clickMouseOnElement(element);
    boolean checkUrl = false;
    String baseUrl = LESSONS_PAGE.getValue();
    String[] actualUrls = {"/bi-analytics-course/", "/system_analyst/", "/linux-professional/"};
    String currentUrl = driver.getCurrentUrl();
    for (String url : actualUrls) {
      checkUrl = currentUrl.equals(baseUrl + url);
      if (checkUrl)
        break;
    }
    assertTrue(checkUrl);
  }

  @ParameterizedTest(name = "{index} - Выбор курса в категории {0} где курс самый {1}")
  @CsvSource({
          "Все,Последний",
          "Специализации,Последний"
  })
  public void clickCourseByQueueDate(String courseName, String queue) {
    WebElement element = page.getCourseByQueue(courseName, queue);
    page.clickMouseOnElement(element);
    String baseUrl = LESSONS_PAGE.getValue();
    String actualUrl = baseUrl + "/php-specialization/";
    String currentUrl = driver.getCurrentUrl();
    assertEquals(actualUrl, currentUrl);
  }

  @DisplayName("Проверка на отсутствие курсов в разделе Рекомендации для вас")
  @Test
  public void clickRecomindatedCourseByQueueDate() {
    try {
      page.getCourseByQueue("Рекомендации для вас", "Последний");
    } catch (RuntimeException e) {
      assertEquals("Элементы в разделе отсутствуют", e.getMessage());
    }
  }

}
