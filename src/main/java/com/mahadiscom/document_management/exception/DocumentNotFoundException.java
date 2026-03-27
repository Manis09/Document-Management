package com.mahadiscom.document_management.exception;

public class DocumentNotFoundException extends RuntimeException {
    public DocumentNotFoundException(String msg) {
        super(msg);
    }
}
