package com.mahadiscom.document_management.service.impl;

import com.mahadiscom.document_management.entity.DocumentMetadata;
import com.mahadiscom.document_management.enums.MigrationStatus;
import com.mahadiscom.document_management.enums.StorageType;
import com.mahadiscom.document_management.repository.DocumentMetadataRepository;
import com.mahadiscom.document_management.service.GridFsService;
import com.mahadiscom.document_management.service.MigrationService;
import com.mahadiscom.document_management.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MigrationServiceImpl implements MigrationService {

    private final DocumentMetadataRepository documentMetadataRepository;
    private final GridFsService gridFsService;
    private final S3Service s3Service;
    Page<DocumentMetadata> documentMetadataPage;
    private final ExecutorService executor= Executors.newFixedThreadPool(5);

    @Override
    public void processOldDocuments() {
        log.info("Schedular is running");
        Pageable pageable= PageRequest.of(0,10);

        LocalDateTime cutoff=LocalDateTime.now().minusDays(30);
        log.info("cut off date:{}  ",cutoff);
        do{
            documentMetadataPage = documentMetadataRepository.findByUploadedAtBeforeAndStorageTypeAndMigrationStatus(cutoff, StorageType.LOCAL, MigrationStatus.PENDING, pageable);
            log.info("Record found : {} " ,documentMetadataPage.getContent().size());
            for(DocumentMetadata doc:documentMetadataPage.getContent()){
               executor.submit(()->processingOldDocument(doc));
            }
            pageable=pageable.next();
        }while (documentMetadataPage.hasNext());
    }


    @Transactional
    public void processingOldDocument(DocumentMetadata documentMetadata){

        try {
            documentMetadata.setMigrationStatus(MigrationStatus.IN_PROGRESS);
            documentMetadataRepository.save(documentMetadata);

            String gridId = documentMetadata.getGridFsId();
            byte[] file = gridFsService.getFile(gridId).getContentAsByteArray();

            String s3Key=s3Service.uploadFile(documentMetadata.getFileName(),file);
            documentMetadata.setS3Key(s3Key);
            documentMetadata.setStorageType(StorageType.S3);
            documentMetadata.setMigrationStatus(MigrationStatus.COMPLETED);
            documentMetadataRepository.save(documentMetadata);

            gridFsService.deleteFile(gridId);

        } catch (Exception e) {
            log.warn("Migration is failed for document id {}",documentMetadata.getDocumentId());
            e.printStackTrace();
            documentMetadata.setMigrationStatus(MigrationStatus.FAILED);
            documentMetadataRepository.save(documentMetadata);
        }
    }
}
