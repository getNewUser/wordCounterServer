package com.metasite.task.demo.services;

import com.metasite.task.demo.constants.Constants;
import com.metasite.task.demo.DTOs.FileIdWithIntervalDTO;
import com.metasite.task.demo.DTOs.WordWithCountDTO;
import com.metasite.task.demo.DTOs.WordWithCountListAndIntervalDTO;
import com.metasite.task.demo.interfaces.IFileService;
import com.metasite.task.demo.interfaces.IWordsService;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FileService implements IFileService {

    private final Logger logger = LoggerFactory.getLogger(FileService.class);
    private final IWordsService wordsService;
    private final DownloadUploadService downloadUploadService;

    public FileService(IWordsService wordsService, DownloadUploadService downloadUploadService) {
        this.wordsService = wordsService;
        this.downloadUploadService = downloadUploadService;
    }

    @Override
    public void checkMultipartFilesValidity(List<MultipartFile> files) {
        if(Objects.equals(FilenameUtils.getName(files.get(0).getOriginalFilename()), ""))
            throwErrorAndLogWithMessage("No files provided");

        String extension;
        for(MultipartFile file : files){
            extension = FilenameUtils.getExtension(file.getOriginalFilename());
            assert extension != null;
            if(!extension.equals("txt"))
                throwErrorAndLogWithMessage("Illegal file provided '" + extension+ "' - use txt");

        }
    }
    @Override
    public List<FileIdWithIntervalDTO> uploadSortedFiles(List<WordWithCountDTO> wordWithCountDTOS) {
        List<WordWithCountListAndIntervalDTO> fourLists = get4ListsWithInteralNames(wordWithCountDTOS);
        List<FileIdWithIntervalDTO> idsList = new ArrayList<>();
        for(WordWithCountListAndIntervalDTO wordWithCountListAndIntervalDTO : fourLists){
            FileIdWithIntervalDTO fileIdWithInterval = new FileIdWithIntervalDTO();
            fileIdWithInterval.setId(downloadUploadService.addFile(
                    wordCountPairListToString(wordWithCountListAndIntervalDTO.getWordCountPairs()).getBytes(StandardCharsets.UTF_8)));
            fileIdWithInterval.setIntervalName(wordWithCountListAndIntervalDTO.getIntervalName());
            idsList.add(fileIdWithInterval);
        }
        return idsList;
    }
    private List<WordWithCountListAndIntervalDTO> get4ListsWithInteralNames(List<WordWithCountDTO> wordWithCountDTOS){
        List<WordWithCountListAndIntervalDTO> fourLists = new ArrayList<>();
        fourLists.add(new WordWithCountListAndIntervalDTO(wordsService.getWordsByRegex(wordWithCountDTOS, Constants.REGEXATOG),Constants.REGEXATOG));
        fourLists.add(new WordWithCountListAndIntervalDTO(wordsService.getWordsByRegex(wordWithCountDTOS, Constants.REGEXHTON),Constants.REGEXHTON));
        fourLists.add(new WordWithCountListAndIntervalDTO(wordsService.getWordsByRegex(wordWithCountDTOS, Constants.REGEXOTOU),Constants.REGEXOTOU));
        fourLists.add(new WordWithCountListAndIntervalDTO(wordsService.getWordsByRegex(wordWithCountDTOS, Constants.REGEXVTOZ),Constants.REGEXVTOZ));
        fourLists = removeEmptyLists(fourLists);
        return fourLists;
    }

    private List<WordWithCountListAndIntervalDTO> removeEmptyLists(List<WordWithCountListAndIntervalDTO> fourLists){
        fourLists.removeIf(list -> list.getWordCountPairs().size() < 1);
        return fourLists;
    }

    private String wordCountPairListToString(List<WordWithCountDTO> wordWithCountDTOS){
        StringBuilder stringBuilder = new StringBuilder();
        for(WordWithCountDTO pair : wordWithCountDTOS){
            stringBuilder.append(pair.getWord()).append(" - ").append(pair.getCount()).append("\n");
        }
        return stringBuilder.toString();
    }
    public void throwErrorAndLogWithMessage(String message){
        logger.warn(message);
        throw new IllegalArgumentException(message);
    }
}
