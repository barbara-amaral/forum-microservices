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
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private Environment env;

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/status/check")
    public String status(){
        return "working on port " + env.getProperty("local.server.port") + "with token " + env.getProperty("token.secret");
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

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<String> deletar(@PathVariable(value = "id") String id){
        usuarioService.deletarUsuario(id);

        return ResponseEntity.status(HttpStatus.OK).body("Usuario deletado com sucesso");
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listar() {
        List<UsuarioResponseModel> responseModelList = usuarioService.listar();

        if (responseModelList.isEmpty() || responseModelList == null){

            return ResponseEntity.status(HttpStatus.OK).body("Nao ha usuarios cadastrados.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseModelList);
    }

}
