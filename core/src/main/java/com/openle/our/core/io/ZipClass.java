package com.openle.our.core.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * http://www.xckey.com/1090.html#crayon-5edf5154f0ac7358452683
 *
 * @author xiaodong
 */
public class ZipClass {

    //  Java原生GZIP压缩类 - 测试成功后删除以下注释原代码
    public static void gzipFile(File srcFile, File desFile) {
        try ( var fis = new FileInputStream(srcFile);  var fos = new FileOutputStream(desFile);  var gzos = new GZIPOutputStream(fos)) {
            fis.transferTo(gzos);
            gzos.flush();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
//    public static void gzipFile(File srcFile, File desFile) {
//        FileInputStream fis;
//        FileOutputStream fos;
//        GZIPOutputStream gzos;
//        final int MAX_BYTE = 1024 * 1000;
//        int len = 0;
//        byte[] b = new byte[MAX_BYTE];
//
//        try {
//            fis = new FileInputStream(srcFile);
//            fos = new FileOutputStream(desFile);
//            gzos = new GZIPOutputStream(fos);
//            while ((len = fis.read(b)) != -1) {
//                gzos.write(b, 0, len);
//            }
//            gzos.flush();
//            gzos.close();
//            fos.close();
//            fis.close();
//        } catch (IOException ex) {
//            System.err.println(ex);
//        }
//    }

    /**
     * zip文件压缩
     *
     * @param inputFile 待压缩文件夹/文件名
     * @param outputFile 生成的压缩包名字
     * @throws java.lang.Exception ...
     */
    public static void ZipCompress(String inputFile, String outputFile) throws Exception {
        //创建缓冲输出流
        try ( //创建zip输出流
                 ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputFile)); //创建缓冲输出流
                  BufferedOutputStream bos = new BufferedOutputStream(out)) {
            File input = new File(inputFile);
            compress(out, bos, input, null);
        }
    }

    //递归压缩 - name 压缩文件名，可以写为null保持默认
    public static void compress(ZipOutputStream out, BufferedOutputStream bos, File input, String name) throws IOException {
        if (name == null) {
            name = input.getName();
        }
        //如果路径为目录（文件夹）
        if (input.isDirectory()) {
            //取出文件夹中的文件（或子文件夹）
            File[] flist = input.listFiles();

            if (flist.length == 0)//如果文件夹为空，则只需在目的地zip文件中写入一个目录进入
            {
                out.putNextEntry(new ZipEntry(name + "/"));
            } else//如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
            {
                for (int i = 0; i < flist.length; i++) {
                    compress(out, bos, flist[i], name + "/" + flist[i].getName());
                }
            }
        } else//如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
        {
            out.putNextEntry(new ZipEntry(name));
            try ( FileInputStream fos = new FileInputStream(input);  BufferedInputStream bis = new BufferedInputStream(fos)) {
                int len = -1;
                //将源文件写入到zip文件中
                byte[] buf = new byte[1024];
                while ((len = bis.read(buf)) != -1) {
                    bos.write(buf, 0, len);
                }
            }
        }
    }

    /**
     * zip解压
     *
     * @param inputFile 待解压文件名
     * @param destDirPath 解压路径
     * @throws java.lang.Exception ...
     */
    public static void ZipUncompress(String inputFile, String destDirPath) throws Exception {
        File srcFile = new File(inputFile);//获取当前压缩文件
        // 判断源文件是否存在
        if (!srcFile.exists()) {
            throw new Exception(srcFile.getPath() + "所指文件不存在");
        }
        //开始解压
        //构建解压输入流
        ZipInputStream zIn = new ZipInputStream(new FileInputStream(srcFile));
        ZipEntry entry;
        File file;
        while ((entry = zIn.getNextEntry()) != null) {
            if (!entry.isDirectory()) {
                file = new File(destDirPath, entry.getName());
                if (!file.exists()) {
                    new File(file.getParent()).mkdirs();//创建此文件的上级目录
                }
                try ( OutputStream out = new FileOutputStream(file);  BufferedOutputStream bos = new BufferedOutputStream(out)) {
                    int len = -1;
                    byte[] buf = new byte[1024];
                    while ((len = zIn.read(buf)) != -1) {
                        bos.write(buf, 0, len);
                    }
                    // 关流顺序，先打开的后关闭
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            ZipCompress("D:\\temp", "D:\\temp.zip");
            ZipUncompress("D:\\temp.zip", "D:\\temp-zip");
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
