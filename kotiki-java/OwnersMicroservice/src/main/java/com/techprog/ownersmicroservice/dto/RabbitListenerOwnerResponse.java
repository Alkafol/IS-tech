package com.techprog.ownersmicroservice.dto;

public class RabbitListenerOwnerResponse {
    private OwnerDto ownerDto;
    private String message;

    public RabbitListenerOwnerResponse(OwnerDto ownerDto, String message) {
        this.ownerDto = ownerDto;
        this.message = message;
    }

    public OwnerDto getOwnerDto() {
        return ownerDto;
    }

    public String getMessage() {
        return message;
    }
}
