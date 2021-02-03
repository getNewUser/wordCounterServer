package com.metasite.task.demo.controllers;

import com.metasite.task.demo.DTOs.FileIdWithIntervalDTO;
import com.metasite.task.demo.entities.TextFile;
import com.metasite.task.demo.DTOs.WordWithCountDTO;
import com.metasite.task.demo.interfaces.IFileService;
import com.metasite.task.demo.interfaces.IWordsService;
import com.metasite.task.demo.responses.PairsWithIdsResponse;
import com.metasite.task.demo.services.DownloadUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@CrossOrigin(origins = "https://word-counter-client.herokuapp.com/")
public class FileController {

    private final Logger logger = LoggerFactory.getLogger(FileController.class);
    private final IWordsService wordsService;
    private final IFileService fileService;
    private final DownloadUploadService downloadUploadService;


    public FileController(IWordsService wordsService, IFileService fileService, DownloadUploadService downloadUploadService) {
        this.wordsService = wordsService;
        this.fileService = fileService;
        this.downloadUploadService = downloadUploadService;
    }


    @PostMapping(value = "/file")
    public ResponseEntity<Object> getListOfWordCountPairsFromFiles(@RequestParam("file[]") List<MultipartFile> files) throws ExecutionException, InterruptedException {
        logger.info("Inside /file POST end point");
        try{
            fileService.checkMultipartFilesValidity(files);
        }catch(IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        List<WordWithCountDTO> pairs = wordsService.getCalculationOfEachWord(files);
        List<FileIdWithIntervalDTO> fileIdsWithIntervals = fileService.uploadSortedFiles(pairs);
        PairsWithIdsResponse response = new PairsWithIdsResponse(pairs,fileIdsWithIntervals);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/file")
    public ResponseEntity<Object> getFileWithIds(@RequestParam String id) {
        logger.info("Inside /file GET end point");

        TextFile file;
        try{
            file = downloadUploadService.getTextFile(id);
        }catch(IllegalArgumentException | IOException | NullPointerException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(file, HttpStatus.OK);
    }
}
