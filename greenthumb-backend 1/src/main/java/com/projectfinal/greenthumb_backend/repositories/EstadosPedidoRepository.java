package com.projectfinal.greenthumb_backend.repositories;

import com.projectfinal.greenthumb_backend.entities.EstadosPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EstadosPedidoRepository extends JpaRepository<EstadosPedido, Integer> {
    Optional<EstadosPedido> findByNombreEstado(String nombreEstado);
}