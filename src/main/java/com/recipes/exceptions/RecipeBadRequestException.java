package com.recipes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class RecipeBadRequestException extends RuntimeException{
    public RecipeBadRequestException() {
        super();
    }
}
