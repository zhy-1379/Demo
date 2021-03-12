package com.c.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author zhy
 * @time 2021/3/12
 */
public class LoggerUtils {
    private static final Map<Class<?>, Logger> loggerMap = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(LoggerUtils.class);

    private LoggerUtils() {
    }

    public static Logger getLogger(Class<?> clazz) {
        if (!loggerMap.containsKey(clazz)) {
            try {
                synchronized (loggerMap) {
                    if (!loggerMap.containsKey(clazz)) {
                        loggerMap.put(clazz, LoggerFactory.getLogger(clazz));
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        return loggerMap.getOrDefault(clazz, null);
    }

    public static void info(String message) {
        info(LoggerUtils.class, message);
    }

    public static void info(Class<?> clazz, String message) {
        Objects.requireNonNull(getLogger(clazz)).info(message);
    }

    public static void info(Class<?> clazz, Object message) {
        Objects.requireNonNull(getLogger(clazz)).info(String.valueOf(message));
    }

    public static void debug(Class<?> clazz, String message) {
        Objects.requireNonNull(getLogger(clazz)).debug(message);
    }

    public static void error(Throwable e) {
        error(LoggerUtils.class, e);
    }

    public static void error(Class<?> clazz, String message) {
        Objects.requireNonNull(getLogger(clazz)).error(message);
    }

    public static void error(Class<?> clazz, Throwable e) {
        Objects.requireNonNull(getLogger(clazz)).error(e.getMessage(), e);
    }
}
