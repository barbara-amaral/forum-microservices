package com.forum.usuarios.controller;

import com.forum.usuarios.model.LoginModel;
import com.forum.usuarios.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    TokenService tokenService;

    private static final Logger log = LoggerFactory.getLogger(AutenticacaoController.class);

    @PostMapping
    public ResponseEntity<?> autenticar(@RequestBody @Valid LoginModel loginModel, HttpServletResponse httpServletResponse) {

        UsernamePasswordAuthenticationToken dadosLogin = loginModel.converter();

        try{

            Authentication authentication = authenticationManager.authenticate(dadosLogin);
            log.info("Usuario autenticado");

            tokenService.gerarToken(authentication, httpServletResponse);
            log.info("Token gerado.");

            return ResponseEntity.ok("Usuario autenticado com sucesso.");

        }catch (AuthenticationException e){
            log.info("Ocorreu um erro durante a autenticacao.");
            return ResponseEntity.badRequest().body("Dados inválidos.");
        }
    }
}
