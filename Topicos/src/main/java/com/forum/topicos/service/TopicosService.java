package com.forum.topicos.service;

import com.forum.topicos.dto.TopicoDTO;
import com.forum.topicos.model.AutorModel;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public interface TopicosService {
    TopicoDTO cadastrarTopico(TopicoDTO topicoDTO, HttpServletRequest httpServletRequest);
    String getUserid(HttpServletRequest httpServletRequest);
    AutorModel getUserDetailsById(String id);
}
