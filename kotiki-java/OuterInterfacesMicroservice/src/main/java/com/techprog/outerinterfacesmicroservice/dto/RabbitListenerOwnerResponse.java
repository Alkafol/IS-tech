package com.techprog.outerinterfacesmicroservice.dto;

public class RabbitListenerOwnerResponse {
    private OwnerDto ownerDto;
    private String message;

    public RabbitListenerOwnerResponse(OwnerDto ownerDto, String message) {
        this.ownerDto = ownerDto;
        this.message = message;
    }

    public RabbitListenerOwnerResponse() {
    }

    public OwnerDto getOwnerDto() {
        return ownerDto;
    }

    public String getMessage() {
        return message;
    }
}
