package com.project.HeartConnect.exception;


import com.project.HeartConnect.utils.response.ErrorResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HeartConnectException extends RuntimeException{
    private ErrorResponse errorResponse;

   public HeartConnectException(ErrorResponse errorResponse){
       this.errorResponse = errorResponse;
   }
}
