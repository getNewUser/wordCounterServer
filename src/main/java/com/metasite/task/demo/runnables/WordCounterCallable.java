package com.metasite.task.demo.runnables;

import com.metasite.task.demo.DTOs.WordWithCountDTO;
import com.metasite.task.demo.interfaces.IWordsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class WordCounterCallable implements Callable<List<WordWithCountDTO>> {

    private final Logger logger = LoggerFactory.getLogger(WordCounterCallable.class);
    private final MultipartFile file;
    private final IWordsService wordsService;

    public WordCounterCallable(MultipartFile file, IWordsService wordsService) {
        logger.info("Starting TaskRunnableService thread.");
        this.file = file;
        this.wordsService = wordsService;
    }


    @Override
    public List<WordWithCountDTO> call() throws IOException {
        ArrayList<String> words = wordsService.getListOfWords(new String(file.getBytes()));
        ArrayList<WordWithCountDTO> wordWithCountDTOS = new ArrayList<>();
        while(!words.isEmpty()){
            WordWithCountDTO wordWIthCountDTO = wordsService.getWordCountPair(words, words.get(0));
            wordWithCountDTOS.add(wordWIthCountDTO);
        }
        wordWithCountDTOS = wordsService.getUniqueWordCountPairs(wordWithCountDTOS);
        return wordWithCountDTOS;
    }
}
