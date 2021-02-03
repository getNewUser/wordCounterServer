package com.metasite.task.demo.interfaces;

import com.metasite.task.demo.DTOs.WordWithCountDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface IWordsService {
    List<WordWithCountDTO> getCalculationOfEachWord(List<MultipartFile> files) throws ExecutionException, InterruptedException;
    ArrayList<WordWithCountDTO> getUniqueWordCountPairs(List<WordWithCountDTO> wordWithCountDTOS);
    WordWithCountDTO getWordCountPair(List<String> words, String wordToCount);
    List<WordWithCountDTO> getWordsByRegex(List<WordWithCountDTO> pairs, String regex);
    ArrayList<String> getListOfWords(String text);
}
