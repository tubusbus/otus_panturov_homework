package org.example.enums;

public enum Months {
  JAN("январ", "01"),
  FEB("феврал", "02"),
  MAR("март", "03"),
  APR("апрел", "04"),
  MAY("ма", "05"),
  JUN("июн", "06"),
  JUL("июл", "07"),
  AUG("август", "08"),
  SEP("сентябр", "09"),
  OCT("октябр", "10"),
  NOV("ноябр", "11"),
  DEC("декабр", "12");

  private final String monthName;
  private final String monthNumber;

  Months(String monthName, String monthNumber) {
    this.monthName = monthName;
    this.monthNumber = monthNumber;
  }

  public static String getMonthNumber(String expectedMounth) throws Exception {
    for (Months mounth : values()) {
      if (expectedMounth.matches(mounth.monthName)) {
        return mounth.monthNumber;
      }
    }
    throw new Exception(String.format("месяц %s не определён", expectedMounth));
  }

  public String getMonthNumber() {
    return this.monthNumber;
  }

  public String getMonthName() {
    return this.monthName;
  }
}
