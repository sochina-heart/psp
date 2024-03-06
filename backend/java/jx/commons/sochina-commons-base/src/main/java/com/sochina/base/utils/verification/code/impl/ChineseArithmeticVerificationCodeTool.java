package com.sochina.base.utils.verification.code.impl;

import com.sochina.base.constants.Constants;
import com.sochina.base.domain.VerificationCode;
import com.sochina.base.utils.verification.code.IVerificationCodeTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class ChineseArithmeticVerificationCodeTool extends IVerificationCodeTool {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChineseArithmeticVerificationCodeTool.class);
    private static final int DISTURB_LINE_SIZE = 15;
    private static final String[] CVC_NUMBERS = {"\u96F6", "\u4E00", "\u4E8C", "\u4E09", "\u56DB", "\u4E94", "\u516D", "\u4E03", "\u516B", "\u4E5D", "\u5341", "\u4E58", "\u9664", "\u52A0", "\u51CF"};
    private static final Map<String, Integer> OP_MAP = new HashMap<>();

    static {
        OP_MAP.put(Constants.TAKE, 11);
        OP_MAP.put(Constants.REMOVE, 12);
        OP_MAP.put(Constants.ADD, 13);
        OP_MAP.put(Constants.REDUCE, 14);
    }

    private final Font font = new Font("黑体", Font.BOLD, 18);
    private int xyResult;
    private String randomString;

    @Override
    public VerificationCode createVerificationCodeImage(int img_width, int img_height) {
        BufferedImage image = new BufferedImage(img_width, img_height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, img_width, img_height);
        g.setColor(getRandomColor(200, 250));
        g.drawRect(0, 0, img_width - 2, img_height - 2);
        g.setColor(getRandomColor(200, 255));
        shearX(g, img_width, img_height, getRandomColor(200, 255));
        shearY(g, img_width, img_height, getRandomColor(200, 255));
        for (int i = 0; i < DISTURB_LINE_SIZE; i++) {
            drawDisturbLineAdd(g, 13, 15, img_width, img_height, getRandomColor(200, 255));
            drawDisturbLineReduce(g, 13, 15, img_width, img_height, getRandomColor(200, 255));
        }
        getRandomMathString();
        LOGGER.info("验证码：{}", randomString);
        LOGGER.info("验证码结果：{}", xyResult);
        StringBuffer logsu = new StringBuffer();
        for (int j = 0, k = randomString.length(); j < k; j++) {
            int chid = 0;
            if (j == 1) {
                chid = OP_MAP.get(String.valueOf(randomString.charAt(j)));
            } else {
                chid = Integer.parseInt(String.valueOf(randomString.charAt(j)));
            }
            String ch = CVC_NUMBERS[chid];
            logsu.append(ch);
            drawRandomString((Graphics2D) g, ch, j);
            // drawRandomString((Graphics2D) g, String.valueOf(chid), j);
        }
        drawRandomString((Graphics2D) g, "\u7B49\u4E8E\uFF1F", 3);
        logsu.append("\u7B49\u4E8E \uFF1F");
        LOGGER.info("汉字验证码 : {}", logsu);
        randomString = logsu.toString();
        g.dispose();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setImage(image);
        verificationCode.setCode(String.valueOf(xyResult));
        return verificationCode;
    }

    private void getRandomMathString() {
        int xx = random.nextInt(10);
        int yy = random.nextInt(10);
        StringBuilder suChinese = new StringBuilder();
        int randomOperations = (int) Math.round(Math.random() * 2);
        if (randomOperations == 0) {
            this.xyResult = yy * xx;
            suChinese.append(yy);
            suChinese.append(Constants.TAKE);
            suChinese.append(xx);
        } else if (randomOperations == 1) {
            if (!(xx == 0) && (yy % xx == 0)) {
                this.xyResult = yy / xx;
                suChinese.append(yy);
                suChinese.append(Constants.REMOVE);
                suChinese.append(xx);
            } else {
                this.xyResult = yy + xx;
                suChinese.append(yy);
                suChinese.append(Constants.ADD);
                suChinese.append(xx);
            }
        } else if (randomOperations == 2) {
            this.xyResult = yy - xx;
            suChinese.append(yy);
            suChinese.append(Constants.REDUCE);
            suChinese.append(xx);
        } else {
            this.xyResult = yy + xx;
            suChinese.append(yy);
            suChinese.append(Constants.ADD);
            suChinese.append(xx);
        }
        this.randomString = suChinese.toString();
    }

    private void drawRandomString(Graphics2D g, String randomString, int i) {
        g.setFont(font);
        int rc = random.nextInt(255);
        int gc = random.nextInt(255);
        int bc = random.nextInt(255);
        g.setColor(new Color(rc, gc, bc));
        int x = random.nextInt(3);
        int y = random.nextInt(2);
        g.translate(x, y);
        int degree = new Random().nextInt() % 15;
        g.rotate(degree * Math.PI / 180, 5 + i * 25, 20);
        g.drawString(randomString, 5 + i * 25, 20);
        g.rotate(-degree * Math.PI / 180, 5 + i * 25, 20);
    }
}
