package com.mahadiscom.document_management.entity;

import com.mahadiscom.document_management.enums.DocumentType;
import com.mahadiscom.document_management.enums.MigrationStatus;
import com.mahadiscom.document_management.enums.OcrStatus;
import com.mahadiscom.document_management.enums.StorageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "doc_id_seq_gen" ,sequenceName = "doc_id_seq" ,allocationSize = 1)
    @Column(name = "document_id")
    private int documentId;

    @Column(name="user_id")
    private String userId;

    @Column(name="file_name")
    private String fileName;

    @Column(name="file_type")
    private String fileType;

    @Column(name="file_size")
    private long fileSize;

    @Column(name="grid_fs_id")
    private String gridFsId;

    @Column(name="uploaded_at")
    private LocalDateTime uploadedAt;

    @Column(columnDefinition = "TEXT")
    private String extractedText;

    @Column(columnDefinition = "TEXT")
    private String cleanExtractedText;

    @Column(name="doc_type")
    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    @Enumerated(EnumType.STRING)
    private OcrStatus OcrStatus;

    @Column(name = "file_hash")
    private String fileHash;

    @Column(name="storage_type")
    @Enumerated(EnumType.STRING)
    private StorageType storageType;

    @Column(name="migration_status")
    @Enumerated(EnumType.STRING)
    private MigrationStatus migrationStatus;

    @Column(name = "s3_key")
    private String s3Key;
}
