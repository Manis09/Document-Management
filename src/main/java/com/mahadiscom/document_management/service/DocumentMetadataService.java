package com.mahadiscom.document_management.service;

import com.mahadiscom.document_management.entity.DocumentMetadata;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentMetadataService {

    public DocumentMetadata saveMetadata(MultipartFile file, String userId, Object gridFsId,String fileHash);
}
