package com.techprog.catsmicroservice.dto;

import java.util.List;

public class RabbitListenerCatListResponse {
    private String message;
    private List<CatDto> cats;

    public RabbitListenerCatListResponse(String message, List<CatDto> cats) {
        this.message = message;
        this.cats = cats;
    }

    public String getMessage() {
        return message;
    }

    public List<CatDto> getCats() {
        return cats;
    }
}
