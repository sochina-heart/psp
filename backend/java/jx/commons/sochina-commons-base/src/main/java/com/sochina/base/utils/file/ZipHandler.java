package com.sochina.base.utils.file;

import cn.hutool.core.date.SystemClock;
import com.alibaba.fastjson2.JSONObject;
import com.sochina.base.constants.Constants;
import com.sochina.base.enums.file.compression.CompressionType;
import com.sochina.base.utils.ThreadPoolUtils;
import org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator;
import org.apache.commons.compress.archivers.zip.UnixStat;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.parallel.InputStreamSupplier;
import org.apache.commons.io.input.NullInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

import java.io.*;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component("zipHandler")
public class ZipHandler implements CompressionHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(ZipHandler.class);
    private static volatile CompressionHandler singletonInstance;
    @Value("${zip.buffer-size:10MB}")
    private DataSize bufferSize;

    public static CompressionHandler getInstance() {
        if (singletonInstance == null) {
            synchronized (ZipHandler.class) {
                if (singletonInstance == null) {
                    singletonInstance = new ZipHandler();
                }
            }
        }
        return singletonInstance;
    }

    @Override
    public void compress(List<String> filePaths, String outName) {
        long startTime = SystemClock.now();
        if (filePaths == null || filePaths.isEmpty()) {
            LOGGER.warn("no files to compression");
            return;
        }
        ThreadPoolExecutor executor = ThreadPoolUtils.getExecutorPool();
        ParallelScatterZipCreator parallelScatterZipCreator = new ParallelScatterZipCreator(executor);
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(outName);
        } catch (FileNotFoundException e) {
            LOGGER.error("file not found", e);
        }
        ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(outputStream);
        zipArchiveOutputStream.setEncoding(Constants.UTF8);
        for (String filePath : filePaths) {
            File file = new File(filePath);
            final InputStreamSupplier inputStreamSupplier = () -> {
                try {
                    FileInputStream fis = new FileInputStream(file);
                    return new BufferedInputStream(fis, (int) bufferSize.toBytes());
                } catch (FileNotFoundException e) {
                    LOGGER.error("file not found", e);
                    return new NullInputStream(0);
                }
            };
            ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file.getName());
            zipArchiveEntry.setMethod(ZipArchiveEntry.DEFLATED);
            zipArchiveEntry.setSize(file.length());
            zipArchiveEntry.setUnixMode(UnixStat.FILE_FLAG | 436);
            parallelScatterZipCreator.addArchiveEntry(zipArchiveEntry, inputStreamSupplier);
        }
        try {
            parallelScatterZipCreator.writeTo(zipArchiveOutputStream);
            zipArchiveOutputStream.close();
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (Exception e) {
            LOGGER.error("stream close occur error", e);
        }
        long endTime = SystemClock.now();
        long consumeTime = (endTime - startTime) / 1000;
        LOGGER.info("ParallelCompressUtil->ParallelCompressUtil-> info:{} -> consume time is : {}", JSONObject.toJSONString(parallelScatterZipCreator.getStatisticsMessage()), consumeTime);
    }

    @Override
    public void decompress(File file, String outPath) {
        byte[] buffer = new byte[(int) bufferSize.toBytes()];
        try
                (FileInputStream fis = new FileInputStream(file);
                 ZipInputStream zis = new ZipInputStream(fis)) {
            ZipEntry entry;
            while (((entry = zis.getNextEntry())) != null) {
                String name = entry.getName();
                File cFile = new File(outPath, name);
                if (entry.isDirectory()) {
                    cFile.mkdirs();
                } else {
                    try (FileOutputStream fos = new FileOutputStream(cFile)) {
                        int length;
                        while ((length = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                }
                zis.closeEntry();
            }
        } catch (IOException e) {
            LOGGER.error("file decompression occur error", e);
        }
    }

    @Override
    public File[] getFilesFromPath(String folderPath) {
        File folder = new File(folderPath);
        return folder.listFiles((dir, name) -> name.toLowerCase().endsWith(CompressionType.ZIP.getExtension()));
    }
}
