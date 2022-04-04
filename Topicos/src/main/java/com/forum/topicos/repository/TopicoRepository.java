package com.forum.topicos.repository;

import com.forum.topicos.entity.TopicoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicoRepository extends MongoRepository<TopicoEntity, String> {
}
