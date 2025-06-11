package com.projectfinal.greenthumb_backend.repositories;

import com.projectfinal.greenthumb_backend.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    Optional<Producto> findByNombreProducto(String nombreProducto);
}
