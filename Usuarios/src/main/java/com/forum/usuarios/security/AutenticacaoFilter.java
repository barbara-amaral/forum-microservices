package com.forum.usuarios.security;

import com.forum.usuarios.entity.UsuarioEntity;
import com.forum.usuarios.repository.UsuarioRepository;
import com.forum.usuarios.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class AutenticacaoFilter extends OncePerRequestFilter {

    private UsuarioRepository usuarioRepository;
    private TokenService tokenService;

    @Autowired
    public AutenticacaoFilter(UsuarioRepository usuarioRepository, TokenService tokenService){
        this.usuarioRepository = usuarioRepository;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = recuperarToken(request);

        boolean valido = tokenService.isTokenValido(token);

        if(valido){
            autenticarCliente(token);
        }

        filterChain.doFilter(request, response);

    }

    private void autenticarCliente(String token) {

        String idUsuario = tokenService.getIdUsuario(token);

        Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(idUsuario);

        if (usuarioEntity.isEmpty()){
                throw new UsernameNotFoundException("Usuario nao encontrado");
        }else if(usuarioEntity.isPresent()){
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuarioEntity.get().getEmail(), usuarioEntity.get().getSenha(), usuarioEntity.get().getPerfis());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private String recuperarToken(HttpServletRequest httpServletRequest){

        String token = httpServletRequest.getHeader("Authorization");
        if(token==null || token.isEmpty() || !token.startsWith("Bearer ")){
            return null;
        }
        return token.substring(7);
    }
}
