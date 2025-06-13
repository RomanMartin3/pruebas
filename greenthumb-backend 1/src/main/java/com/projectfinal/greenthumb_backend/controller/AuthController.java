package com.projectfinal.greenthumb_backend.controller;

import com.projectfinal.greenthumb_backend.dto.AuthRequestDTO;
import com.projectfinal.greenthumb_backend.dto.ClienteRegistroDTO;
import com.projectfinal.greenthumb_backend.dto.UsuarioDTO;
import com.projectfinal.greenthumb_backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*") // Asegúrate de permitir el acceso desde tu frontend
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequestDTO authRequest) {
        try {
            UsuarioDTO userDTO = authService.authenticateUser(authRequest);
            return ResponseEntity.ok(userDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            // Para cualquier otro error inesperado
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno del servidor.", "details", e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerClient(@RequestBody ClienteRegistroDTO registroDTO) {
        try {
            // La contraseña debería venir en el DTO de registro.
            if (registroDTO.getContrasena() == null || registroDTO.getContrasena().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "La contraseña es obligatoria para el registro."));
            }
            UsuarioDTO newUser = authService.registerClient(registroDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno del servidor durante el registro.", "details", e.getMessage()));
        }
    }
}