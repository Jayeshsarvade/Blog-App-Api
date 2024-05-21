package com.blogappapi.useraddress.microservice.useraddressserviceblogappapi.Exception;


public class ApiException extends RuntimeException {

    public ApiException() {
    }

    public ApiException(String message) {
        super(message);
    }
}
