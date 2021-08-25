package com.mike.base.constant;

/** created by  wjf  at 2021/8/17 10:01 */
public class Config {

    //BASE_URL
    private String baseUrl;

    public static Config getInstance() {
        return ConfigInternalClassHolder.instance;
    }

    public static class ConfigInternalClassHolder {

        private static final Config instance = new Config();
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
