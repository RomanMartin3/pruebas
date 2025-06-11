package com.projectfinal.greenthumb_backend.repositories;

import com.projectfinal.greenthumb_backend.dto.ProductoMasVendidoDTO;
import com.projectfinal.greenthumb_backend.entities.DetallesPedido;
import com.projectfinal.greenthumb_backend.entities.Pedido;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DetallesPedidoRepository extends JpaRepository<DetallesPedido, Integer> {
    List<DetallesPedido> findByPedido(Pedido pedido);

    // AÑADIR ESTE MÉTODO CON LA CONSULTA
    @Query("SELECT new com.projectfinal.greenthumb_backend.dto.ProductoMasVendidoDTO(dp.producto.productoId, dp.producto.nombreProducto, SUM(dp.cantidadComprada)) " +
            "FROM DetallesPedido dp " +
            "GROUP BY dp.producto.productoId, dp.producto.nombreProducto " +
            "ORDER BY SUM(dp.cantidadComprada) DESC")
    List<ProductoMasVendidoDTO> findTopSellingProducts(Pageable pageable);

}