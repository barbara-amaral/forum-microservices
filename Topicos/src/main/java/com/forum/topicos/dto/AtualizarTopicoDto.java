package com.forum.topicos.dto;

import javax.validation.constraints.NotBlank;

public class AtualizarTopicoDto {

    @NotBlank
    private String mensagem;

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
