package org.example.learn.redis.embedded.util;

public class ClassUtils {

    public static String getClassName(Object object) {
        if (object == null) {
            return "null";
        }

        return object.getClass().getName();
    }
}
