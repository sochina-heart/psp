package com.sochina.base.utils;

import cn.hutool.core.date.SystemClock;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

public class MavenCleanUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MavenCleanUtils.class);
    private static final Pattern LAST_UPDATED_PATTERN = Pattern.compile(".*lastUpdated.*");
    private static final Pattern IN_PROGRESS_PATTERN = Pattern.compile(".*-in-progress");
    private static final Pattern METADATA_XML_PATTERN = Pattern.compile(".*maven-metadata-.*\\.xml.*");
    private static final Pattern RESOLVER_STATUS_PATTERN = Pattern.compile(".*resolver-status\\.properties");
    private static final Pattern REMOTE_REPOSITORIES_PATTERN = Pattern.compile(".*_remote.repositories.*");
    private static final Pattern UN_KNOWN_PATTERN = Pattern.compile("unknown");

    public static void cleanThread(String path) {
        long startTime = SystemClock.now();
        Future<Void> future = ThreadPoolUtils.submit(() -> {
            handleDirectory(new File(path));
            return null;
        });
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Error while waiting for clean task completion: {}", e.getMessage());
        }
        long endTime = SystemClock.now();
        int time = (int) ((endTime - startTime) / 1000);
        LOGGER.info("清理Maven仓库耗费时间-{}s", time);
    }

    private static void handleDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    handleDirectory(file);
                } else {
                    String fileName = file.getName();
                    if (isMatchingPattern(fileName)) {
                        try {
                            FileUtils.forceDelete(file);
                            LOGGER.info("Deleted document: {}", file.getAbsolutePath());
                        } catch (IOException e) {
                            LOGGER.error("Failed to delete document: {} - {}", file.getAbsolutePath(), e.getMessage());
                        }
                    }
                }
            }
        }
        File[] subDirectories = directory.listFiles();
        if (subDirectories != null) {
            for (File subDirectory : subDirectories) {
                if (subDirectory.isDirectory() && isEmptyDirectory(subDirectory)) {
                    try {
                        FileUtils.forceDelete(subDirectory);
                        LOGGER.info("Deleted folder: {}", subDirectory.getAbsolutePath());
                    } catch (IOException e) {
                        LOGGER.error("Failed to delete folder: {} - {}", subDirectory.getAbsolutePath(), e.getMessage());
                    }
                }
            }
        }
    }

    private static boolean isMatchingPattern(String fileName) {
        return LAST_UPDATED_PATTERN.matcher(fileName).matches()
                || IN_PROGRESS_PATTERN.matcher(fileName).matches()
                || METADATA_XML_PATTERN.matcher(fileName).matches()
                || RESOLVER_STATUS_PATTERN.matcher(fileName).matches()
                || REMOTE_REPOSITORIES_PATTERN.matcher(fileName).matches()
                || UN_KNOWN_PATTERN.matcher(fileName).matches();
    }

    private static boolean isEmptyDirectory(File directory) {
        try {
            return FileUtils.isEmptyDirectory(directory);
        } catch (IOException e) {
            LOGGER.error("判断路径 : {} - {}", directory.getAbsolutePath(), e.getMessage());
            return false;
        }
    }
}
