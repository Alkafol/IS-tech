package com.techprog.outerinterfacesmicroservice.dto;

public class RabbitListenerCatResponse {
    private String message;
    private CatDto catDto;

    public RabbitListenerCatResponse(String message, CatDto catDto) {
        this.message = message;
        this.catDto = catDto;
    }

    public RabbitListenerCatResponse(){}

    public String getMessage() {
        return message;
    }

    public CatDto getCatDto() {
        return catDto;
    }
}
