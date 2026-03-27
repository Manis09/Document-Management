package com.mahadiscom.document_management.service;

import com.mahadiscom.document_management.dto.DocumentMetadataResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DocumentService {

    DocumentMetadataResponse uploadDocument(MultipartFile file,String userId);

    Resource downloadDocument(String userId, int documentId);

    List<DocumentMetadataResponse> getDocuments(String userId);

    DocumentMetadataResponse getMetadata(int documentId);
}
