package com.metasite.task.demo.services;

import com.metasite.task.demo.DTOs.WordWithCountDTO;
import com.metasite.task.demo.interfaces.IWordsService;
import com.metasite.task.demo.runnables.WordCounterCallable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class WordsService implements IWordsService {

    private final Logger logger = LoggerFactory.getLogger(WordsService.class);

    @Override
    public List<WordWithCountDTO> getCalculationOfEachWord(List<MultipartFile> files) throws ExecutionException, InterruptedException {
        List<Future<List<WordWithCountDTO>>> futureList = getFuturesOfWordCountPairs(files);
        List<List<WordWithCountDTO>> listsOfWordCountPairlists = getListOfWordCountPairListsFromFutures(futureList);
        List<WordWithCountDTO> listsMerged = listsOfWordCountPairlists.stream().flatMap(List::stream).collect(Collectors.toList());
        listsMerged = getUniqueWordCountPairs(listsMerged);
        return listsMerged;
    }

    private List<Future<List<WordWithCountDTO>>> getFuturesOfWordCountPairs(List<MultipartFile> files) {
        logger.info("Starting Executor Service with cached thread pool.");
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Future<List<WordWithCountDTO>>> futureList = new ArrayList<>();
        for(MultipartFile file : files){
            futureList.add(executorService.submit(new WordCounterCallable(file, this)));
        }
        executorService.shutdown();
        logger.info("Executor Service shutting down.");
        return futureList;
    }

    private List<List<WordWithCountDTO>> getListOfWordCountPairListsFromFutures(List<Future<List<WordWithCountDTO>>> futureList) throws ExecutionException, InterruptedException {
        List<List<WordWithCountDTO>> listsOfWordCountPairlists = new ArrayList<>();
        for(Future<List<WordWithCountDTO>> future : futureList){
            listsOfWordCountPairlists.add(future.get());
        }
        return listsOfWordCountPairlists;
    }
    @Override
    public ArrayList<WordWithCountDTO> getUniqueWordCountPairs(List<WordWithCountDTO> wordWithCountDTOS){
        ArrayList<WordWithCountDTO> uniquePairs = new ArrayList<>();
        while(!wordWithCountDTOS.isEmpty()){
            uniquePairs.add(getWordCountFromPairs(wordWithCountDTOS, wordWithCountDTOS.stream().findFirst().get().getWord()));
        }
        return uniquePairs;
    }

    private WordWithCountDTO getWordCountFromPairs(List<WordWithCountDTO> wordWithCountDTOS, String wordToCount){
        int count = 0;
        int index;
        while((index = getIndexOfPair(wordWithCountDTOS, wordToCount)) > -1){
            count += wordWithCountDTOS.get(index).getCount();
            wordWithCountDTOS.remove(index);
        }
        return new WordWithCountDTO(wordToCount,count);
    }

    private int getIndexOfPair(List<WordWithCountDTO> wordWithCountDTOS, String wordToCount) {
        for(WordWithCountDTO iteratingWordWithCountDTO : wordWithCountDTOS){
            if(iteratingWordWithCountDTO.getWord().equals(wordToCount)){
                return wordWithCountDTOS.indexOf(iteratingWordWithCountDTO);
            }
        }
        return -1;
    }

    @Override
    public WordWithCountDTO getWordCountPair(List<String> words, String wordToCount) {
        int count = 0;
        while(getIndexOfString(words,wordToCount) > -1){
            count++;
            words.remove(wordToCount);
        }
        return new WordWithCountDTO(wordToCount,count);
    }

    private int getIndexOfString(List<String> listOfWords, String wordToFind){
        for(String iteratingWord : listOfWords){
            if(iteratingWord.equals(wordToFind)){
                return listOfWords.indexOf(iteratingWord);
            }
        }
        return -1;
    }
    @Override
    public List<WordWithCountDTO> getWordsByRegex(List<WordWithCountDTO> pairs, String regex){
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        return pairs.stream()
                .filter(pair -> pattern.matcher(pair.getWord()).find())
                .collect(Collectors.toList());

    }
    @Override
    public ArrayList<String> getListOfWords(String text) {
        text = text.replaceAll("\r\n", " ");
        return lineOfWordsToListOfWords(text.trim().replaceAll(" +", " "));
    }

    private ArrayList<String> lineOfWordsToListOfWords(String lineOfText){
        String[] arrayOfWords = lineOfText.split(" ");
        return new ArrayList<>(Arrays.asList(arrayOfWords));
    }


}
