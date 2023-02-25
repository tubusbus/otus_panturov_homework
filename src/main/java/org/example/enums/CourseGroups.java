package org.example.enums;

public enum CourseGroups {

  ALL("Все"),
  POPULAR("Популярные курсы"),
  SPECIALISACION("Специализации"),
  RECOMENDATION("Рекомендации для вас");

  private final String value;

  CourseGroups(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }

  public static CourseGroups getGroup(String value) throws Exception {
    for (CourseGroups group : values()) {
      if (value.matches(group.value)) {
        return group;
      }
    }
    throw new Exception(String.format("группа %s не определёна", value));
  }

}