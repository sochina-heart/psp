package com.sochina.base.utils.file;

import java.io.File;
import java.util.List;

public interface CompressionHandler {
    /**
     * 压缩文件
     *
     * @param filePaths 文件路径列表
     * @param outName   压缩文件名
     */
    public abstract void compress(List<String> filePaths, String outName);

    /**
     * 解压文件
     *
     * @param file    压缩文件
     * @param outPath 压缩文件输出路径
     */
    public abstract void decompress(File file, String outPath);

    /**
     * 获取指定目录下，指定类型的压缩文件
     *
     * @param folderPath 文件夹路径
     * @return
     */
    public abstract File[] getFilesFromPath(String folderPath);
}