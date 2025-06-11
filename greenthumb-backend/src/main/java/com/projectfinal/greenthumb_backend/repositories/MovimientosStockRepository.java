package com.projectfinal.greenthumb_backend.repositories;

import com.projectfinal.greenthumb_backend.entities.MovimientosStock;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface MovimientosStockRepository extends JpaRepository<MovimientosStock, Integer> {
}
