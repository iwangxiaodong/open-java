package com.openle.our.core.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IO {

    public static String readText(String path) {

        String r = null;
        try {
            //  or Files.lines(Paths.get("D:\\jd.txt")).forEach(System.out::println);
            //  注意 - Files.readAllLines返回值不包括换行符
            //  jdk11
            r = Files.readString(Paths.get(path), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return r;
    }

    public static void writeText(String path, String content) {
        File f = new File(path);
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            try ( OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");  BufferedWriter bw = new BufferedWriter(write)) {
                bw.write(content);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String inputStreamToString(InputStream is) {
        return inputStreamToString(is, null);
    }

    //  BufferedReader需要处理换行符,而InputStreamReader则不需要.
    public static String inputStreamToString(InputStream is, String charsetName) {
        if (is == null) {
            System.err.println("inputStreamToString(...) InputStream is null!");
            return null;
        }

        StringBuilder temp = new StringBuilder();

        try ( InputStreamReader rd = new InputStreamReader(is,
                charsetName != null ? charsetName : Charset.defaultCharset().name())) {
            int c;
            while ((c = rd.read()) != -1) {
                temp.append((char) c);
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return temp.toString();
    }

    //  保持输入流原始字符集信息
    public static String inputStreamToStringKeepOriginalCharset(InputStream is) {
        byte[] buffer = new byte[2048];
        int readBytes;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((readBytes = is.read(buffer)) > 0) {
                stringBuilder.append(new String(buffer, 0, readBytes));
            }
        } catch (IOException ex) {
            Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String inputStreamToStringByScanner(InputStream is) {
        return inputStreamToString(is, null);    //  or "UTF-8"
    }

    //  效率没InputStreamReader高;  
    //  仅用于极短文本流及控制台输入流System.in
    //  类似按空格分割的split方法。
    public static String inputStreamToStringByScanner(InputStream is, String charsetName) {

        if (is == null) {
            System.err.println("inputStreamToString(...) InputStream is null!");
            return null;
        }

        //  scanner.ioException()可获取其忽略的exception
        Scanner s = charsetName != null ? new Scanner(is, charsetName) : new Scanner(is);
        s.useDelimiter("\\A");
        return s.hasNext() ? s.next() : null;
    }

    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        return file.delete();
    }

    // 强制删除文件/文件夹(含不为空的文件夹)
    public static void deleteFileOrNotEmptyDirectory(Path dir) throws IOException {
        //  Files.deleteIfExists(zipPath);  //  非空目录会报DirectoryNotEmptyException
        Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.deleteIfExists(file);
                return super.visitFile(file, attrs);
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.deleteIfExists(dir);
                return super.postVisitDirectory(dir, exc);
            }
        });
    }

    public static String fileSizeFormat(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }

    //  org.apache.commons.io.FilenameUtils.getExtension(fileName);
    //  com.google.common.io.Files.getFileExtension("some.txt");
    public static String getExt(String fileName) {
        return Arrays.stream(fileName.split("\\.")).reduce((a, b) -> b).orElse(null);
    }
}
