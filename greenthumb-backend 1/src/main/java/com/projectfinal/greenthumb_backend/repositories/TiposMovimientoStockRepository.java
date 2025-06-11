package com.projectfinal.greenthumb_backend.repositories;

import com.projectfinal.greenthumb_backend.entities.TiposMovimientoStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TiposMovimientoStockRepository extends JpaRepository<TiposMovimientoStock, Integer> {
    Optional<TiposMovimientoStock> findByDescripcionTipoMovimiento(String descripcionTipoMovimiento);

}
