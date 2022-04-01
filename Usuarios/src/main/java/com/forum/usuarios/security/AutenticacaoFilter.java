package com.forum.usuarios.security;

import com.forum.usuarios.entity.UsuarioEntity;
import com.forum.usuarios.repository.UsuarioRepository;
import com.forum.usuarios.service.TokenService;
import com.forum.usuarios.service.impl.UsuarioServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(AutenticacaoFilter.class);

    @Autowired
    public AutenticacaoFilter(UsuarioRepository usuarioRepository, TokenService tokenService){
        this.usuarioRepository = usuarioRepository;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = recuperarToken(request);

        boolean valido = tokenService.isTokenValido(token);

        if(valido == true){
            autenticarCliente(token);
        }

        filterChain.doFilter(request, response);

    }

    private void autenticarCliente(String token) {

        String idUsuario = tokenService.getIdUsuario(token);

        Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(idUsuario);

        if (usuarioEntity.isEmpty()){
            log.info("Usuario nao encontrado. Uma excecao sera gerada.");
            throw new UsernameNotFoundException("Usuario nao encontrado");
        }else{

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuarioEntity.get().getEmail(), usuarioEntity.get().getSenha(), usuarioEntity.get().getPerfis());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Usuario " + usuarioEntity.get().getNome() + " autenticado");
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
