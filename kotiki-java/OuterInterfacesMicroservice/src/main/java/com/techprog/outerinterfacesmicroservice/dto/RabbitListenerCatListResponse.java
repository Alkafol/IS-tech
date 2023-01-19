package com.techprog.outerinterfacesmicroservice.dto;

import java.util.List;

public class RabbitListenerCatListResponse {
    String message;
    List<CatDto> cats;

    public RabbitListenerCatListResponse(){
    }

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
