package com.mike.base.utils;

import com.google.gson.Gson;

/** created by  wjf  at 2021/6/29 19:35 */
public class GsonUtil {

    private static Gson create() {
        return GsonHolder.gson;
    }

    private static class GsonHolder {

        private static Gson gson = new Gson();
    }

    public static String toJson(Object src) {
        return create().toJson(src);
    }

    public static <T> T fromJson(String s, Class<T> classOfT) {
        return create().fromJson(s, classOfT);
    }
}
