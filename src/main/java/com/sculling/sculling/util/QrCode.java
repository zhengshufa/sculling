package com.sculling.sculling.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import cn.smallbun.screw.core.util.StringUtils;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * @author hpdata
 * @DATE 2024/1/1215:25
 */
public class QrCode {

    /**
     * 图片的宽度
     */
    private static final int IMAGE_WIDTH = 200;
    /**
     * 图片的高度(需按实际内容高度进行调整)
     */
    private static final int IMAGE_HEIGHT = 200;
    /**
     * 二维码的宽度
     */
    private static final int QR_CODE_WIDTH = 180;
    /**
     * 二维码的宽度
     */
    private static final int QR_CODE_HEIGHT = 180;

    private static final String FORMAT_NAME = "JPG";

    /**
     * @param imgLogo logo图片
     * @param title   头部标题
     * @param content 内容
     * @param footer  底部文字
     */
    public static BufferedImage createQrCode(String imgLogo, String content, String footer) {
        // 创建主模板图片
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D main = image.createGraphics();
        // 设置图片的背景色
        main.setColor(Color.white); //白色
        main.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

        // 动态高度
        int height = 0;

        //***************插入二维码图片***********************************************
        Graphics codePic = image.getGraphics();
        BufferedImage codeImg;
        QrConfig config = new QrConfig();
        config.setWidth(QR_CODE_WIDTH);
        config.setHeight(QR_CODE_HEIGHT);
        if (StrUtil.isNotBlank(imgLogo)) {
            config.setImg(imgLogo);
        }
        codeImg = QrCodeUtil.generate(content, config);
        // 绘制二维码
        codePic.drawImage(codeImg, (IMAGE_WIDTH - QR_CODE_WIDTH) / 2, height, QR_CODE_WIDTH, QR_CODE_HEIGHT, null);
        codePic.dispose();
        if(StringUtils.isNotBlank(footer)){
            //**********************底部公司名字*********
            Graphics2D typeLeft = image.createGraphics();
            // 设置字体颜色
            typeLeft.setColor(Color.black);
            // 设置字体
            Font footerFont = new Font("楷体", Font.BOLD, 20);
            typeLeft.setFont(footerFont);
            typeLeft.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            // x开始的位置：（图片宽度-字体大小*字的个数）/2
            FontRenderContext frc = typeLeft.getFontRenderContext();
            Rectangle2D stringBounds = footerFont.getStringBounds(footer,frc);

            int startX = (IMAGE_WIDTH - (int) stringBounds.getWidth() ) /2 ;
            height += QR_CODE_HEIGHT + 10;
            typeLeft.drawString(footer, startX, height);
        }
        return image;
    }

    // 生成图片文件
    public static void createImage(BufferedImage image, String fileLocation) {
        if (image != null) {
            try {
                ImageIO.write(image, "png", new File(fileLocation));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        String content = "汇丰管业-HDPE节流式缠绕增强管-DN300-20210101-A001-0821001-080901000001";
        BufferedImage bufferedImage = createQrCode(null,  content, "123");
        createImage(bufferedImage, "D:/zsf_work/test/test.png");
    }
}
