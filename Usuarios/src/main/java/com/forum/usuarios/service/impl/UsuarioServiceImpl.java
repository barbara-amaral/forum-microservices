package com.forum.usuarios.service.impl;

import com.forum.usuarios.dto.UsuarioDTO;
import com.forum.usuarios.entity.UsuarioEntity;
import com.forum.usuarios.repository.UsuarioRepository;
import com.forum.usuarios.service.UsuarioService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UsuarioServiceImpl implements UsuarioService {


    UsuarioRepository usuarioRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.usuarioRepository = usuarioRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UsuarioDTO cadastrarUsuario(UsuarioDTO usuarioDTO) {

        usuarioDTO.setSenha(bCryptPasswordEncoder.encode(usuarioDTO.getSenha()));

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UsuarioEntity usuarioEntity = modelMapper.map(usuarioDTO, UsuarioEntity.class);

        usuarioRepository.save(usuarioEntity);

        UsuarioDTO usuarioCriado = modelMapper.map(usuarioEntity, UsuarioDTO.class);

        return usuarioCriado;
    }

    @Override
    public UsuarioDTO getUserDetailsByEmail(String email) {
        UsuarioEntity usuarioEntity = usuarioRepository.findByEmail(email);

        if(usuarioEntity == null) throw new UsernameNotFoundException(email);
        return new ModelMapper().map(usuarioEntity, UsuarioDTO.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioEntity usuarioEntity = usuarioRepository.findByEmail(username);

        if(usuarioEntity == null) throw new UsernameNotFoundException(username);
        return new User(usuarioEntity.getEmail(), usuarioEntity.getSenha(), true, true, true, true, (Collection<? extends GrantedAuthority>) usuarioEntity.getPerfis());
    }
}
