package com.mahadiscom.document_management.utility;

import com.mahadiscom.document_management.exception.FileValidationException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class FileValidator {
    private static final long MAX_FILE_SIZE=10*1024*1024;

    private static final List<String> ALLOWED_TYPES= List.of("application/pdf"
            ,"image/png"
            ,"image/jpeg"
            );

    public static void validate(MultipartFile file){
        if(file.isEmpty()){
            throw new FileValidationException("Empty file not allowed");
        }
        if(file.getSize()>MAX_FILE_SIZE){
            throw new FileValidationException("File size exceed 10MB limit");
        }
        if(!ALLOWED_TYPES.contains(file.getContentType())){
            throw new FileValidationException("Unsupported file type");
        }
    }
}
