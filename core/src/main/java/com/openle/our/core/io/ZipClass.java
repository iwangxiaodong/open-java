package com.openle.our.core.io;

import com.openle.our.core.os.OS;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipClass {

    private static Field field = null;

    static {
        try {
            field = ZipEntry.class.getDeclaredField("extraAttributes");
        } catch (NoSuchFieldException | SecurityException ex) {
            System.err.println(ex);
        }
        field.setAccessible(true);
    }

    // 注意 - 不会读取ZIP文件内权限信息，若有需要可换用unZip(...)或Apache Commons Compress
    public static void unZipNoReadPermissions(File zipFile, File destDir) throws IOException {
        unZip(zipFile, destDir, false);
    }

    //  Linux下会读取文件权限信息
    //  java --add-opens java.base/java.util.zip=ALL-UNNAMED -jar app.jarls 
    public static void unZip(File zipFile, File destDir, boolean restorePermissions) throws IOException {
        if (!zipFile.exists()) {
            return;
        }
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        if (!destDir.isDirectory()) {
            return;
        }

        ZipFile zf = new ZipFile(zipFile);
        var isWindows = OS.isWindows();
        if (isWindows) {
            System.out.println("Windows OS - unZip Do not execute setPosixFilePermissions!");
        }
        zf.stream().forEachOrdered(ze -> {
            File tf = new File(destDir.getAbsolutePath() + "/" + ze.getName());
            //  目录项处理
            if (ze.isDirectory()) {
                tf.mkdirs();
            } else {
                //  文件项父目录处理
                if (!tf.getParentFile().exists()) {
                    tf.getParentFile().mkdirs();
                }
                //  文件项读写
                //  todo - 后续考虑用上 checkTargetPathForUnZip(tf,ze) 安全方式;
                try ( var is = zf.getInputStream(ze);  var os = new FileOutputStream(tf)) {
                    is.transferTo(os);
                } catch (IOException ex) {
                    System.err.println(ex);
                }

                if (!isWindows) {
                    //  还原Linux权限
                    try { // jvmArgs += ['--add-opens','java.base/java.util.zip=ALL-UNNAMED']
                        var p = IO.permissionsFromMode((int) field.get(ze));
                        //  todo - 只能用于Linux？
                        Files.setPosixFilePermissions(tf.toPath(), p);
                    } catch (IllegalAccessException | IOException ex) {
                        System.err.println(ex);
                    }
                }
            }
        });
    }

    //  避免解压出目标dir之外出现安全问题
    public static File checkTargetPathForUnZip(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    public static void ZipCompress(String inputFileOrDir, String zipOutputFile) throws Exception {
        try ( ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipOutputFile));//
                  BufferedOutputStream bos = new BufferedOutputStream(out)) {
            compress(out, bos, new File(inputFileOrDir), null);
        }
    }

    //  递归压缩 - name 压缩文件名，可以写为null保持默认
    //  out入参为写入*.zip文件，文件写入用out.write(bytes); bos是可选的，仅用来优化缓冲下out的字节写入。
    private static void compress(ZipOutputStream out, BufferedOutputStream bos,
            File input, String entryPath) throws IOException {
        //  根目录名或根文件名取自待压缩file或dir名
        if (entryPath == null) {
            entryPath = input.getName();
        }

        //如果路径为目录（文件夹）
        if (input.isDirectory()) {
            //取出文件夹中的文件（或子文件夹）
            File[] files = input.listFiles();
            if (files.length > 0) {
                //  此处的f是待压缩的file或dir
                for (var f : files) {
                    //  自动创建父目录条目？
                    compress(out, bos, f, entryPath + File.separator + f.getName());
                }
            } else {
                //  空目录应显式保留，非空目录则会由子条目自动创建
                out.putNextEntry(new ZipEntry(entryPath + File.separator));
                out.closeEntry();
            }
        } else { //如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
            out.putNextEntry(new ZipEntry(entryPath));
            try ( FileInputStream fos = new FileInputStream(input);//
                      BufferedInputStream bis = new BufferedInputStream(fos)) {
                bis.transferTo(bos);
            }
            //  todo - 是否需要反射赋值当前文件权限至 ZipEntry.extraAttributes
            out.closeEntry();
        }
    }

    public static void main(String[] args) throws IOException {
        unZipNoReadPermissions(new File("D:\\temp\\x\\JSON-java-master.zip"), new File("D:\\temp\\x\\xx\\"));
        try {
            ZipCompress("D:\\temp", "D:\\temp.zip");
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
