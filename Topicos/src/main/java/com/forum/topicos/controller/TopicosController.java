package com.forum.topicos.controller;

import com.forum.topicos.dto.TopicoDTO;
import com.forum.topicos.model.TopicoModel;
import com.forum.topicos.response.TopicoResponseModel;
import com.forum.topicos.service.TopicosService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    TopicosService topicosService;

    @PostMapping("/novotopico")
    public ResponseEntity<TopicoResponseModel> cadastrar(@RequestBody @Valid TopicoModel topicoModel, HttpServletRequest httpServletRequest) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        TopicoDTO topicoDTO = modelMapper.map(topicoModel, TopicoDTO.class);

        TopicoDTO topicoCriado = topicosService.cadastrarTopico(topicoDTO, httpServletRequest);

        TopicoResponseModel topicoResponseModel = new ModelMapper().map(topicoCriado, TopicoResponseModel.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(topicoResponseModel);
    }
}
