package com.metasite.task.demo.DTOs;

import java.util.List;

public class WordWithCountListAndIntervalDTO {
    private List<WordWithCountDTO> wordWithCountDTOS;
    private String intervalName;

    public WordWithCountListAndIntervalDTO(List<WordWithCountDTO> wordWithCountDTOS, String intervalName) {
        this.wordWithCountDTOS = wordWithCountDTOS;
        this.intervalName = intervalName;
    }

    public List<WordWithCountDTO> getWordCountPairs() {
        return wordWithCountDTOS;
    }

    public String getIntervalName() {
        return intervalName;
    }

}
