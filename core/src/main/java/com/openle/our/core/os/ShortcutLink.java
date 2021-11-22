package com.openle.our.core.os;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * https://blog.csdn.net/weixin_43217817/article/details/104821787
 *
 * 更完备方式 - implementation 'com.github.vatbub:mslinks:1.0.6.2'
 *
 * @author 贺驰宇
 */
public class ShortcutLink {

    public static void main(String[] args) {
        String fileName = "C:\\Program Files\\"; // exe文件所在父目录
        var hashMap = new HashMap<String, String>();
        hashMap.put("LAN-IM.lnk", "LAN IM\\局域网聊天.exe");
        //hashMap.put("x.lnk", "xx\\xxx.exe");
        var shortcut = new ShortcutLink(fileName, hashMap);
        shortcut.start(ShortcutLink.startup);
    }

    private String prefixFile;
    private HashMap<String, String> pathAndName;
    /**
     * 开机启动目录
     */
    public final static String startup = System.getProperty("user.home")
            + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\";
    /**
     * 文件头，固定字段
     */
    private byte[] headFile = {0x4c, 0x00, 0x00, 0x00,
        0x01, 0x14, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00,
        (byte) 0xc0, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x46
    };
    /**
     * 文件头属性
     */
    private byte[] fileAttributes = {(byte) 0x93, 0x00, 0x08, 0x00,//可选文件属性
        0x20, 0x00, 0x00, 0x00,//目标文件属性
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,//文件创建时间
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,//文件修改时间
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,//文件最后一次访问时间
        0x00, 0x00, 0x00, 0x00,//文件长度
        0x00, 0x00, 0x00, 0x00,//自定义图标个数
        0x01, 0x00, 0x00, 0x00,//打开时窗口状态
        0x00, 0x00, 0x00, 0x00,//热键
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00//未知
    };
    /**
     * 固定字段1
     */
    byte[] fixedValueOne = {
        (byte) 0x83, 0x00, 0x14, 0x00,
        0x1F, 0x50, (byte) 0xE0, 0x4F,
        (byte) 0xD0, 0x20, (byte) 0xEA,
        0x3A, 0x69, 0x10, (byte) 0xA2,
        (byte) 0xD8, 0x08, 0x00, 0x2B,
        0x30, 0x30, (byte) 0x9D, 0x19, 0x00, 0x2f
    };
    /**
     * 固定字段2
     */
    byte[] fixedValueTwo = {
        0x3A, 0x5C, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x54, 0x00, 0x32, 0x00, 0x04,
        0x00, 0x00, 0x00, 0x67, 0x50, (byte) 0x91, 0x3C, 0x20, 0x00
    };

    /**
     * @param prefixFile 需要生成的快捷方式的文件前缀路径
     * @param pathAndName 文件和生成快捷方式的对应关系，k为文件位置，v为生成快捷方式位置
     */
    public ShortcutLink(String prefixFile, HashMap<String, String> pathAndName) {
        this.prefixFile = prefixFile;
        this.pathAndName = pathAndName;
    }

    /**
     * @param path 生成快捷方式前缀路径
     */
    public void start(String path) {
        for (String k : pathAndName.keySet()) {
            String v = pathAndName.get(k);
            start0(path + k, prefixFile + v);
        }
    }

    /**
     * 生成快捷方式
     *
     * @param start 完整的文件路径
     * @param target 完整的快捷方式路径
     */
    public void start0(String start, String target) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(createDirectory(start));
            fos.write(headFile);
            fos.write(fileAttributes);
            fos.write(fixedValueOne);
            fos.write((target.toCharArray()[0] + "").getBytes());
            fos.write(fixedValueTwo);
            fos.write(target.substring(3).getBytes("gbk"));
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 解决父路径问题
     */
    public static File createDirectory(String file) {
        File f = new File(file);
        //获取父路径
        File fileParent = f.getParentFile();
        //如果文件夹不存在
        if (fileParent != null && !fileParent.exists()) {
            //创建文件夹
            fileParent.mkdirs();
        }
        return f;
    }

}
