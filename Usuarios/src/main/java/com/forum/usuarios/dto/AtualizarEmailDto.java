package com.forum.usuarios.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class AtualizarEmailDto {

    @NotBlank
    @Email
    private String antigoEmail;
    @NotBlank
    @Email
    private String novoEmail;

    public String getAntigoEmail() {
        return antigoEmail;
    }

    public void setAntigoEmail(String antigoEmail) {
        this.antigoEmail = antigoEmail;
    }

    public String getNovoEmail() {
        return novoEmail;
    }

    public void setNovoEmail(String novoEmail) {
        this.novoEmail = novoEmail;
    }
}
