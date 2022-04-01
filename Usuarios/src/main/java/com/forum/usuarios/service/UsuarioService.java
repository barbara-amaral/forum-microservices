package com.forum.usuarios.service;

import com.forum.usuarios.dto.UsuarioDTO;
import com.forum.usuarios.model.UsuarioResponseModel;

import java.util.List;

public interface UsuarioService {

    UsuarioDTO cadastrarUsuario(UsuarioDTO usuarioDTO);
    void deletarUsuario(String id);
    List<UsuarioResponseModel> listar();
    UsuarioDTO getUserDetailsByEmail(String email);
    UsuarioDTO getUserDetailsById(String id);
}
