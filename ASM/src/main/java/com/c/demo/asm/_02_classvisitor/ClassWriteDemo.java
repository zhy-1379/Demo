package com.c.demo.asm._02_classvisitor;

import java.util.Arrays;

/**
 * @author zhy
 * @time 2021/3/5
 *
 * 构造一个类的字节码，并使用自定义的 ClassLoader 加载
 * - 类的定义
 * - 字段的定义
 * - 方法的定义，不包括方法体
 */
public class ClassWriteDemo {

    /**
     * 构造一个类的字节码，并使用自定义的 ClassLoader 加载，不包括方法体
     */
    public static void main(String[] args) {
        byte[] code = ClassByteFactory.getComparableInterface();

        MyClassLoader loader = new MyClassLoader();
        Class<?> comparable = loader.defineClass("pkg.Comparable", code);

        Arrays.stream(comparable.getDeclaredMethods()).forEach(System.out::println);   // NOSONAR
        Arrays.stream(comparable.getDeclaredFields()).forEach(System.out::println);   // NOSONAR
    }

}

