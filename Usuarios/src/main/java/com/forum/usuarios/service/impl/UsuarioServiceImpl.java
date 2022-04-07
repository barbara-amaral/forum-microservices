package com.forum.usuarios.service.impl;

import com.forum.usuarios.dto.AtualizarEmailDto;
import com.forum.usuarios.dto.UsuarioDTO;
import com.forum.usuarios.entity.UsuarioEntity;
import com.forum.usuarios.model.UsuarioResponseModel;
import com.forum.usuarios.repository.UsuarioRepository;
import com.forum.usuarios.service.UsuarioService;
import com.google.common.reflect.TypeToken;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
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
    public ResponseEntity<String> deletarUsuario(String id) {

        log.info("Entrando no metodo deletar.");

        UsuarioDTO usuarioDTO = getUserDetailsById(id);

        if(usuarioDTO == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario nao encontrado.");
        }

        usuarioRepository.deleteById(usuarioDTO.getId());
        log.info("Usuario deletado.");

        return ResponseEntity.ok("Usuario deletado com sucesso");
    }

    @Override
    public ResponseEntity<List<UsuarioResponseModel>> listar(){
        
        log.info("Entrando no metodo listar usuarios.");
        
        List<UsuarioResponseModel> returnValue = new ArrayList<>();
        List<UsuarioEntity> entityList = usuarioRepository.findAll();
        
        if(entityList.isEmpty() || entityList == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(returnValue);
        }
        
        Type listType = new TypeToken<List<UsuarioResponseModel>>(){}.getType();
   
        returnValue = new ModelMapper().map(entityList, listType);
        
        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }

    @Override
    public ResponseEntity<?> atualizarEmail(AtualizarEmailDto atualizarEmailDto) {
        UsuarioEntity usuarioEntity = usuarioRepository.findByEmail(atualizarEmailDto.getAntigoEmail());

        if(usuarioEntity == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email inexistente.");
        }

        if(atualizarEmailDto.getNovoEmail().matches(usuarioEntity.getEmail())){
            log.info("Ocorreu um erro: E-mail não pode ser o mesmo.");
            return ResponseEntity.badRequest().body("E-mail deve ser diferente do anterior.");
        }

        usuarioEntity.setEmail(atualizarEmailDto.getNovoEmail());
        usuarioRepository.save(usuarioEntity);

        UsuarioResponseModel usuarioResponseModel = new ModelMapper().map(usuarioEntity, UsuarioResponseModel.class);
        return ResponseEntity.ok(usuarioResponseModel);
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
    public UsuarioEntity getUserEntityByEmail(String email) {
        UsuarioEntity usuarioEntity = usuarioRepository.findByEmail(email);

        if(usuarioEntity == null){
            log.info("Usuario nao encontrado na base de dados.");
            throw new UsernameNotFoundException("Usuario nao encontrado");
        }

        log.info("Usuario " + usuarioEntity.getNome() + " encontrado na base de dados");
        return usuarioEntity;
    }

    @Override
    public UsuarioDTO getAutor(String id) {
        return getUserDetailsById(id);
    }

}
