package com.forum.topicos.handler;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()){
            case 404:
            {
                if (methodKey.contains("getAutor")){
                    return new ResponseStatusException(HttpStatus.valueOf(response.status()), response.reason());
                }
                break;
            }
            default:
                return new Exception(response.reason());
        }
        return null;
    }

}
