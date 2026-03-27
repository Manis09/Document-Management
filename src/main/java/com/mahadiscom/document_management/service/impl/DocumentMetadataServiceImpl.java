package com.mahadiscom.document_management.service.impl;

import com.mahadiscom.document_management.entity.DocumentMetadata;
import com.mahadiscom.document_management.enums.DocumentType;
import com.mahadiscom.document_management.enums.MigrationStatus;
import com.mahadiscom.document_management.enums.OcrStatus;
import com.mahadiscom.document_management.enums.StorageType;
import com.mahadiscom.document_management.repository.DocumentMetadataRepository;
import com.mahadiscom.document_management.service.DocumentMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DocumentMetadataServiceImpl implements DocumentMetadataService {

    private final DocumentMetadataRepository documentMetadataRepository;

    @Override
    public DocumentMetadata saveMetadata(MultipartFile file,String userId,Object gridFsId,String fileHash) {
        DocumentMetadata documentMetadata=DocumentMetadata.builder()
                .userId(userId)
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .fileName(file.getOriginalFilename())
                .documentType(DocumentType.USER_UPLOADED)
                .gridFsId(gridFsId.toString())
                .extractedText(null)
                .cleanExtractedText(null)
                .OcrStatus(OcrStatus.PENDING)
                .uploadedAt(LocalDateTime.now())
                .fileHash(fileHash)
                .s3Key(null)
                .storageType(StorageType.LOCAL)
                .migrationStatus(MigrationStatus.PENDING)
                .build();

        DocumentMetadata document = documentMetadataRepository.save(documentMetadata);
        return document;
    }
}
