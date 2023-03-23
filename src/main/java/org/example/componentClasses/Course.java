package org.example.componentClasses;

import org.openqa.selenium.WebElement;

import java.time.LocalDate;

public class Course {

  private WebElement webElement;

  private String name;

  private LocalDate date;

  private Integer price;

  public Course(WebElement webElement, String name, LocalDate date, Integer price) {
    this.webElement = webElement;
    this.name = name;
    this.date = date;
    this.price = price;
  }

  public Course(WebElement webElement, String name, LocalDate date) {
    this.webElement = webElement;
    this.name = name;
    this.date = date;
  }

  public WebElement getWebElement() {
    return webElement;
  }

  public String getName() {
    return name;
  }

  public LocalDate getDate() {
    return date;
  }

  public Integer getPrice() {
    return price;
  }

  public void setWebElement(WebElement webElement) {
    this.webElement = webElement;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  @Override
  public String toString() {
    return String.format("Название = '%s'\nCтарт курса = '%s'\nЦена = '%s'\n", name, date, price);
  }

}