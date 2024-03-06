package com.sochina.base.utils.file;

import com.sochina.base.enums.file.compression.CompressionType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.unit.DataSize;

import java.io.File;
import java.util.List;

public class RarHandler implements CompressionHandler {
    @Value("${zip.buffer-size:10MB}")
    private DataSize bufferSize;

    @Override
    public void compress(List<String> filePaths, String outName) {
    }

    @Override
    public void decompress(File file, String outPath) {
    }

    @Override
    public File[] getFilesFromPath(String folderPath) {
        File folder = new File(folderPath);
        return folder.listFiles((dir, name) -> name.toLowerCase().endsWith(CompressionType.RAR.getExtension()));
    }
}
