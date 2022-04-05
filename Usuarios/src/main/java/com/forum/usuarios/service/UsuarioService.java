package com.forum.usuarios.service;

import com.forum.usuarios.dto.AtualizarEmailDto;
import com.forum.usuarios.dto.UsuarioDTO;
import com.forum.usuarios.model.UsuarioResponseModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UsuarioService {

    UsuarioDTO cadastrarUsuario(UsuarioDTO usuarioDTO);
    ResponseEntity<String> deletarUsuario(String id);
    ResponseEntity<List<UsuarioResponseModel>> listar();
    ResponseEntity atualizarEmail(AtualizarEmailDto atualizarEmailDto);
    UsuarioDTO getUserDetailsById(String id);
    UsuarioDTO getAutor(String id);
}
