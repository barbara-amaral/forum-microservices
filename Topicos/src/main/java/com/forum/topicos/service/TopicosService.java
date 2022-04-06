package com.forum.topicos.service;

import com.forum.topicos.dto.AtualizarTopicoDto;
import com.forum.topicos.dto.TopicoDTO;
import com.forum.topicos.model.AutorModel;
import com.forum.topicos.response.TopicosListResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public interface TopicosService {
    TopicoDTO cadastrarTopico(TopicoDTO topicoDTO, HttpServletRequest httpServletRequest);
    String getUserid(HttpServletRequest httpServletRequest);
    AutorModel getAutor(String id);
    ResponseEntity<String> deletarTopico(String id);
    ResponseEntity<?> getTopicoById(String id);
    ResponseEntity<?> atualizarTopico(TopicoDTO topicoDTO, AtualizarTopicoDto atualizarTopicoDto);
    ResponseEntity<List<TopicosListResponseModel>> listar();
}
