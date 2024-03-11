package com.project.HeartConnect.utils.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private Boolean success;
    private String message;

    public ErrorResponse(final Boolean success, final  String message){
        this.success = success;
        this.message = message;
    }
}
