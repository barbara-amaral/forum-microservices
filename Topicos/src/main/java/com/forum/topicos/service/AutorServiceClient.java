package com.forum.topicos.service;

import com.forum.topicos.model.AutorModel;
import com.forum.topicos.service.impl.TopicosServiceImpl;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuarios-ms")
public interface AutorServiceClient {

    Logger log = LoggerFactory.getLogger(TopicosServiceImpl.class);

    @GetMapping("/usuarios/getautor/{id}")
    @CircuitBreaker(name = "topicos-ms", fallbackMethod = "getAutorFallback")
    AutorModel getAutor(@PathVariable String id);

    default AutorModel getAutorFallback(String id, Throwable exception) {

        log.info("Param = " + id);
        log.info("Ocorreu uma Exception " + exception.getMessage());

        AutorModel autorModel = new AutorModel();
        autorModel.setId(id);
        autorModel.setNome("");
        autorModel.setEmail("");
        return autorModel;
    }

}
