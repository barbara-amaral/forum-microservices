package com.forum.usuarios.service;

import com.forum.usuarios.entity.UsuarioEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Service
public class TokenService {

    private Environment environment;

    @Autowired
    public TokenService(Environment environment){
        this.environment = environment;
    }

    public String gerarToken(Authentication authentication, HttpServletResponse httpServletResponse) {

        UsuarioEntity logado = ( (UsuarioEntity) authentication.getPrincipal());
        Date hoje = new Date();
        Date dataExp = new Date(hoje.getTime() + Long.parseLong(environment.getProperty("token.expiration_time")));

        String token = Jwts.builder()
                .setIssuer("Usuarios MS")
                .setSubject(logado.getId())
                .setIssuedAt(hoje)
                .setExpiration(dataExp)
                .signWith(SignatureAlgorithm.HS256, environment.getProperty("token.secret"))
                .compact();

        httpServletResponse.addHeader("token", token);
        httpServletResponse.addHeader("userId", logado.getId());

        return token;
    }

    public boolean isTokenValido(String token) {

        try {
            Jwts.parser().setSigningKey(environment.getProperty("token.secret")).parseClaimsJws(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public String getIdUsuario(String token) {

        Claims claims = Jwts.parser().setSigningKey(environment.getProperty("token.secret")).parseClaimsJws(token).getBody();
        return claims.getSubject();

    }

}
