package com.projectfinal.greenthumb_backend.repositories;


import com.projectfinal.greenthumb_backend.entities.NivelesLuz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface NivelesLuzRepository extends JpaRepository<NivelesLuz, Integer> {
    Optional<NivelesLuz> findByDescripcionNivelLuz(String descripcionNivelLuz);
}
