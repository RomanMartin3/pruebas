package com.projectfinal.greenthumb_backend.repositories;

import com.projectfinal.greenthumb_backend.entities.RegistroBaja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RegistroBajaRepository extends JpaRepository<RegistroBaja, Integer> {
    Optional<RegistroBaja> findByNombreTablaAndRegistroId(String nombreTabla, Integer registroId);
}
