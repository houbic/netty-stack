package com.bink.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by chenbinghao on 16/10/11.下午9:23
 */
public class Config {

    static Properties properties = new Properties();

    public static String getValue(String key) {
        return properties.getProperty(key);
    }

    public static Integer getInteger(String key) {
        return Integer.parseInt(getValue(key));
    }

    public static Long getLong(String key) {
        return Long.parseLong(getValue(key));
    }

    static {
        InputStream inputStream = Config.class.getClassLoader().getResourceAsStream("config.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
