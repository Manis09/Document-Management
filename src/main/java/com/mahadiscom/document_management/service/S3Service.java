package com.mahadiscom.document_management.service;

import org.springframework.core.io.InputStreamResource;

import java.io.InputStream;

public interface S3Service {
    String uploadFile(String fileName, byte[] file);

    public InputStream downloadFile(String key);
}
