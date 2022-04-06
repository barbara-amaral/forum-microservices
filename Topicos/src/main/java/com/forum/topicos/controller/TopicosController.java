package com.forum.topicos.controller;

import com.forum.topicos.dto.AtualizarTopicoDto;
import com.forum.topicos.dto.TopicoDTO;
import com.forum.topicos.model.TopicoModel;
import com.forum.topicos.response.TopicoResponseModel;
import com.forum.topicos.response.TopicosListResponseModel;
import com.forum.topicos.service.TopicosService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

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

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<String> deletar(@PathVariable(value = "id") String id){
        return topicosService.deletarTopico(id);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizar(@PathVariable(value = "id") String id, @RequestBody @Valid AtualizarTopicoDto atualizarTopicoDto){

        ResponseEntity<?> topicoById = topicosService.getTopicoById(id);

        if (topicoById.getStatusCode().value() ==  200){
            return topicosService.atualizarTopico((TopicoDTO) topicoById.getBody(), atualizarTopicoDto);
        }

        return topicoById;
    }

    @GetMapping("/listar")
    public ResponseEntity<List<TopicosListResponseModel>> listar(){
        return topicosService.listar();
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscar(@PathVariable(value = "id") String id){
        return topicosService.getTopicoById(id);
    }
}
