package com.projectfinal.greenthumb_backend.repositories;

import com.projectfinal.greenthumb_backend.entities.PrecioProductoActual;
import com.projectfinal.greenthumb_backend.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface PrecioProductoActualRepository extends JpaRepository<PrecioProductoActual, Integer> {
    Optional<PrecioProductoActual> findByProducto(Producto producto);

}