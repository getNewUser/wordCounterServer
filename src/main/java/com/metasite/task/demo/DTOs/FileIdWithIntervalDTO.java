package com.metasite.task.demo.DTOs;

public class FileIdWithIntervalDTO {
    private String id;
    private String intervalName;

    public FileIdWithIntervalDTO() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIntervalName(String intervalName) {
        this.intervalName = intervalName;
    }

    public String getId() {
        return id;
    }

    public String getIntervalName() {
        return intervalName;
    }
}
