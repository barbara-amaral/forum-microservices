package com.forum.usuarios.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

@Document(collection = "Perfil")
public class Perfil implements GrantedAuthority {

    String id;
    String nome;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String getAuthority() {
        return nome;
    }
}
