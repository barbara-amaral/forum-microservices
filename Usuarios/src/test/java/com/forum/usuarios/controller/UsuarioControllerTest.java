package com.forum.usuarios.controller;

import com.forum.usuarios.entity.UsuarioEntity;
import com.forum.usuarios.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class UsuarioControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UsuarioService usuarioService;

    @Test
    void cadastrar() throws Exception {

        String json = "{\"nome\":\"Teste\",\"email\":\"teste@ibm.com\",\"senha\":\"123456\"}";

        mvc
                .perform(MockMvcRequestBuilders
                        .post("/usuarios/cadastrar")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(201));

        UsuarioEntity usuarioEntity = usuarioService.getUserEntityByEmail("teste@ibm.com");
        mongoTemplate.remove(usuarioEntity);

    }

    @Test
    void deletar() throws Exception {

        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setEmail("teste@ibm.com");
        mongoTemplate.save(usuarioEntity);

        mvc.
                perform(MockMvcRequestBuilders.delete("/usuarios/deletar/{id}", usuarioEntity.getId()))
                .andExpect(MockMvcResultMatchers.status().is(200));

    }

    @Test
    void listar() throws Exception {

        mvc.
                perform(MockMvcRequestBuilders.get("/usuarios/listar"))
                .andExpect(MockMvcResultMatchers.status().is(200));

    }

    @Test
    void atualizarEmail() throws Exception {

        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setEmail("teste@ibm.com");
        mongoTemplate.save(usuarioEntity);

        String json = "{\"antigoEmail\":\"teste@ibm.com\",\"novoEmail\":\"teste@atualizado.com\"}";

        mvc.
                perform(MockMvcRequestBuilders.put("/usuarios/atualizar")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200));

        mongoTemplate.remove(usuarioEntity);

    }

    @Test
    void getAutor() throws Exception {

        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setEmail("teste@ibm.com");
        mongoTemplate.save(usuarioEntity);

        mvc.
                perform(MockMvcRequestBuilders.get("/usuarios/getautor/{id}", usuarioEntity.getId()))
                .andExpect(MockMvcResultMatchers.status().is(200));

        mongoTemplate.remove(usuarioEntity);

    }
}