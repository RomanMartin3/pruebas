package com.projectfinal.greenthumb_backend.repositories;

import com.projectfinal.greenthumb_backend.entities.ImagenesProducto;
import com.projectfinal.greenthumb_backend.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ImagenesProductoRepository extends JpaRepository<ImagenesProducto, Integer> {
    List<ImagenesProducto> findByProducto(Producto producto);
}
