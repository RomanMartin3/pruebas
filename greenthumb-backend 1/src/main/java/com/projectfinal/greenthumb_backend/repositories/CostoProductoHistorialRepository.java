package com.projectfinal.greenthumb_backend.repositories;

import com.projectfinal.greenthumb_backend.entities.CostoProductoHistorial;
import com.projectfinal.greenthumb_backend.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CostoProductoHistorialRepository extends JpaRepository<CostoProductoHistorial, Integer> {
    // Similar a PrecioProductoHistorialRepository si necesitas buscar costos históricos
    @Query("SELECT cph FROM CostoProductoHistorial cph WHERE cph.producto = :producto " +
            "AND cph.fechaInicioVigencia <= :fecha " +
            "AND cph.fechaFinVigencia > :fecha")
    Optional<CostoProductoHistorial> findCostoVigenteEnFecha(
            @Param("producto") Producto producto,
            @Param("fecha") LocalDateTime fecha
    );
}
