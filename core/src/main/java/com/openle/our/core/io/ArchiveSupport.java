package com.openle.our.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

/**
 *
 * @author xiaodong
 */
public class ArchiveSupport {

    //  Java原生GZIP压缩类 - 测试成功后删除以下注释原代码
    public static void gzipSingleFile(File srcFile, File desFile) {
        try ( var fis = new FileInputStream(srcFile);  var fos = new FileOutputStream(desFile);  var gzos = new GZIPOutputStream(fos)) {
            fis.transferTo(gzos);
            gzos.flush();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}
