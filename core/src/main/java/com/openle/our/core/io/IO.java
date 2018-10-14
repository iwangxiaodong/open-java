package com.openle.our.core.io;

//import org.apache.commons.io.FilenameUtils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

//Note: readLine方式遇到\r\n字符才返回缓冲数据。
public class IO {

    public static String newLine = System.lineSeparator();//System.getProperty("line.separator");

    // 强制删除文件/文件夹(含不为空的文件夹)
    public static void deleteIfExistsWithNotEmpty(Path dir) throws IOException {
        try {
            Files.deleteIfExists(dir);
        } catch (DirectoryNotEmptyException e) {
            Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return super.postVisitDirectory(dir, exc);
                }
            });
        }
    }

    public static String readText(String path) {
        String result = null;
        File f = new File(path);
        if (f.exists()) {
            try {
                InputStreamReader isr = new InputStreamReader(
                        new FileInputStream(f), "UTF-8");

                BufferedReader br = new BufferedReader(isr);
                String data = null;
                while ((data = br.readLine()) != null) {
                    if (result == null) {
                        result = "";
                    }
                    result += data + newLine;
                }
                isr.close();
                br.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        return result != null ? result.trim() : null;
    }

    public static void writeText(String path, String content) {
        File f = new File(path);
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            OutputStreamWriter write = new OutputStreamWriter(
                    new FileOutputStream(f), "UTF-8");
            BufferedWriter bw = new BufferedWriter(write);
            bw.write(content);
            bw.close();
            write.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        return file.delete();
    }

//    //todo 临时注释，后续研究是否要引入Commons IO
//    public static String getExt(String fileName) {
//        return FilenameUtils.getExtension(fileName);
//    }
    public static String inputStreamToString(InputStream is) {
        if (is != null) {
            System.err.println("inputStreamToString() InputStream is null!");
            return null;
        }

        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader buf = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));
            String line;
            while ((line = buf.readLine()) != null) {
                //sb.append(line);
                sb.append(line).append(newLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
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
}
