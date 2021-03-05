package com.c.demo.asm.classvisitor;

import org.objectweb.asm.*;

import java.io.IOException;
import java.io.InputStream;

/**
 * ClassName: ClassPrinter
 * Description:
 * 实现一个 ClassVisitor 啥也不干, 简单地访问以下类
 *
 * date: 2021/3/5 20:30
 *
 * @author zhy
 * @since JDK 1.8
 */
public class ClassPrinter extends ClassVisitor {

    public ClassPrinter() {
        super(Opcodes.ASM4);
    }

    public ClassPrinter(int i, ClassVisitor classVisitor) {
        super(i, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        System.out.println(name + " extends " + superName + " {");
    }

    @Override
    public void visitSource(String source, String debug) {

    }

    @Override
    public void visitOuterClass(String owner, String name, String desc) {

    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return null;
    }

    @Override
    public void visitAttribute(Attribute attr) {

    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {

    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        System.out.println(" " + desc + " " + name);
        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        System.out.println(" " + name + desc);
        return null;
    }

    @Override
    public void visitEnd() {
        System.out.println("}");
    }


    public static void main(String[] args) throws IOException {
        System.out.println("==========printByClassName==========");
        printByClassName();

        System.out.println();

        System.out.println("==========printByStream==========");
        printByStream();
    }

    private static void printByClassName() throws IOException {
        ClassPrinter cp = new ClassPrinter();
        ClassReader cr = new ClassReader("java.lang.Runnable");
        cr.accept(cp, 0);
    }

    private static void printByStream() throws IOException {
        ClassPrinter cp = new ClassPrinter();
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("java/lang/Runnable.class");
        ClassReader cr = new ClassReader(stream);
        cr.accept(cp, 0);
    }


}
