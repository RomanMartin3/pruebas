package com.projectfinal.greenthumb_backend.repositories;

import com.projectfinal.greenthumb_backend.entities.HistorialEstadosPedido;
import com.projectfinal.greenthumb_backend.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistorialEstadosPedidoRepository extends JpaRepository<HistorialEstadosPedido, Integer> {
    List<HistorialEstadosPedido> findByPedidoOrderByFechaCambioAsc(Pedido pedido);
}
