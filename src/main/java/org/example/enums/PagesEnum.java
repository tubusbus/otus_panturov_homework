package org.example.enums;

public enum PagesEnum {
  MAIN_PAGE("/"),
  FULLSTACK_DEV_PAGE("/lessons/specializacija-fullstack-dev/"),
  SPEC_ANDROID_PAGE("/lessons/spec-android/"),
  LESSONS_PAGE("/lessons");
  private final String url;

  PagesEnum(String url) {
    this.url = url;
  }

  public String getValue() {
    String baseUrl = System.getProperty("webDriver.base.url","https://otus.ru");
    return baseUrl + this.url;
  }

}
