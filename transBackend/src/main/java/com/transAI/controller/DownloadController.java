package com.transAI.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/api/download")
public class DownloadController {

    @GetMapping("/hidden_relic")
    public ResponseEntity<Resource> downloadFile() {

        System.out.println("DownloadController.downloadFile");
        // 替换为你的文件路径
        File file = new File("~/Desktop/HL.zip.2b");
        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        System.out.println(file.getName());
        Resource resource = new FileSystemResource(file);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}