package com.c.demo.asm._02_classvisitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author zhy
 * @time 2021/3/5
 */
public class AddFieldAdapter extends ClassVisitor {

    private int fieldAcc;
    private String fieldName;
    private String fieldDesc;
    private Object value;
    private boolean isFieldPresent;

    public AddFieldAdapter(ClassVisitor cv, int fieldAcc, String fieldName, String fieldDesc, Object value) {
        super(Opcodes.ASM4, cv);
        this.fieldAcc = fieldAcc;
        this.fieldName = fieldName;
        this.fieldDesc = fieldDesc;
        this.value = value;
    }

    // region getter & setter
    public int getFieldAcc() {
        return fieldAcc;
    }

    public void setFieldAcc(int fieldAcc) {
        this.fieldAcc = fieldAcc;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isFieldPresent() {
        return isFieldPresent;
    }

    public void setFieldPresent(boolean fieldPresent) {
        isFieldPresent = fieldPresent;
    }
    // endregion

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        if (name.equals(fieldName)) {
            isFieldPresent = true;
        }
        return cv.visitField(access, name, desc, signature, value);
    }

    @Override
    public void visitEnd() {
        if (!isFieldPresent) {
            FieldVisitor fv = cv.visitField(fieldAcc, fieldName, fieldDesc, null, value);
            if (fv != null) {
                fv.visitEnd();
            }
        }
        cv.visitEnd();
    }

}