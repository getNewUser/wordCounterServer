package com.metasite.task.demo.services;

import com.metasite.task.demo.entities.TextFile;
import com.metasite.task.demo.repositories.FileRepository;
import org.apache.commons.io.FileUtils;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class DownloadUploadService {

    private final Logger logger = LoggerFactory.getLogger(FileService.class);
    private final FileRepository fileRepository;

    public DownloadUploadService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public String addFile(byte[] file) {
        TextFile textFile;
        textFile = new TextFile(new Binary(BsonBinarySubType.BINARY, file));
        fileRepository.insert(textFile);
        return textFile.getId();
    }

    public TextFile getTextFile(String id) throws IOException {
        Optional<TextFile> file = fileRepository.findById(id);
        if(file.isPresent()){
            FileUtils.writeByteArrayToFile(new File("C:\\Users\\ITWORK\\IdeaProjects\\test.txt"), file.get().getFile().getData());
            return file.get();
        }else{
            throw new NullPointerException("Object not found in database");
        }
    }
}
