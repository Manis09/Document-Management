package com.mahadiscom.document_management.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface GridFsService {

    public Object saveDocInGridFs(MultipartFile file) throws IOException;

    GridFsResource getFile(String gridFsId);

    void deleteFile(String gridId);
}
