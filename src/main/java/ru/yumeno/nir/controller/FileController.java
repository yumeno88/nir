package ru.yumeno.nir.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yumeno.nir.security.jwt.JwtTokenProvider;
import ru.yumeno.nir.service.FileService;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping(value = "/news")
@Api
@CrossOrigin("*")
@Slf4j
public class FileController {
    private final FileService fileService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public FileController(FileService fileService, JwtTokenProvider jwtTokenProvider) {
        this.fileService = fileService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping(value = "/extract/{token}")
    public ResponseEntity<?> getFile(@PathVariable(value = "token") String token) throws IOException {
        String uncPath = jwtTokenProvider.getUncPathFromToken(token);
        File file = fileService.getFileByUncPath(uncPath);
        FileSystemResource fsr = new FileSystemResource(file);
        String mimeType = fileService.getMimeType(file);
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType(mimeType))
                .body(fsr);
    }
}
