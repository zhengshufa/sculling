package com.sculling.sculling.util;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author hpdata
 * @DATE 2024/1/1215:43
 */
public class QrCodeTool {

    public static void main(String[] args) throws IOException {
        String text = "Scan the QR code";  // 要添加的文字内容
        String content = "https://www.example.com";  // 二维码的内容

        // 生成带文字的二维码
        BufferedImage image = addTextToQrCode(content, text);

        // 保存二维码图片到文件
        File outputFile = new File("qrcode.png");
        ImageIO.write(image, "png", outputFile);
    }

    public static BufferedImage addTextToQrCode(String content, String text) {
        QrConfig config = new QrConfig(300, 300);

        // 生成二维码的 BufferedImage 对象
        BufferedImage qrImage = QrCodeUtil.generate(content, config);

        // 新建一个 BufferedImage 对象，尺寸与二维码相同
        BufferedImage resultImage = new BufferedImage(qrImage.getWidth(), qrImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = resultImage.createGraphics();

        // 在新建的 BufferedImage 上绘制原二维码图像
        graphics.drawImage(qrImage, 0, 0, null);

        // 绘制文字到 BufferedImage 上
        graphics.setFont(new Font("Arial", Font.BOLD, 20));

        FontMetrics fontMetrics = graphics.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(text);
        int centerX = (qrImage.getWidth() - textWidth) / 2;
        graphics.setColor(Color.BLACK);  // 设置文字颜色
        graphics.drawString(text, centerX, qrImage.getHeight() + 20);  // 设置文字位置

        graphics.dispose();

        return resultImage;
    }

}
