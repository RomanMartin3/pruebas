package com.projectfinal.greenthumb_backend.repositories;

import com.projectfinal.greenthumb_backend.entities.CostoProductoActual;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface CostoProductoActualRepository extends JpaRepository<CostoProductoActual, Integer> {
    // El ID es ProductoID, JpaRepository<CostoProductoActual, Integer> ya cubre findById
}