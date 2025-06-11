package com.projectfinal.greenthumb_backend.controller;

import com.projectfinal.greenthumb_backend.dto.ClienteRegistroDTO;
import com.projectfinal.greenthumb_backend.dto.UsuarioDTO;
import com.projectfinal.greenthumb_backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/sincronizar-usuario")
    public ResponseEntity<UsuarioDTO> sincronizarUsuario(@AuthenticationPrincipal Jwt jwt) {
        String auth0Id = jwt.getSubject();
        String email = jwt.getClaimAsString("email");
        String nombre = jwt.getClaimAsString("given_name") != null ? jwt.getClaimAsString("given_name") : jwt.getClaimAsString("nickname");
        String apellido = jwt.getClaimAsString("family_name");


        System.out.println("LOG: Recibida petición en /sincronizar-usuario para: " + email);
        UsuarioDTO usuario = authService.sincronizarUsuario(auth0Id, email, nombre, apellido);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/registrar-cliente")
    public ResponseEntity<UsuarioDTO> registrarCliente(@AuthenticationPrincipal Jwt jwt, @RequestBody ClienteRegistroDTO request) {
        String auth0Id = jwt.getSubject();
        System.out.println("LOG: Recibida petición en /registrar-cliente para Auth0 ID: " + auth0Id);
        UsuarioDTO nuevoCliente = authService.registrarNuevoCliente(request, auth0Id);
        return ResponseEntity.ok(nuevoCliente);
    }
}