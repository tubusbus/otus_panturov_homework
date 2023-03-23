package org.example.enums;

public enum TargetPrice {

  EXPENSIVE("дорогой"),
  CHEAP("дешевый");

  private final String value;

  TargetPrice(String value) {
    this.value = value;
  }

  public static TargetPrice getTargetPrice(String value) throws Exception {
    for (TargetPrice price : values()) {
      if (value.matches(price.value)) {
        return price;
      }
    }
    throw new Exception(String.format("очередь %s не определёна", value));
  }
  
}