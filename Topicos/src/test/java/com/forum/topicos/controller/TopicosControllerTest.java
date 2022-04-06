package com.forum.topicos.controller;

import com.forum.topicos.entity.TopicoEntity;
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
class TopicosControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void cadastrar() {

    }


    @Test
    void deletar() throws Exception {

        TopicoEntity topicoEntity = new TopicoEntity();
        mongoTemplate.save(topicoEntity);

        mvc.perform(MockMvcRequestBuilders.delete("/topicos/deletar/{id}", topicoEntity.getId()))
                .andExpect(MockMvcResultMatchers.status().is(200));

    }

    @Test
    void atualizar() throws Exception {

        TopicoEntity topicoEntity = new TopicoEntity();
        mongoTemplate.save(topicoEntity);

        String json = "{\"mensagem\":\"Atualizando topico\"}";

        mvc.perform(MockMvcRequestBuilders.put("/topicos/atualizar/{id}", topicoEntity.getId())
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200));

        mongoTemplate.remove(topicoEntity);

    }
    @Test
    void listar() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/topicos/listar"))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    void buscar() throws Exception {

        TopicoEntity topicoEntity = new TopicoEntity();
        mongoTemplate.save(topicoEntity);

        mvc.perform(MockMvcRequestBuilders.get("/topicos/buscar/{id}", topicoEntity.getId()))
                .andExpect(MockMvcResultMatchers.status().is(200));

        mongoTemplate.remove(topicoEntity);

    }
}