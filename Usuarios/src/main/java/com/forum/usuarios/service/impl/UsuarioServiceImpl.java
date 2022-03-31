package com.forum.usuarios.service.impl;

import com.forum.usuarios.dto.UsuarioDTO;
import com.forum.usuarios.entity.UsuarioEntity;
import com.forum.usuarios.repository.UsuarioRepository;
import com.forum.usuarios.service.UsuarioService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {


    UsuarioRepository usuarioRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.usuarioRepository = usuarioRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UsuarioDTO cadastrarUsuario(UsuarioDTO usuarioDTO) {

        log.info("Entrando no metodo cadastrar para usuario: " + usuarioDTO.getNome());

        usuarioDTO.setSenha(bCryptPasswordEncoder.encode(usuarioDTO.getSenha()));

        log.info("Senha gerada.");

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UsuarioEntity usuarioEntity = modelMapper.map(usuarioDTO, UsuarioEntity.class);

        usuarioRepository.save(usuarioEntity);

        log.info("Usuario " + usuarioDTO.getNome() + " salvo na base de dados.");

        UsuarioDTO usuarioCriado = modelMapper.map(usuarioEntity, UsuarioDTO.class);

        log.info("Usuario " + usuarioCriado.getNome() + " criado.");

        return usuarioCriado;
    }

    @Override
    public void deletarUsuario(String id) {

        log.info("Entrando no metodo deletar.");

        UsuarioDTO usuarioDTO = getUserDetailsById(id);

        usuarioRepository.deleteById(usuarioDTO.getId());

        log.info("Usuario deletado.");
    }

    @Override
    public UsuarioDTO getUserDetailsByEmail(String email) {
        UsuarioEntity usuarioEntity = usuarioRepository.findByEmail(email);

        if(usuarioEntity == null) throw new UsernameNotFoundException(email);
        return new ModelMapper().map(usuarioEntity, UsuarioDTO.class);
    }

    @Override
    public UsuarioDTO getUserDetailsById(String id) {

        Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(id);

        if(usuarioEntity.isEmpty()){
            log.info("Usuario nao encontrado na base de dados.");
            throw new UsernameNotFoundException("Usuario nao encontrado");
        }
        log.info("Usuario " + usuarioEntity.get().getNome() + " encontrado na base de dados");
        return new ModelMapper().map(usuarioEntity, UsuarioDTO.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioEntity usuarioEntity = usuarioRepository.findByEmail(username);

        if(usuarioEntity == null) throw new UsernameNotFoundException(username);
        return new User(usuarioEntity.getEmail(), usuarioEntity.getSenha(), true, true, true, true, (Collection<? extends GrantedAuthority>) usuarioEntity.getPerfis());
    }
}
