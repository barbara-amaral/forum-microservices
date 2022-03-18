package com.forum.usuarios.controller;

import com.forum.usuarios.dto.UsuarioDTO;
import com.forum.usuarios.dto.UsuarioResponseDTO;
import com.forum.usuarios.entity.UsuarioEntity;
import com.forum.usuarios.model.Usuario;
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
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@RequestBody @Valid Usuario usuario) {

        //teste

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UsuarioDTO usuarioDTO = modelMapper.map(usuario, UsuarioDTO.class);

        UsuarioDTO usuarioCriado = usuarioService.cadastrarUsuario(usuarioDTO);

        UsuarioResponseDTO responseDTO = modelMapper.map(usuarioCriado, UsuarioResponseDTO.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}
