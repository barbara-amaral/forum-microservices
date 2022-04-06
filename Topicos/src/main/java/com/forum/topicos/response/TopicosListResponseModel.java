package com.forum.topicos.response;

import com.forum.topicos.model.AutorModel;
import com.forum.topicos.model.StatusTopico;

public class TopicosListResponseModel {

    private String id;
    private String titulo;
    private String tag;
    private String dataCriacao;
    private String autor;
    private StatusTopico status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(AutorModel autor) {
        this.autor = autor.getNome();
    }

    public StatusTopico getStatus() {
        return status;
    }

    public void setStatus(StatusTopico status) {
        this.status = status;
    }
}
