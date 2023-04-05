package enums;

public enum UrlsEnum {

  BASE_URL("https://petstore.swagger.io/v2"),

  USER_PATH("/user");

  private final String url;

  UrlsEnum(String url) {
    this.url = url;
  }

  public String getValue() {
    return this.url;
  }

}