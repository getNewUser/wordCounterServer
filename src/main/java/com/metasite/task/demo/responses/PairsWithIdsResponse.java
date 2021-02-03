package com.metasite.task.demo.responses;

import com.metasite.task.demo.DTOs.FileIdWithIntervalDTO;
import com.metasite.task.demo.DTOs.WordWithCountDTO;

import java.util.List;

public class PairsWithIdsResponse {

    private final List<WordWithCountDTO> pairs;
    private final List<FileIdWithIntervalDTO> fileIdsWithIntervals;

    public PairsWithIdsResponse(List<WordWithCountDTO> pairs, List<FileIdWithIntervalDTO> fileIdsWithIntervals) {
        this.pairs = pairs;
        this.fileIdsWithIntervals = fileIdsWithIntervals;
    }

    public List<FileIdWithIntervalDTO> getFileIds() {
        return fileIdsWithIntervals;
    }

    public List<WordWithCountDTO> getPairs() {
        return pairs;
    }
}
