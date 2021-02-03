package com.metasite.task.demo.DTOs;

public class WordWithCountDTO {
    private final String word;
    private final int count;


    public WordWithCountDTO(String word, int count){
        this.word = word;
        this.count = count;
    }

    public String getWord() {
        return word;
    }

    public int getCount() {
        return count;
    }

}
