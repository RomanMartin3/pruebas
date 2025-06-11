
package com.projectfinal.greenthumb_backend.repositories;

import com.projectfinal.greenthumb_backend.entities.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Integer> {
    // Método para buscar por email y así evitar duplicados
    Optional<Administrador> findByEmail(String email);
    // El método count() ya viene de JpaRepository y lo usaremos
}
