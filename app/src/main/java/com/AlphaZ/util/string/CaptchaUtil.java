package com.AlphaZ.util.string;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;


/**
 * @author 无名
 * @version 1.0
 * @ClassName: CaptchaUtil
 * @Description: 关于验证码的工具类
 * @date 2016-5-7 上午8:33:08
 */
public final class CaptchaUtil {
    private CaptchaUtil() {
    }

    /*
     * 随机字符字典
     */
    private static final char[] CHARS = {'2', '3', '4', '5', '6', '7', '8',
            '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M',
            'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    /*
     * 随机数
     */
    private static Random random = new Random();

    /*
     * 获取随机字符
     */
    public static String getRandomString(int length) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            buffer.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return buffer.toString();
    }

    /**
     * 获取随机数字
     *
     * @return
     */
    public static String getRandomNum(int length) {
        StringBuffer buffer = new StringBuffer();
        char[] CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        for (int i = 0; i < length; i++) {
            buffer.append(CHAR[random.nextInt(CHAR.length)]);
        }
        return buffer.toString();
    }

    /*
     * 获取随机数颜色
     */
    private static Color getRandomColor() {
        return new Color(random.nextInt(255), random.nextInt(255),
                random.nextInt(255));
    }

    /*
     * 返回某颜色的反色
     */
    private static Color getReverseColor(Color c) {
        return new Color(255 - c.getRed(), 255 - c.getGreen(),
                255 - c.getBlue());
    }


    public static byte[] getCaptchaPic(int width, int height, String captcha) {
        Color color = getRandomColor();
        Color reverse = getReverseColor(color);

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        g.setFont(new Font(Font.MONOSPACED, Font.ITALIC, height));
        g.setColor(color);
        g.fillRect(0, 0, width, height);
        g.setColor(reverse);
        g.drawString(captcha, 0, height - 4);
        for (int i = 0, n = random.nextInt(100); i < n; i++) {
            g.drawRect(random.nextInt(width), random.nextInt(height), 1, 1);
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, "png", bos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }
}