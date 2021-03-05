package com.c.demo.asm._02_classvisitor;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

/**
 * @author zhy
 * @time 2021/3/5
 */
public class ClassByteFactory {

    private ClassByteFactory() {

    }

    /**
     * package pkg;
     * public interface Comparable {
     *     int LESS = -1;
     *     int EQUAL = 0;
     *     int GREATER = 1;
     *     int compareTo(Object o);
     * }
     */
    public static byte[] getComparableInterface() {
        ClassWriter cw = new ClassWriter(0);

        // 定义类头
        cw.visit(
                Opcodes.V1_8,   // java 版本 1.8
                Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE,  // public interface
                "pkg/Comparable",   // 以内部形式指定类名称: pkg.Comparable
                null,   // 泛型
                "java/lang/Object", // 内部形式的超类（接口类隐式继承自Object）
                new String[]{} // 扩展接口的数组，由其内部名称指定
        );

        cw.visitField(
                Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, // public final static
                "LESS", // 字段名
                "I",    // 字段的类型，采用类型描述符形式 int 类型
                null,   // 泛型
                -1  // 字段的常量值：必须使用此参数仅适用于真正恒定的字段，即最终的静态字段
        ).visitEnd();

        cw.visitField(
                Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, // public final static
                "EQUAL", // 字段名
                "I",     // 字段的类型，采用类型描述符形式 int 类型
                null, // 泛型
                0   // 字段的常量值：必须使用此参数仅适用于真正恒定的字段，即最终的静态字段
        ).visitEnd();

        cw.visitField(
                Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, // public final static
                "GREATER",  // 字段名
                "I",     // 字段的类型，采用类型描述符形式 int 类型
                null, // 泛型
                1   // 字段的常量值：必须使用此参数仅适用于真正恒定的字段，即最终的静态字段
        ).visitEnd();

        cw.visitMethod(
                Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT,  // public static abstract
                "compareTo",    // 方法名
                "(Ljava/lang/Object;)I",    // 方法描述符：(参数)返回值
                null,   // 泛型
                null    // 由方法抛出的异常数组
        ).visitEnd();

        cw.visitEnd();

        return cw.toByteArray();
    }
}
