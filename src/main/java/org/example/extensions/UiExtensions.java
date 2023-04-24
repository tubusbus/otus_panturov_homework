package org.example.extensions;

import io.qameta.allure.Allure;
import org.example.annotations.Driver;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.ByteArrayInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URI;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UiExtensions implements BeforeEachCallback, AfterEachCallback, AfterTestExecutionCallback {

  private RemoteWebDriver driver = null;

  @Override
  public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
    boolean testResult = extensionContext.getExecutionException().isPresent();
    if (testResult) {
      Allure.addAttachment("Failed screenshot", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }
  }

  private Set<Field> getAnnotatedFields(Class<? extends Annotation> annotation, ExtensionContext extensionContext) {
    Set<Field> set = new HashSet<>();
    Class<?> testClass = extensionContext.getTestClass().get();
    while (testClass != null) {
      for (Field field : testClass.getDeclaredFields()) {
        if (field.isAnnotationPresent(annotation)) {
          set.add(field);
        }
      }
      testClass = testClass.getSuperclass();
    }
    return set;
  }

  @Override
  public void afterEach(ExtensionContext extensionContext) {
    if (driver != null) {
      driver.close();
      driver.quit();
    }
  }

  @Override
  public void beforeEach(ExtensionContext extensionContext) {
    getDriver();
    Set<Field> fields = getAnnotatedFields(Driver.class, extensionContext);

    for (Field field : fields) {
      if (field.getType().getName().equals(WebDriver.class.getName())) {
        AccessController.doPrivileged((PrivilegedAction<Void>)
            () -> {
              try {
                field.setAccessible(true);
                field.set(extensionContext.getTestInstance().get(), driver);
              } catch (IllegalAccessException e) {
                throw new Error(String.format("Could not access or set webdriver in field: %s - is this field public?", field), e);
              }
              return null;
            }
        );
      }
    }
  }

  private void getDriver() {
    String browser = System.getProperty("browser");
    String browserVersion = System.getProperty("browser.version");
    String seleniumUrl = String.format("%s/wd/hub", System.getProperty("webdriver.remote.url"));

    Map<String, Object> selenoidOptions = new HashMap<>();
    selenoidOptions.put("enableVNC", true);
    DesiredCapabilities capabilities = new DesiredCapabilities();
    capabilities.setCapability("browserName", browser);
    capabilities.setCapability("browserVersion", browserVersion);
    capabilities.setCapability("enableVideo", false);
    capabilities.setCapability("selenoid:options", selenoidOptions);
    driver = null;
    try {
      driver = new RemoteWebDriver(
              URI.create(seleniumUrl).toURL(), capabilities
      );
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
    driver.manage().window().maximize();
  }

}
