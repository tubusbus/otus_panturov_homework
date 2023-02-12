package org.example.enums;

public enum Months {
  JAN("январ(?:ь|е|я)", "01"),
  FEB("феврал(?:ь|е|я)", "02"),
  MAR("март(?:е|а)", "03"),
  APR("апрел(?:ь|е|я)", "04"),
  MAY("ма(?:й|е|я)", "05"),
  JUN("июн(?:ь|е|я)", "06"),
  JUL("июл(?:ь|е|я)", "07"),
  AUG("август(?:е|а)", "08"),
  SEP("сентябр(?:ь|е|я)", "09"),
  OCT("октябр(?:ь|е|я)", "10"),
  NOV("ноябр(?:ь|е|я)", "11"),
  DEC("декабр(?:ь|е|я)", "12");

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
}
