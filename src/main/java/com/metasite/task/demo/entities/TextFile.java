package com.metasite.task.demo.entities;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "files")
public class TextFile {
    @Id
    private String id;
    private Binary file;

    public TextFile(Binary file) {
        this.file = file;
    }

    public String getId() {
        return id;
    }

    public Binary getFile() {
        return file;
    }
}
