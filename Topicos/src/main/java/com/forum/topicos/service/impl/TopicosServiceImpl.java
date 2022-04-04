package com.forum.topicos.service.impl;

import com.forum.topicos.dto.TopicoDTO;
import com.forum.topicos.entity.TopicoEntity;
import com.forum.topicos.model.AutorModel;
import com.forum.topicos.model.StatusTopico;
import com.forum.topicos.repository.TopicoRepository;
import com.forum.topicos.response.TopicoResponseModel;
import com.forum.topicos.service.TopicosService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TopicosServiceImpl implements TopicosService {

    @Autowired
    TopicoRepository topicoRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    Environment environment;

    private static final Logger log = LoggerFactory.getLogger(TopicoResponseModel.class);

    @Override
    public TopicoDTO cadastrarTopico(TopicoDTO topicoDTO, HttpServletRequest httpServletRequest) {

        log.info("Entrando no metodo cadastrarTopico.");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm");
        topicoDTO.setDataCriacao(LocalDateTime.now().format(formatter));

        topicoDTO.setStatus(StatusTopico.NAO_RESPONDIDO);

        String id = getUserid(httpServletRequest);

        log.info("Id do usuario identificado: " + id);

        AutorModel autorModel = getUserDetailsById(id);
        topicoDTO.setAutor(autorModel);

        log.info("Autor do topico identificado: " + autorModel.getNome());

        ModelMapper modelMapper = new ModelMapper();
        TopicoEntity topicoEntity = modelMapper.map(topicoDTO, TopicoEntity.class);

        topicoRepository.save(topicoEntity);

        log.info("Topico salvo na base de dados.");

        TopicoDTO topicoCriado = new ModelMapper().map(topicoEntity, TopicoDTO.class);

        log.info("Topico criado.");

        return topicoCriado;

    }

    @Override
    public String getUserid(HttpServletRequest httpServletRequest) {

        String authorization = httpServletRequest.getHeader("Authorization");
        String token = authorization.substring(7);
        Claims claims = Jwts.parser().setSigningKey(environment.getProperty("token.secret")).parseClaimsJws(token).getBody();
        String id = claims.getSubject();

        return id;
    }

    @Override
    public AutorModel getUserDetailsById(String id) {

        String autorUrl = String.format("http://USUARIOS-MS/usuarios/autor/%s",id);
        ResponseEntity<AutorModel> autor = restTemplate.exchange(autorUrl, HttpMethod.GET, null, new ParameterizedTypeReference<AutorModel>() {
        });
        AutorModel autorModel = autor.getBody();

        return autorModel;
    }
}
