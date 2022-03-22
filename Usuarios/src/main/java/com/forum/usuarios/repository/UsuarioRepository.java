package com.forum.usuarios.repository;

import com.forum.usuarios.entity.UsuarioEntity;
import com.forum.usuarios.service.UsuarioService;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends MongoRepository<UsuarioEntity, String> {
    UsuarioEntity findByEmail(String email);
}
