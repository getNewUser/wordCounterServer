package com.metasite.task.demo.interfaces;

import com.metasite.task.demo.DTOs.FileIdWithIntervalDTO;
import com.metasite.task.demo.DTOs.WordWithCountDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFileService {
    void checkMultipartFilesValidity(List<MultipartFile> files);
    List<FileIdWithIntervalDTO> uploadSortedFiles(List<WordWithCountDTO> wordWithCountDTOS);
}
