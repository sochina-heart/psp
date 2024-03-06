package com.sochina.base.utils.verification.code;

import cn.hutool.core.util.RandomUtil;
import com.sochina.base.domain.VerificationCode;

import java.awt.*;
import java.security.SecureRandom;

public abstract class IVerificationCodeTool {
    protected SecureRandom random = new SecureRandom();

    public abstract VerificationCode createVerificationCodeImage(int img_img_width, int img_height);

    protected void drawDisturbLineAdd(Graphics g, int x, int y, int img_width, int img_height, Color color) {
        g.setColor(color);
        int x1 = random.nextInt(img_width);
        int y1 = random.nextInt(img_height);
        int x2 = random.nextInt(x);
        int y2 = random.nextInt(y);
        g.drawLine(x1, y1, x1 + x2, y1 + y2);
    }

    protected void drawDisturbLineReduce(Graphics g, int x, int y, int img_width, int img_height, Color color) {
        g.setColor(color);
        int x1 = random.nextInt(img_width);
        int y1 = random.nextInt(img_height);
        int x2 = random.nextInt(x);
        int y2 = random.nextInt(y);
        g.drawLine(x1, y1, x1 - x2, y1 - y2);
    }

    protected Color getRandomColor(int fc, int bc) {
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc - 16);
        int g = fc + random.nextInt(bc - fc - 14);
        int b = fc + random.nextInt(bc - fc - 18);
        return new Color(r, g, b);
    }

    protected void shearX(Graphics g, int img_width, int img_height, Color color) {
        int period = RandomUtil.randomInt(img_width);
        int frames = 1;
        int phase = RandomUtil.randomInt(2);
        for (int i = 0; i < img_height; i++) {
            double d = (double) (period >> 1) * Math.sin((double) i / (double) period + (6.2831853071795862D * (double) phase) / (double) frames);
            g.copyArea(0, i, img_width, 1, (int) d, 0);
            g.setColor(color);
            g.drawLine((int) d, i, 0, i);
            g.drawLine((int) d + img_width, i, img_width, i);
        }
    }

    protected void shearY(Graphics g, int img_width, int img_height, Color color) {
        int period = RandomUtil.randomInt(img_height >> 1);
        int frames = 20;
        int phase = 7;
        for (int i = 0; i < img_width; i++) {
            double d = (double) (period >> 1) * Math.sin((double) i / (double) period + (6.2831853071795862D * (double) phase) / (double) frames);
            g.copyArea(i, 0, 1, img_height, 0, (int) d);
            g.setColor(color);
            g.drawLine(i, (int) d, i, 0);
            g.drawLine(i, (int) d + img_height, i, img_height);
        }
    }
}
