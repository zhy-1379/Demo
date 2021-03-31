package com.c.demo.asm.tool;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.SerialVersionUIDAdder;
import sun.misc.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
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
                        byte[] oBytes = IOUtils.readNBytes(zis, zis.available());

                        ClassReader reader = new ClassReader(oBytes);

                        ClassWriter writer = new ClassWriter(0);
                        ClassVisitor adder = new SVUIDAdder(writer);
                        reader.accept(adder, 0);

                        byte[] nBytes = writer.toByteArray();

                        String targetPath = "out/" + entryName;
                        File file = new File(targetPath);
                        if (file.getParentFile().mkdirs()) {
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

    public static void editJar(String jarPath) {
        try (ByteArrayOutputStream bArrOS = new ByteArrayOutputStream()) {
            try (
                    JarFile jarFile = new JarFile(jarPath);
                    JarOutputStream os = new JarOutputStream(bArrOS)
            ) {
                jarFile.stream().forEach(entry -> {
                    try (InputStream is = jarFile.getInputStream(entry)) {
                        os.putNextEntry(new JarEntry(entry.getName()));
                        String entryName = entry.getName();
                        byte[] oBytes = IOUtils.readNBytes(is, is.available());
                        if (entryName.endsWith(".class")) {
                            ClassReader reader = new ClassReader(oBytes);

                            ClassWriter writer = new ClassWriter(0);
                            // ClassVisitor adder = new SVUIDAdder(writer);
                            ClassVisitor adder = new SerialVersionUIDAdder(writer);
                            reader.accept(adder, 0);

                            oBytes = writer.toByteArray();
                        }
                        os.write(oBytes);
                        os.closeEntry();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            bArrOS.writeTo(new FileOutputStream(jarPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
