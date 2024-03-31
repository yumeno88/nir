package ru.yumeno.nir.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileService {
    File getFileByUncPath(String uncPath) throws FileNotFoundException;

    String getMimeType(File file) throws IOException;

    String generateFileToken(String path);
}
