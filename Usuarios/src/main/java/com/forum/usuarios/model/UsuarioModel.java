package com.forum.usuarios.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class UsuarioModel {

    private String id;

    @NotNull(message = "Nome nao deve ser nulo.")
    @Size(min=2, message = "Nome nao deve ser menor que 2 caracteres.")
    private String nome;

    @NotNull(message = "Email nao deve ser nulo.")
    @Size(min=2, message = "Email nao deve ser menor que 2 caracteres.")
    @Email
    private String email;

    @NotNull(message = "Senha nao deve ser nula.")
    @Size(min=2, max=16, message = "Senha nao deve ser menor que 2 caracteres e maior que 16 caracteres.")
    private String senha;

    private List<Perfil> perfis = new ArrayList<>();

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<Perfil> getPerfis() {
        return perfis;
    }

    public void setPerfis(List<Perfil> perfis) {
        this.perfis = perfis;
    }
}
