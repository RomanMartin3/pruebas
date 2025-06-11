package com.projectfinal.greenthumb_backend.repositories;

import com.projectfinal.greenthumb_backend.entities.PrecioProductoHistorial;
import com.projectfinal.greenthumb_backend.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PrecioProductoHistorialRepository extends JpaRepository<PrecioProductoHistorial, Integer> {
    // Para buscar el precio vigente en una fecha específica para un producto
    @Query("SELECT pph FROM PrecioProductoHistorial pph WHERE pph.producto = :producto " +
            "AND pph.fechaInicioVigencia <= :fecha " +
            "AND pph.fechaFinVigencia > :fecha") // Asumiendo que fechaFinVigencia es exclusiva
    Optional<PrecioProductoHistorial> findPrecioVigenteEnFecha(
            @Param("producto") Producto producto,
            @Param("fecha") LocalDateTime fecha
    );
}