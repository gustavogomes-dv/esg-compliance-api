package com.esg.compliance.security;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Aqui eu testo o JwtService sozinho, sem subir o Spring e sem precisar do banco Oracle
public class JwtServiceTest {

    // Aqui eu crio o serviço com um segredo de teste (mais de 32 caracteres) e validade de 60 segundos
    private final JwtService jwtService = new JwtService(
            "segredo-de-teste-com-mais-de-trinta-e-dois-caracteres", 60_000L);

    // Aqui eu monto um usuário de mentira para gerar o token
    private UserDetails criarUsuario(String email) {
        return User.withUsername(email)
                .password("sem-uso")
                .roles("USER")
                .build();
    }

    @Test
    void geraTokenValidoParaOUsuario() {
        UserDetails usuario = criarUsuario("analista@esg.com");

        String token = jwtService.generateToken(usuario);

        // Aqui eu confirmo que o e-mail dentro do token é o do usuário e que o token é aceito
        assertEquals("analista@esg.com", jwtService.extractUsername(token));
        assertTrue(jwtService.isTokenValid(token, usuario));
    }

    @Test
    void rejeitaTokenDeOutroUsuario() {
        String token = jwtService.generateToken(criarUsuario("analista@esg.com"));

        // Aqui eu confirmo que um token emitido para uma pessoa não vale para outra
        assertFalse(jwtService.isTokenValid(token, criarUsuario("outra@esg.com")));
    }

    @Test
    void rejeitaTokenAssinadoComOutroSegredo() {
        JwtService outroServico = new JwtService(
                "outro-segredo-de-teste-com-mais-de-trinta-e-dois-caracteres", 60_000L);
        String tokenFalso = outroServico.generateToken(criarUsuario("analista@esg.com"));

        // Aqui eu confirmo que um token assinado com outro segredo é recusado ao ser lido
        assertThrows(JwtException.class, () -> jwtService.extractUsername(tokenFalso));
    }
}