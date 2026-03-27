package com.mahadiscom.document_management.dto;

import lombok.Data;

@Data
public class DocumentMetadataResponse {
    private int documentId;
    private String userId;
    private String fileName;
    private String fileType;
    private long fileSize;
}
