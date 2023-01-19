package com.techprog.catsmicroservice.dto;

public class RabbitListenerCatResponse {
    private String message;
    private CatDto catDto;

    public RabbitListenerCatResponse(String message, CatDto catDto) {
        this.message = message;
        this.catDto = catDto;
    }

    public String getMessage() {
        return message;
    }

    public CatDto getCatDto() {
        return catDto;
    }
}


