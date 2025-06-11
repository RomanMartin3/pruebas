package com.projectfinal.greenthumb_backend.repositories;

import com.projectfinal.greenthumb_backend.entities.DetallesSemilla;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface DetallesSemillaRepository extends JpaRepository<DetallesSemilla, Integer> {
}
