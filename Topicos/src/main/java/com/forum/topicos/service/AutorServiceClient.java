package com.forum.topicos.service;

import com.forum.topicos.model.AutorModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuarios-ms")
public interface AutorServiceClient {

    @GetMapping("/usuarios/getautorr/{id}")
    AutorModel getAutor(@PathVariable String id);

}
