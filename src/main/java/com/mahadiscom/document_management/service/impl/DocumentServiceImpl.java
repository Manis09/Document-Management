package com.mahadiscom.document_management.service.impl;

import com.mahadiscom.document_management.dto.DocumentMetadataResponse;
import com.mahadiscom.document_management.entity.DocumentMetadata;
import com.mahadiscom.document_management.enums.DocumentType;
import com.mahadiscom.document_management.enums.StorageType;
import com.mahadiscom.document_management.exception.DocumentNotFoundException;
import com.mahadiscom.document_management.exception.DocumentProcessingException;
import com.mahadiscom.document_management.exception.UnauthorizedException;
import com.mahadiscom.document_management.repository.DocumentMetadataRepository;
import com.mahadiscom.document_management.service.*;
import com.mahadiscom.document_management.utility.FileValidator;
import com.mahadiscom.document_management.utility.HashUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    private final DocumentMetadataRepository documentMetadataRepository;
    private final GridFsService gridFsService;
    private final OcrService ocrService;
    private final DocumentMetadataService documentMetadataService;
    private final ModelMapper modelMapper;
    private final S3Service s3Service;

    public DocumentMetadataResponse uploadDocument(MultipartFile file, String userId) {

        FileValidator.validate(file);

        String fileHashValue = HashUtil.generateHah(file);

        DocumentMetadata existingDocument = documentMetadataRepository.findByFileHashAndUserId(fileHashValue, userId);

        if (existingDocument != null) {
            throw new DocumentProcessingException("Duplicate not allowed");
            // return modelMapper.map(existingDocument,DocumentMetadataResponse.class);
        }

        Object gridFsId = null;
        try {
            gridFsId = gridFsService.saveDocInGridFs(file);
            DocumentMetadata saveDocument = documentMetadataService.saveMetadata(file, userId, gridFsId, fileHashValue);

            if (saveDocument.getDocumentType() == DocumentType.USER_UPLOADED) {
                File tempFile = null;
                tempFile = File.createTempFile("upload-", file.getOriginalFilename());
                file.transferTo(tempFile);
                ocrService.extractTextAsync(tempFile, saveDocument);
            }
            return modelMapper.map(saveDocument, DocumentMetadataResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Resource downloadDocument(String userId, int documentId) {
        DocumentMetadata metadata = documentMetadataRepository.findById(documentId).
                orElseThrow(()->new DocumentNotFoundException("Document Not Found Exception"));

        if(!metadata.getUserId().equals(userId)){
            throw new UnauthorizedException("Access Denied : You do not own this file");
        }

        if(metadata.getStorageType()== StorageType.LOCAL) {

            GridFsResource file = gridFsService.getFile(metadata.getGridFsId());
            if (file == null) {
                throw new RuntimeException("File Not Found in GridFs");
            }
            return file;
        } else if (metadata.getStorageType()==StorageType.S3) {

            InputStream inputStream = s3Service.downloadFile(metadata.getS3Key());
            return new InputStreamResource(inputStream);
        }else{
         throw new DocumentNotFoundException("Unknown Storage type");
        }
    }

    @Override
    public List<DocumentMetadataResponse> getDocuments(String userId) {
        List<DocumentMetadata> allByUserId = documentMetadataRepository.findAllByUserId(userId);
        return allByUserId.stream().map(doc -> modelMapper.map(doc, DocumentMetadataResponse.class)).toList();
    }

    @Override
    public DocumentMetadataResponse getMetadata(int documentId) {
        DocumentMetadata metadata = documentMetadataRepository.findByDocumentId(documentId);
        return modelMapper.map(metadata, DocumentMetadataResponse.class);
    }




}
