package com.projectfinal.greenthumb_backend.service;

import com.projectfinal.greenthumb_backend.dto.CategoriaDTO; // Asegúrate de importar el DTO
import com.projectfinal.greenthumb_backend.entities.Administrador;
import com.projectfinal.greenthumb_backend.entities.Categoria;
import com.projectfinal.greenthumb_backend.entities.RegistroBaja;
import com.projectfinal.greenthumb_backend.repositories.AdministradorRepository;
import com.projectfinal.greenthumb_backend.repositories.CategoriaRepository;
import com.projectfinal.greenthumb_backend.repositories.RegistroBajaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final RegistroBajaRepository registroBajaRepository;
    private final AdministradorRepository administradorRepository;

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository,
                            RegistroBajaRepository registroBajaRepository,
                            AdministradorRepository administradorRepository) {
        this.categoriaRepository = categoriaRepository;
        this.registroBajaRepository = registroBajaRepository;
        this.administradorRepository = administradorRepository;
    }

    // Método helper para convertir Entidad a DTO
    private CategoriaDTO convertToDTO(Categoria categoria) {
        return new CategoriaDTO(
                categoria.getCategoriaId(),
                categoria.getNombreCategoria(),
                categoria.getDescripcionCategoria()
        );
    }

    public List<CategoriaDTO> getAllActiveCategoriasDTO() {
        List<Categoria> todas = categoriaRepository.findAll();
        return todas.stream()
                .filter(cat -> registroBajaRepository.findByNombreTablaAndRegistroId("categorias", cat.getCategoriaId()).isEmpty())
                .map(this::convertToDTO) // Usamos el helper
                .collect(Collectors.toList());
    }

    public Optional<CategoriaDTO> getActiveCategoriaByIdDTO(Integer id) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(id);
        if (categoriaOpt.isPresent()) {
            boolean estaDeBaja = registroBajaRepository.findByNombreTablaAndRegistroId("categorias", id).isPresent();
            if (estaDeBaja) {
                return Optional.empty();
            }
            return categoriaOpt.map(this::convertToDTO); // Convertir a DTO
        }
        return Optional.empty();
    }

    public List<CategoriaDTO> searchActiveCategoriasByNombreDTO(String nombre) {
        // Podríamos optimizar esto creando un método en el repositorio que devuelva DTOs
        // o que filtre por nombre en la BD y luego filtramos por activas y convertimos.
        // Por ahora, filtramos en memoria después de obtener todas las activas:
        List<Categoria> activas = categoriaRepository.findAll().stream()
                .filter(cat -> registroBajaRepository.findByNombreTablaAndRegistroId("categorias", cat.getCategoriaId()).isEmpty())
                .toList();

        if (nombre == null || nombre.trim().isEmpty()) {
            return activas.stream().map(this::convertToDTO).collect(Collectors.toList());
        }
        return activas.stream()
                .filter(cat -> cat.getNombreCategoria().toLowerCase().contains(nombre.toLowerCase()))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoriaDTO createCategoria(Categoria categoriaRequest, Integer adminId) {
        // Validar que el nombre de la categoría no esté ya en uso (entre las activas)
        Optional<Categoria> existenteConMismoNombre = categoriaRepository.findByNombreCategoria(categoriaRequest.getNombreCategoria());
        if (existenteConMismoNombre.isPresent()) {
            // Verificar si la encontrada está activa o no
            boolean estaDeBaja = registroBajaRepository.findByNombreTablaAndRegistroId("categorias", existenteConMismoNombre.get().getCategoriaId()).isPresent();
            if (!estaDeBaja) {
                throw new RuntimeException("Ya existe una categoría activa con el nombre: " + categoriaRequest.getNombreCategoria());
            }
            // Si existe pero está de baja, se podría permitir "reactivarla" o crear una nueva.
            // Por ahora, si existe (aunque esté de baja) no permitimos crear otra con el mismo nombre para simplificar.
            // O podrías decidir eliminar el registro de baja y "actualizar" la existente.
            // throw new RuntimeException("Ya existe una categoría (inactiva) con el nombre: " + categoriaRequest.getNombreCategoria());
        }


        Optional<Administrador> adminOpt = administradorRepository.findById(adminId);
        if (adminOpt.isEmpty()) {
            throw new RuntimeException("Administrador no encontrado para crear la categoría.");
        }
        // Creamos la entidad Categoria
        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombreCategoria(categoriaRequest.getNombreCategoria());
        nuevaCategoria.setDescripcionCategoria(categoriaRequest.getDescripcionCategoria());
        // fechaAlta se setea automáticamente por @PrePersist

        Categoria categoriaGuardada = categoriaRepository.save(nuevaCategoria);
        return convertToDTO(categoriaGuardada); // Devolvemos el DTO
    }

    @Transactional
    public Optional<CategoriaDTO> updateCategoria(Integer id, Categoria categoriaDetails, Integer adminId) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(id); // Buscamos por ID directamente

        if (categoriaOpt.isPresent()) {
            // Verificar si está de baja, si es así, no permitir actualización
            if (registroBajaRepository.findByNombreTablaAndRegistroId("categorias", id).isPresent()) {
                throw new RuntimeException("No se puede actualizar una categoría que está dada de baja.");
            }

            Optional<Administrador> adminOpt = administradorRepository.findById(adminId);
            if (adminOpt.isEmpty()) {
                throw new RuntimeException("Administrador no encontrado para actualizar la categoría.");
            }

            // Verificar que el nuevo nombre no entre en conflicto con otra categoría existente y activa
            if (categoriaDetails.getNombreCategoria() != null &&
                    !categoriaDetails.getNombreCategoria().equalsIgnoreCase(categoriaOpt.get().getNombreCategoria())) {
                Optional<Categoria> otraConMismoNombre = categoriaRepository.findByNombreCategoria(categoriaDetails.getNombreCategoria());
                if (otraConMismoNombre.isPresent() && !otraConMismoNombre.get().getCategoriaId().equals(id) &&
                        registroBajaRepository.findByNombreTablaAndRegistroId("categorias", otraConMismoNombre.get().getCategoriaId()).isEmpty()) {
                    throw new RuntimeException("Ya existe otra categoría activa con el nombre: " + categoriaDetails.getNombreCategoria());
                }
            }

            Categoria categoriaExistente = categoriaOpt.get();
            categoriaExistente.setNombreCategoria(categoriaDetails.getNombreCategoria());
            categoriaExistente.setDescripcionCategoria(categoriaDetails.getDescripcionCategoria());

            Categoria categoriaActualizada = categoriaRepository.save(categoriaExistente);
            return Optional.of(convertToDTO(categoriaActualizada));
        }
        return Optional.empty(); // No se encontró la categoría para actualizar
    }

    @Transactional
    public boolean softDeleteCategoria(Integer id, String motivoBaja, Integer adminId) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(id);
        if (categoriaOpt.isPresent()) {
            if (registroBajaRepository.findByNombreTablaAndRegistroId("categorias", id).isPresent()) {
                return true; // Ya está dada de baja
            }

            Optional<Administrador> adminOpt = administradorRepository.findById(adminId);
            if (adminOpt.isEmpty()) {
                throw new RuntimeException("Administrador no encontrado para dar de baja la categoría.");
            }

            RegistroBaja registro = new RegistroBaja();
            registro.setNombreTabla("categorias");
            registro.setRegistroId(id);
            registro.setMotivoBaja(motivoBaja != null ? motivoBaja : "Baja solicitada por administrador.");
            registro.setAdministrador(adminOpt.get());
            registroBajaRepository.save(registro);
            return true;
        }
        return false; // No se encontró la categoría
    }
}