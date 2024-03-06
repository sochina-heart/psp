package com.sochina.test;

import java.io.*;

public class FileTest {

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("D:\\Users\\admin\\Desktop\\12.txt");
        File file2 = new File("D:\\Users\\admin\\Desktop\\123.txt");
        try (FileInputStream fileInputStream = new FileInputStream(file);
             FileOutputStream fileOutputStream = new FileOutputStream(file2)) {
            byte[] bytes = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(bytes)) != -1) {
                String content = new String(bytes, 0, bytesRead);
                System.out.println(content);
                fileOutputStream.write(content.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        /* try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } */
    }
}
