package com.projectfinal.greenthumb_backend.controller;

import com.projectfinal.greenthumb_backend.dto.CategoriaDTO;
import com.projectfinal.greenthumb_backend.entities.Administrador; // Importar Administrador
import com.projectfinal.greenthumb_backend.entities.Categoria;
import com.projectfinal.greenthumb_backend.repositories.AdministradorRepository; // Importar AdministradorRepository
import com.projectfinal.greenthumb_backend.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/categorias")
@CrossOrigin("*")
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final AdministradorRepository administradorRepository; // Inyectar AdministradorRepository

    @Autowired
    public CategoriaController(CategoriaService categoriaService, AdministradorRepository administradorRepository) {
        this.categoriaService = categoriaService;
        this.administradorRepository = administradorRepository;
    }

    // Método auxiliar para obtener el ID del administrador global
    // En un sistema real, el adminId vendría del contexto de seguridad (ej. Spring Security)
    // Para este ejemplo, buscamos al admin por email.
    private Integer getAdminGlobalId() {
        return administradorRepository.findByEmail("admin@greenthumb.com")
                .map(Administrador::getUsuarioId)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado para crear/actualizar la categoría."));
    }

    // GET todas las categorías activas
    // GET /api/categorias
    // GET /api/categorias?nombre=Plantas  (Filtrado por nombre)
    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> listarCategorias(
            @RequestParam(required = false) String nombre) {
        List<CategoriaDTO> categoriasDTO;
        if (nombre != null && !nombre.trim().isEmpty()) {
            categoriasDTO = categoriaService.searchActiveCategoriasByNombreDTO(nombre);
        } else {
            categoriasDTO = categoriaService.getAllActiveCategoriasDTO();
        }

        if (categoriasDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categoriasDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> obtenerCategoriaPorId(@PathVariable Integer id) {
        Optional<CategoriaDTO> categoriaDTO = categoriaService.getActiveCategoriaByIdDTO(id);
        return categoriaDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST para crear una nueva categoría
    // POST /api/categorias
    // Body: {"nombreCategoria": "Nueva Categoria", "descripcionCategoria": "Descripción..."}
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> crearCategoria(@RequestBody Categoria categoriaRequest) {
        try {
            Integer adminId = getAdminGlobalId(); // Obtener el ID del admin dinámicamente
            CategoriaDTO nuevaCategoriaDTO = categoriaService.createCategoria(categoriaRequest, adminId);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoriaDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error al crear la categoria", e.getMessage()));
        }
    }

    // PUT para actualizar una categoría existente
    // PUT /api/categorias/28
    // Body: {"nombreCategoria": "Nombre Actualizado", "descripcionCategoria": "Nueva Descripción"}
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> actualizarCategoria(@PathVariable Integer id, @RequestBody Categoria categoriaDetails) {
        try {
            Integer adminId = getAdminGlobalId(); // Obtener el ID del admin dinámicamente
            Optional<CategoriaDTO> categoriaActualizadaDTO = categoriaService.updateCategoria(id, categoriaDetails, adminId);
            return categoriaActualizadaDTO.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error al actualizar la categoria", e.getMessage()));
        }
    }

    // DELETE para borrado lógico de una categoría
    // DELETE /api/categorias/28
    // Body (opcional): {"motivoBaja": "Categoría descontinuada"}
    @DeleteMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> borrarCategoria(@PathVariable Integer id, @RequestBody(required = false) Map<String, String> payload) {
        try {
            Integer adminId = getAdminGlobalId(); // Obtener el ID del admin dinámicamente
            String motivoBaja = (payload != null) ? payload.get("motivoBaja") : "Baja desde API";
            boolean borradoExitoso = categoriaService.softDeleteCategoria(id, motivoBaja, adminId);
            if (borradoExitoso) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error al eliminar la categoria", e.getMessage()));
        }
    }
}