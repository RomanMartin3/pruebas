package com.projectfinal.greenthumb_backend.repositories;
import com.projectfinal.greenthumb_backend.entities.DetallesHerramienta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface DetallesHerramientaRepository extends JpaRepository<DetallesHerramienta, Integer> {
    // El ID es ProductoID
}
