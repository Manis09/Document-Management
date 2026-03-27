package com.mahadiscom.document_management.service;

import com.mahadiscom.document_management.entity.DocumentMetadata;

import java.io.File;

public interface OcrService {

    public void extractTextAsync(File file, DocumentMetadata documentMetadata);
}
