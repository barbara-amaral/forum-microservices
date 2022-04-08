package com.forum.usuarios.security;

import com.forum.usuarios.repository.UsuarioRepository;
import com.forum.usuarios.service.AutenticacaoService;
import com.forum.usuarios.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

    private Environment environment;
    private AutenticacaoService autenticacaoService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UsuarioRepository usuarioRepository;
    private TokenService tokenService;

    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Autowired
    public SecurityConfigurations(UsuarioRepository usuarioRepository, TokenService tokenService, Environment environment, AutenticacaoService autenticacaoService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.environment = environment;
        this.autenticacaoService = autenticacaoService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.tokenService = tokenService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(autenticacaoService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/**").hasIpAddress(environment.getProperty("gateway.ip"))
                .antMatchers("/login").permitAll()
                .antMatchers("/**").permitAll()
                .antMatchers(HttpMethod.GET, "/usuarios-ms/actuator/*").hasIpAddress(environment.getProperty("gateway.ip"))
                .anyRequest().authenticated()
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new AutenticacaoFilter(usuarioRepository, tokenService), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

}


