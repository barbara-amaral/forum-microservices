package com.forum.usuarios.controller;

import com.forum.usuarios.entity.UsuarioEntity;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class AutenticacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void autenticar() throws Exception {

        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setEmail("teste@testando.com");
        usuarioEntity.setSenha(passwordEncoder.encode("123456"));

        mongoTemplate.save(usuarioEntity);

        String json = "{\"email\":\"teste@testando.com\",\"senha\":\"123456\"}";

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/login")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200));

        mongoTemplate.remove(usuarioEntity);

    }

}