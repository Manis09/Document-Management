package com.mahadiscom.document_management.service.impl;

import com.mahadiscom.document_management.service.GridFsService;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class GridFsServiceImpl implements GridFsService {

    private final GridFsTemplate gridFsTemplate;

    /*Storing documents using grid fs in chunks*/
    @Override
    public Object saveDocInGridFs(MultipartFile file) throws IOException {
        ObjectId gridFsId = gridFsTemplate.store(file.getInputStream(),
                file.getOriginalFilename(),
                file.getContentType());
        return gridFsId;
    }

    @Override
    public GridFsResource getFile(String gridFsId) {
        Query query = new Query(Criteria.where("_id").is(new ObjectId(gridFsId)));
        GridFSFile file = gridFsTemplate.findOne(query);
        return gridFsTemplate.getResource(file);
    }

    @Override
    public void deleteFile(String gridId) {
        Query query=new Query(Criteria.where("_id").is(gridId));
        gridFsTemplate.delete(query);
    }
}
