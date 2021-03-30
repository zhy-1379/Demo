package com.c.demo.asm.tool;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author zhy
 * @time 2021/3/30
 */
public class SVUIDAddTool {

    public static void main(String[] args) {
        String jarPath = "E:\\Maven\\Repository\\io\\netty\\netty-all\\4.1.17.Final\\netty-all-4.1.17.Final.jar";

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(jarPath))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    String entryName = entry.getName();
                    if (entryName.endsWith(".class")) {
                        byte[] container = new byte[1024];
                        int len;
                        List<Byte> byteList = new ArrayList<>();
                        while ((len = zis.read(container)) != -1) {
                            for (int i = 0; i < len; i++) {
                                byteList.add(container[i]);
                            }
                        }

                        byte[] oBytes = new byte[byteList.size()];
                        for (int i = 0; i < byteList.size(); i++) {
                            oBytes[i] = byteList.get(i);
                        }

                        ClassReader reader = new ClassReader(oBytes);

                        ClassWriter writer = new ClassWriter(0);
                        ClassVisitor adder = new SVUIDAdder(writer);
                        reader.accept(adder, 0);

                        byte[] nBytes = writer.toByteArray();

                        String targetPath = "out/" + entryName;
                        File file = new File(targetPath);
                        if (file.getParentFile().mkdir()) {
                            String[] split = entryName.split("/");
                            targetPath = "out/" + split[split.length - 1];
                        }
                        try (FileOutputStream os = new FileOutputStream(targetPath)) {
                            os.write(nBytes);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static byte[] addSVUID(ClassReader reader) {
        ClassWriter writer = new ClassWriter(0);
        ClassVisitor adder = new SVUIDAdder(writer);
        reader.accept(adder, 0);

        return writer.toByteArray();
    }


}
