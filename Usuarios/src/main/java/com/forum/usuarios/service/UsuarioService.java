package com.forum.usuarios.service;

import com.forum.usuarios.dto.UsuarioDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsuarioService extends UserDetailsService {

    UsuarioDTO cadastrarUsuario(UsuarioDTO usuarioDTO);
    UsuarioDTO getUserDetailsByEmail(String email);
}
