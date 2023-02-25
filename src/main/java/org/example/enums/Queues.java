package org.example.enums;

public enum Queues {

  FIRST("Первый"),
  LAST("Последний");

  private final String value;

  Queues(String value) {
    this.value = value;
  }

  public static Queues getQueue(String value) throws Exception {
    for (Queues queues : values()) {
      if (value.matches(queues.value)) {
        return queues;
      }
    }
    throw new Exception(String.format("очередь %s не определёна", value));
  }
}