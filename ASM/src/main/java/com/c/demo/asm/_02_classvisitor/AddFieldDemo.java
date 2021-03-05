package com.c.demo.asm._02_classvisitor;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.util.Arrays;

/**
 * @author zhy
 * @time 2021/3/5
 */
public class AddFieldDemo {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        byte[] classBytes = ClassByteFactory.getComparableInterface();
        ClassWriter cw = new ClassWriter(0);

        // cv forwards all events to cw
        ClassVisitor cv = new AddFieldAdapter(
                cw,
                Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC,
                "serialVersionUID",
                "J",
                1L
        );

        ClassReader cr = new ClassReader(classBytes);
        cr.accept(cv, 0);
        byte[] newClassBytes = cw.toByteArray();

        Class<?> comparable = new MyClassLoader().defineClass("pkg.Comparable", newClassBytes);

        Arrays.stream(comparable.getDeclaredMethods()).forEach(System.out::println);   // NOSONAR
        Arrays.stream(comparable.getDeclaredFields()).forEach(System.out::println);   // NOSONAR

        System.out.println(comparable.getField("serialVersionUID").get(null));  // NOSONAR
    }

}
