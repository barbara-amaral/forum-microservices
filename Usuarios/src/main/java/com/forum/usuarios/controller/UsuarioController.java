package com.forum.usuarios.controller;

import com.forum.usuarios.dto.UsuarioDTO;
import com.forum.usuarios.model.UsuarioModel;
import com.forum.usuarios.model.UsuarioResponseModel;
import com.forum.usuarios.service.UsuarioService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private Environment env;

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/status/check")
    public String status(){
        return "working on port " + env.getProperty("local.server.port");
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioResponseModel> cadastrar(@RequestBody @Valid UsuarioModel usuarioModel) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UsuarioDTO usuarioDTO = modelMapper.map(usuarioModel, UsuarioDTO.class);

        UsuarioDTO usuarioCriado = usuarioService.cadastrarUsuario(usuarioDTO);

        UsuarioResponseModel responseDTO = modelMapper.map(usuarioCriado, UsuarioResponseModel.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

}
