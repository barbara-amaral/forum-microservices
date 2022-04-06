package com.forum.topicos.service.impl;

import com.forum.topicos.dto.AtualizarTopicoDto;
import com.forum.topicos.dto.TopicoDTO;
import com.forum.topicos.entity.TopicoEntity;
import com.forum.topicos.model.AutorModel;
import com.forum.topicos.model.StatusTopico;
import com.forum.topicos.repository.TopicoRepository;
import com.forum.topicos.response.TopicosListResponseModel;
import com.forum.topicos.service.AutorServiceClient;
import com.forum.topicos.service.TopicosService;
import com.google.common.reflect.TypeToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TopicosServiceImpl implements TopicosService {

    @Autowired
    TopicoRepository topicoRepository;

    @Autowired
    AutorServiceClient autorServiceClient;

    @Autowired
    Environment environment;

    private static final Logger log = LoggerFactory.getLogger(TopicosServiceImpl.class);

    @Override
    public TopicoDTO cadastrarTopico(TopicoDTO topicoDTO, HttpServletRequest httpServletRequest) {

        log.info("Entrando no metodo cadastrarTopico.");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm");
        topicoDTO.setDataCriacao(LocalDateTime.now().format(formatter));

        topicoDTO.setStatus(StatusTopico.NAO_RESPONDIDO);

        String id = getUserid(httpServletRequest);

        log.info("Id do usuario identificado: " + id);

        AutorModel autorModel = getAutor(id);
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

        log.info("Entrando no metodo getUserid.");

        String authorization = httpServletRequest.getHeader("Authorization");
        String token = authorization.substring(7);
        Claims claims = Jwts.parser().setSigningKey(environment.getProperty("token.secret")).parseClaimsJws(token).getBody();
        String id = claims.getSubject();

        return id;
    }

    @Override
    public AutorModel getAutor(String id) {

        log.info("Entrando no metodo getAutor.");

        AutorModel autor = autorServiceClient.getAutor(id);

        log.info("Comunicacao com microservico Usuarios-MS bem sucedida.");
        return autor;
    }

    @Override
    public ResponseEntity<String> deletarTopico(String id) {

        log.info("Entrando no metodo deletarTopico.");

        Optional<TopicoEntity> topicoEntity = topicoRepository.findById(id);

        if(topicoEntity.isEmpty()){
            log.info("Topico nao encontrado na base de dados.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Topico nao encontrado.");
        }

        log.info("Topico encontrado.");
        topicoRepository.delete(topicoEntity.get());

        log.info("Topico deletado.");
        return ResponseEntity.ok("Topico deletado com sucesso.");
    }

    @Override
    public ResponseEntity<?> getTopicoById(String id) {

        log.info("Entrando no metodo getTopicoById.");

        Optional<TopicoEntity> topicoEntity = topicoRepository.findById(id);

        if (topicoEntity.isEmpty()){
            log.info("Topico nao encontrado na base de dados.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Topico nao encontrado.");
        }

        log.info("Topico encontrado na base de dados.");

        ModelMapper modelMapper = new ModelMapper();
        TopicoDTO topicoDTO = modelMapper.map(topicoEntity, TopicoDTO.class);

        log.info("Retornando topico.");

        return ResponseEntity.ok().body(topicoDTO);
    }

    @Override
    public ResponseEntity<?> atualizarTopico(TopicoDTO topicoDTO, AtualizarTopicoDto atualizarTopicoDto) {

        log.info("Entrando no metodo atualizarTopico.");

        ModelMapper modelMapper = new ModelMapper();
        TopicoEntity topicoEntity = modelMapper.map(topicoDTO, TopicoEntity.class);

        topicoEntity.setMensagem(atualizarTopicoDto.getMensagem());
        log.info("Topico atualizado.");

        topicoRepository.save(topicoEntity);

        log.info("Topico salvo no banco de dados.");

        topicoDTO = modelMapper.map(topicoEntity, TopicoDTO.class);

        log.info("Retornando topico.");

        return ResponseEntity.ok().body(topicoDTO);
    }

    @Override
    public ResponseEntity<List<TopicosListResponseModel>> listar() {

        log.info("Entrando no metodo listar.");

        List<TopicosListResponseModel> topicosResponseModel = new ArrayList<>();
        List<TopicoEntity> topicoEntityList = topicoRepository.findAll();

        if(topicoEntityList.isEmpty()){
            log.info("Nao foram encontrados topicos no banco de dados.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(topicosResponseModel);
        }

        log.info("Topicos encontrados no banco de dados.");

        Type listType = new TypeToken<List<TopicosListResponseModel>>(){}.getType();
        topicosResponseModel = new ModelMapper().map(topicoEntityList, listType);

        log.info("Retornando topicos.");

        return ResponseEntity.ok().body(topicosResponseModel);
    }
}
