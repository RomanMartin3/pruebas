package com.projectfinal.greenthumb_backend.repositories;

import com.projectfinal.greenthumb_backend.entities.FrecuenciasRiego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FrecuenciasRiegoRepository extends JpaRepository<FrecuenciasRiego, Integer> {
    Optional<FrecuenciasRiego> findByDescripcionFrecuenciaRiego(String descripcionFrecuenciaRiego);
}
