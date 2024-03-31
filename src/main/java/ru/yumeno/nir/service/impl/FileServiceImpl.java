package ru.yumeno.nir.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yumeno.nir.security.jwt.JwtTokenProvider;
import ru.yumeno.nir.service.FileService;

import javax.xml.datatype.Duration;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

@Service
public class FileServiceImpl implements FileService {
    @Value("${jwt.file.lifetime}")
    private java.time.Duration jwtLifeTime;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public FileServiceImpl(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public File getFileByUncPath(String uncPath) throws FileNotFoundException {
        String separator = "\\";
        String unc = uncPath.replaceAll(Pattern.quote(separator), "\\\\");
        File file = new File(unc);
        if (!file.exists()) {
            throw new FileNotFoundException("File with unc path: " + unc + " not found");
        }
        return file;
    }

    @Override
    public String getMimeType(File file) throws IOException {
        Path path = file.toPath();
        String mimeType = Files.probeContentType(path);
        if (mimeType == null) {
            return "application/octet-stream";
        }
        return mimeType;
    }

    @Override
    public String generateFileToken(String path) {
        return jwtTokenProvider.generateFileToken(path, jwtLifeTime);
    }
}
