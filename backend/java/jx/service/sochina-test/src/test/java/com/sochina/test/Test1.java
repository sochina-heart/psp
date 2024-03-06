package com.sochina.test;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.SystemClock;
import com.alibaba.fastjson2.JSONObject;
import com.sochina.base.constants.BaseInitResources;
import com.sochina.base.domain.VerificationCode;
import com.sochina.base.utils.HttpClientUtils;
import com.sochina.base.utils.XssUtils;
import com.sochina.base.utils.bean.BeanUtils;
import com.sochina.base.utils.id.uuid.UuidUtils;
import com.sochina.base.utils.verification.code.impl.ChineseArithmeticVerificationCodeTool;
import com.sochina.test.domain.User;
import com.sochina.test.service.ITestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest(classes = SochinaTestApplicaiont.class)
@RunWith(SpringRunner.class)
public class Test1 {
    private static final Logger LOGGER = LoggerFactory.getLogger(Test1.class);
    @Value("${sochina.verification-code.img-width:146}")
    private int img_width;
    @Value("${sochina.verification-code.img-height:30}")
    private int img_height;
    /*private final ApplicationContext applicationContext;
    public Test1(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }*/
    @Resource
    private HttpClientUtils httpClientUtils;
    @Autowired
    private ChineseArithmeticVerificationCodeTool verificationCodeTool;
    @Autowired
    private List<ITestService> list;
    /*@Test
    public void generateZipDemo2() {
        List<String> list = new ArrayList<>();
        list.add("D:\\doc\\down\\program\\bcprov-jdk15on.pdf");
        list.add("D:\\doc\\down\\doc\\DBeaver用户向导中文版.pdf");
        // CompressionHandler zipHandler = SpringUtils.getBean("zipHandler");
        // zipHandler.compress(list, "demo1.zip");
        CompressionHandler zipHandler1 = (CompressionHandler) applicationContext.getBean("zipHandler");
        zipHandler1.compress(list, "demo2.zip");
    }*/
    /*@Test
    public void mavenCleanTest1() {
        String path = "/home/sochina/.m2/repository";
        MavenCleanUtils.cleanThread(path);
    }*/

    /* @Test
    public void test3() {
        CacheUtils instance = CacheUtils.getInstance();
        Object fdf = instance.getCacheObject("fdf");
        System.out.println("----------------");
    } */

    @Test
    public void test2() throws UnsupportedEncodingException {
        byte[] encode = java.util.Base64.getEncoder().encode("fdfdf".getBytes(StandardCharsets.UTF_8));
        byte[] decode = java.util.Base64.getDecoder().decode(encode);
        System.out.println(new String(decode, StandardCharsets.UTF_8));
        // byte[] decode = Base64Utils.decode("hello sochina");
        // System.out.println(new String(decode, StandardCharsets.UTF_8));
        // System.out.println(Base64Utils.encode(decode));
        // LOGGER.info(CompressionType.ZIP.getExtension());
        // LOGGER.info(String.valueOf(CompressionType.ZIP));
    }

    /* @Test
    public void generateZip3() {
        List<String> list = new ArrayList<>();
        list.add("D:\\doc\\down\\program\\bcprov-jdk15on.pdf");
        list.add("D:\\doc\\down\\doc\\DBeaver用户向导中文版.pdf");
        CompressionHandler zipHandler = SpringUtils.getBean(CompressionImplements.ZIP.getExtension());
        zipHandler.compress(list, "demo1.zip");
    } */
    /* @Test
    public void deCompressionDemo1() {
        String filePath = "D:\\study\\java\\alibaba-study\\sochina\\service\\sochina-test\\demo1.zip";
        String zipOutPath = "D:\\study\\java\\alibaba-study\\sochina\\service\\sochina-test";
        CompressionHandler zipHandler = SpringUtil.getBean(CompressionImplements.ZIP.getExtension());
        zipHandler.decompress(new File(filePath), zipOutPath);
    } */
    @Test
    public void getNow() {
        LOGGER.info("" + SystemClock.now());
        LOGGER.info(SystemClock.nowDate());
        LOGGER.info(Base64.encode("fskfjskdfhsdkfjksdfjksdfjksdfjwoeroeriwoerfowefjsodfjkfjskdfjksdfjskdfjskdfjskfjskfdjskdfjskdfjksdfjsk"));
        LOGGER.info(Base64.encode("be827085a89ec43f878b091db14c27d8f0f678f697a0fa3ca6419ed43f94b038108000fa9a078bec29925cfbb209ea8314988003862cd86298cf6bf069966bc198802ba3a3e69b272ce811c31ed92f21"));
    }

    @Test
    public void verificationTest() throws IOException {
        VerificationCode verificationCode = verificationCodeTool.createVerificationCodeImage(img_width, img_height);
        ImageIO.write(verificationCode.getImage(), "png", new File("/home/sochina/1.png"));
    }

    @Test
    public void beanCopyTest() {
        User user = new User();
        user.setId(UuidUtils.fastUUID());
        user.setAge(23);
        user.setAddr("hello sochina");
        user.setName("sochina");
        User user1 = new User();
        BeanUtils.copyBeanProp(user, user1);
        LOGGER.info(String.valueOf(JSONObject.from(user)));
        LOGGER.info(String.valueOf(JSONObject.from(user1)));
        User user2 = new User();
        org.springframework.beans.BeanUtils.copyProperties(user, user2);
        LOGGER.info(String.valueOf(JSONObject.from(user2)));
    }

    @Test
    public void strategyInterfaceTest() {
        String type = "1";
        for (ITestService service : list) {
            if (service.getType().equals(type)) {
                LOGGER.info(service.doTest());
                return;
            }
        }
    }
   /* @Test
    public void test11() {
        String user = TestMap.getUser(5);
        LOGGER.info(user);
    }*/

    /* @Test
    public void singletonCacheUtilTest() {
        SingletonCacheUtils singletonCacheUtils = SingletonCacheUtils.getInstance();
        singletonCacheUtils.setCacheObject("sochina", "sochina)2323232");
        Object sochina = singletonCacheUtils.getCacheObject("sochina");
        LOGGER.info("sochina --- {}", sochina);
    } */

    /* @Test
    public void forTest() {
        // when int or integer type data reaches the maximum value, it will overflow to minimum value.
        // Integer end = Integer.MAX_VALUE, start = end - 5;
        int end = Integer.MAX_VALUE, start = end - 5;
        for (int i = start; i < end; i++) {
            LOGGER.info("end     {}", end);
            LOGGER.info("i       {}", i);
        }
    } */

    @Test
    public void testXss() {
        System.out.println(BaseInitResources.XSS_PATTERN_MAP.size());
        System.out.println(XssUtils.clean("html <img src=\"image.jpg\" onload=\"maliciousJavaScript()\">"));
        // String str = "<ewe>ds!232@@";
        // String a = Optional.ofNullable(str)
        //         .map(s -> s.contains("a") ? "1" : s).get();
        // Stream.of(str)
        //         .map(s -> {
        //             System.out.println(s + "---------------------");
        //             return s.split("");
        //         })
        //         .flatMap(Arrays::stream).collect(Collectors.toList()).forEach(System.out::println);
        // System.out.println(a);
        // System.out.println(xssCheck(str));
    }

    public static String xssCheck(String str) {
        List<String> item = new ArrayList<>();
        item.add("a");
        item.add("fdfdsf");
        item.add("22");
        // item.stream().map(String::length).reduce(0, Integer::sum);
        // item.stream().map(String::length).reduce(0, (acc, value) -> acc + value);
        // return Optional.ofNullable(str)
        //         .filter(StringUtils::isNotBlank)
        //         .map(s -> BaseInitResources.XSS_HALF_MAP.entrySet().stream()
        //                 .reduce(s, (s1, entry) -> s1.replace(entry.getKey(), entry.getValue()), String::concat))
        //         .orElse(EMPTY_STRING);

        return Stream.of(str)
                .map(s -> BaseInitResources.XSS_HALF_MAP.entrySet().stream()
                        .reduce(s, (s1, entry) -> {
                            System.out.println(s1 + "-----------------------1");
                            String replace = s1.replace(entry.getKey(), entry.getValue());
                            System.out.println(replace + "-----------------------2");
                            return replace;
                        }, String::concat)).collect(Collectors.joining());
    }

    @Test
    public void main22() {
        String leftImagePath = "C:\\Users\\sochina\\Downloads\\251258\\00001.jpg";
        String rightImagePath = "C:\\Users\\sochina\\Downloads\\251258\\00003.jpg";
        String targetImagePath = "C:\\Users\\sochina\\Downloads\\00001.jpg";

        try {
            connectImageWidthHorizontal(leftImagePath, rightImagePath, targetImagePath);
        } catch(Exception e) {
            System.out.println("图片拼接失败！");
            e.printStackTrace();
        }
    }

    private static void connectImageWidthHorizontal(String leftImagePath, String rightImagePath, String targetImagePath) throws IOException {
        BufferedImage leftBufImage = ImageIO.read(new File(leftImagePath));
        BufferedImage rightBufImage = ImageIO.read(new File(rightImagePath));

        int connImageWidth = Math.max(leftBufImage.getWidth(), rightBufImage.getWidth());
        int connImageHeight = leftBufImage.getHeight() + rightBufImage.getHeight();

        BufferedImage connImage = new BufferedImage(connImageWidth, connImageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics connGraphics = connImage.getGraphics();
        //第一张图左上角坐标为(0, 0)
        connGraphics.drawImage(leftBufImage, 0, 0, null);
        //第二张图左上角坐标为(leftBufImage.getWidth(), 0),画在第一张图的右边
        connGraphics.drawImage(rightBufImage, 0, leftBufImage.getHeight(), null);

        String targetFileName = targetImagePath.split("\\.")[1];
        ImageIO.write(connImage, targetFileName, new File(targetImagePath));
    }

    @Test
    public void fdfd333() throws IOException {
        File[] files = new File("C:\\Users\\sochina\\Downloads\\\\251258").listFiles();
        int width = 0;
        int height = 0;
        for (File file : files) {
            System.out.println(file.getName());
            BufferedImage image = ImageIO.read(file);
            width = Math.max(width, image.getWidth());
            height += image.getHeight();
        }

        BufferedImage connImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int initHeight = 0;
        for (File file : files) {
            BufferedImage image = ImageIO.read(file);
            Graphics connGraphics = connImage.getGraphics();
            //第一张图左上角坐标为(0, 0)
            connGraphics.drawImage(image, 0, initHeight, null);
            //第二张图左上角坐标为(leftBufImage.getWidth(), 0),画在第一张图的右边
            initHeight += image.getHeight();
        }
        ImageIO.write(connImage, "jpg", new File("C:\\Users\\sochina\\Downloads\\3.jpg"));
    }

    @Test
    public void  fdfdf() throws IOException {
        File dir = new File("C:\\Users\\sochina\\Downloads\\251258");
        File[] imageFiles = dir.listFiles();
        if (imageFiles == null || imageFiles.length == 0) {
            System.out.println("No images found in the directory");
            return;
        }

        final int batchSize = 60;
        List<BufferedImage> batchImages = new ArrayList<>();
        int count = 16;
        for (int i = 0; i < imageFiles.length; i++) {
            BufferedImage image = ImageIO.read(imageFiles[i]);
            batchImages.add(image);

            // 如果已经达到批处理大小或者是最后一张图片，执行合并操作
            if (batchImages.size() == batchSize || i == imageFiles.length - 1) {
                BufferedImage mergedImage = mergeImages(batchImages);
                ImageIO.write(mergedImage, "jpg",new File("C:\\Users\\sochina\\Downloads\\eee\\" + count + ".jpg"));
                count++;
                // 清空列表，准备下一批处理
                System.gc(); // 建议JVM进行垃圾回收
                batchImages.clear();
            }
        }
    }

    private static BufferedImage mergeImages(List<BufferedImage> images) {
        int width = 0;
        int height = 0;
        for (BufferedImage image : images) {
            width = Math.max(width, image.getWidth());
            height += image.getHeight();
        }

        BufferedImage mergedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g2 = mergedImage.getGraphics();

        int currentHeight = 0;
        for (BufferedImage image : images) {
            g2.drawImage(image, 0, currentHeight, null);
            currentHeight += image.getHeight();
        }
        g2.dispose();
        return mergedImage;
    }



    @Test
    public void sfdfdf() throws IOException {
        File dir = new File("C:\\Users\\sochina\\Downloads\\\\251258");
        File[] imageFiles = dir.listFiles();
        if (imageFiles == null || imageFiles.length == 0) {
            System.out.println("No images found in the directory");
            return;
        }

        final int batchSize = 10;
        List<BufferedImage> batchImages = new ArrayList<>();
        for (int i = 0; i < imageFiles.length; i++) {
            BufferedImage image = ImageIO.read(imageFiles[i]);
            batchImages.add(image);

            // 如果已经达到批处理大小或者是最后一张图片，执行合并操作
            if (batchImages.size() == batchSize || i == imageFiles.length - 1) {
                BufferedImage mergedImage = mergeImages2(batchImages);
                ImageIO.write(mergedImage, "jpg", new File("C:\\Users\\sochina\\Downloads\\" + i + ".jpg"));

                // 清空列表，准备下一批处理
                batchImages.clear();
            }
        }
    }

    private static BufferedImage mergeImages2(List<BufferedImage> images) {
        int width = 0;
        int height = 0;
        for (BufferedImage image : images) {
            width = Math.max(width, image.getWidth());
            height += image.getHeight();
        }

        BufferedImage mergedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = mergedImage.createGraphics();

        int currentHeight = 0;
        for (BufferedImage image : images) {
            g2.drawImage(image, 0, currentHeight, null);
            currentHeight += image.getHeight();
        }
        g2.dispose();
        return mergedImage;
    }

    @Test
    public void processImages() throws IOException {
        File dir = new File("C:\\Users\\sochina\\Downloads\\251258");
        File[] imageFiles = dir.listFiles();
        if (imageFiles == null || imageFiles.length == 0) {
            System.out.println("No images found in the directory");
            return;
        }

        final int batchSize = 10;
        List<BufferedImage> batchImages = new ArrayList<>();
        for (int i = 0; i < imageFiles.length; i++) {
            BufferedImage image = ImageIO.read(imageFiles[i]);

            // 可选: 在这里调整图像分辨率以减少内存使用
            // image = resizeImage(image, newWidth, newHeight);

            batchImages.add(image);

            if (batchImages.size() == batchSize || i == imageFiles.length - 1) {
                BufferedImage mergedImage = mergeImages2(batchImages);
                ImageIO.write(mergedImage, "jpg", new File("C:\\Users\\sochina\\Downloads\\" + i + ".jpg"));
                batchImages.clear(); // 清空列表，准备下一批处理

                System.gc(); // 建议JVM进行垃圾回收
            }
        }
    }

    private static BufferedImage mergeImages3(List<BufferedImage> images) {
        int width = 0;
        int height = 0;
        for (BufferedImage image : images) {
            width = Math.max(width, image.getWidth());
            height += image.getHeight();
        }

        BufferedImage mergedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = mergedImage.createGraphics();

        int currentHeight = 0;
        for (BufferedImage image : images) {
            g2.drawImage(image, 0, currentHeight, null);
            currentHeight += image.getHeight();
        }
        g2.dispose(); // 显式释放Graphics对象占用的资源
        return mergedImage;
    }
}