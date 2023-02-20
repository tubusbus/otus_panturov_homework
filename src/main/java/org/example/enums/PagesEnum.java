package org.example.enums;

public enum PagesEnum {
  MAIN_PAGE("/"),
  LESSONS_PAGE("/lessons");
  private final String url;

  PagesEnum(String url) {
    this.url = url;
  }

  public String getValue() {
    String baseUrl = System.getProperty("webdriver.base.url");
    return baseUrl + this.url;
  }

}
