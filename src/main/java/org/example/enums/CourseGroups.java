package org.example.enums;

public enum CourseGroups {

  ALL("Все"),
  POPULAR("Популярные курсы"),
  SPECIALISACION("Специализации"),
  RECOMENDATION("Рекомендации для вас");

  private final String name;

  CourseGroups(String name) {
    this.name = name;
  }

}