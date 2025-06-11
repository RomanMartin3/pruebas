package com.projectfinal.greenthumb_backend.repositories;

import com.projectfinal.greenthumb_backend.entities.CarritoItem;
import com.projectfinal.greenthumb_backend.entities.CarritoItemId; // Importa la clase Id
import com.projectfinal.greenthumb_backend.entities.Cliente;
import com.projectfinal.greenthumb_backend.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface CarritoItemRepository extends JpaRepository<CarritoItem, CarritoItemId> {
    List<CarritoItem> findByCliente(Cliente cliente);
    Optional<CarritoItem> findByClienteAndProducto(Cliente cliente, Producto producto);
    void deleteByCliente(Cliente cliente); // Added for vaciarCarrito
    List<CarritoItem> findByClienteUsuarioId(Long clienteId);
}
