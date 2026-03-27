package com.mahadiscom.document_management.repository;

import com.mahadiscom.document_management.dto.DocumentMetadataResponse;
import com.mahadiscom.document_management.entity.DocumentMetadata;
import com.mahadiscom.document_management.enums.MigrationStatus;
import com.mahadiscom.document_management.enums.StorageType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentMetadataRepository extends JpaRepository<DocumentMetadata,Integer> {

    DocumentMetadata findByFileHashAndUserId(String hash, String userId);

    DocumentMetadata findByDocumentId(int documentId);

    List<DocumentMetadata> findAllByUserId(String userId);

    Page<DocumentMetadata> findByUploadedAtBeforeAndStorageTypeAndMigrationStatus(LocalDateTime localDateTime
            , StorageType storageType, MigrationStatus migrationStatus, Pageable pageable);
}
