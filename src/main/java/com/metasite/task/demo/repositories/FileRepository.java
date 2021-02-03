package com.metasite.task.demo.repositories;

import com.metasite.task.demo.entities.TextFile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileRepository extends MongoRepository<TextFile, String> {
}
