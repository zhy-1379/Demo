package com.c.demo.asm.tool;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.SerialVersionUIDAdder;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author zhy
 * @time 2021/3/30
 */
public class SVUIDAdder extends SerialVersionUIDAdder {

    private boolean isFromSerializable;

    public SVUIDAdder(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }

    protected SVUIDAdder(int api, ClassVisitor cv) {
        super(api, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.isFromSerializable = Arrays.asList(interfaces).contains(Serializable.class.getName());

        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    protected void addSVUID(long svuid) {
        if (this.isFromSerializable) {
            super.addSVUID(svuid);
        }
    }
}
