package com.projectfinal.greenthumb_backend.repositories;
import com.projectfinal.greenthumb_backend.entities.DetallesPlanta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface DetallesPlantaRepository extends JpaRepository<DetallesPlanta, Integer> {
}
