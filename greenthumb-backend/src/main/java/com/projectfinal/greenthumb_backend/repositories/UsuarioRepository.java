package com.projectfinal.greenthumb_backend.repositories;


import com.projectfinal.greenthumb_backend.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByAuth0Id(String auth0Id);
    Optional<Usuario> findByEmail(String email);
}