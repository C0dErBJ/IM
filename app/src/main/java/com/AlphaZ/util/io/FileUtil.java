package com.AlphaZ.util.io;

import org.apache.commons.codec.binary.Base64;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtil {

    public static String readImgFileToBase64(String imgFileName) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理

        byte[] data = null;

        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(imgFileName);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 对字节数组Base64编码
        return Base64.encodeBase64String(data);// 返回Base64编码过的字节数组字符串
    }

    public static boolean writeBase64ToImgFile(String imgStr, String imgFileName) {// 对字节数组字符串进行Base64解码并生成图片

        if (imgStr == null) // 图像数据为空
            return false;

        try {
            // Base64解码
            byte[] bytes = Base64.decodeBase64(imgStr);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }

            OutputStream out = new FileOutputStream(imgFileName);
            out.write(bytes);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean makeDirectoryExist(String dir) {

        String fileName = dir;
        if (dir.endsWith(File.separator)) {
            fileName += "mktrigger.txt";
        }

        File file = new File(fileName);
        File parent = file.getParentFile();
        if (parent != null) {
            if (parent.exists()) {
                return true;
            } else {
                return parent.mkdirs();
            }
        }

        return false;
    }

    public static boolean fileOrPathExist(String fileName) {

        File file = new File(fileName);
        return file.exists();
    }

    public static String getRealFilePath(String filePath, HttpSession session) {

        boolean isPath = false;
        if (filePath != null && filePath.endsWith("/")) {
            isPath = true;
        }

        if (session != null) {
            filePath = session.getServletContext().getRealPath(filePath);
            if (isPath) filePath += File.separator;
        }

        return filePath;
    }

    public static List<String> readFileByline(String path) {
        try {
            BufferedReader read = new BufferedReader(new FileReader(path));
            List<String> lines = read.lines().collect(Collectors.toList());
            read.close();
            return lines;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

}
