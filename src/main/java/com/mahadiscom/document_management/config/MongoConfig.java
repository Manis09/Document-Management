package com.mahadiscom.document_management.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

@Configuration
public class MongoConfig {

    private static final String CONNECTION_STRING =
            "mongodb://localhost:27017";

    private static final String DATABASE_NAME =
            "documents_management";

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(CONNECTION_STRING);
    }

    @Bean
    public MongoDatabaseFactory mongoDbFactory() {
        return new SimpleMongoClientDatabaseFactory(
                mongoClient(), DATABASE_NAME);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDbFactory());
    }

    @Bean
    public GridFsTemplate gridFsTemplate(
            MongoDatabaseFactory factory,
            MappingMongoConverter converter) {

        return new GridFsTemplate(factory, converter);
    }
}
