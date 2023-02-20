package org.example.enums;

public enum Queues {

  FIRST("Первый"),
  LAST("Последний");

  private final String name;

  Queues(String name) {
    this.name = name;
  }

}