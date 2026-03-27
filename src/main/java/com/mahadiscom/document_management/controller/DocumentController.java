package com.mahadiscom.document_management.controller;

import com.mahadiscom.document_management.dto.DocumentMetadataResponse;
import com.mahadiscom.document_management.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<DocumentMetadataResponse> uploadDocument(@RequestPart("file") MultipartFile file
            , @RequestParam("userId") String userId) {
        DocumentMetadataResponse response = documentService.uploadDocument(file, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/documents")
    public List<DocumentMetadataResponse> getDocuments(@PathVariable("userId") String userId) {
        List<DocumentMetadataResponse> documents = documentService.getDocuments(userId);
        return ResponseEntity.ok(documents).getBody();
    }

    @GetMapping("{documentId}/download")
    public ResponseEntity<Resource> downloadDocument(@RequestParam String userId, @PathVariable int documentId) throws IOException {
        Resource resource = documentService.downloadDocument(userId, documentId);
        DocumentMetadataResponse metadata = documentService.getMetadata(documentId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + metadata.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(metadata.getFileType()))
                .body(new InputStreamResource(resource.getInputStream()));
    }


}
