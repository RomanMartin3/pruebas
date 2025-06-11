package com.projectfinal.greenthumb_backend.controller;

import com.projectfinal.greenthumb_backend.dto.CategoriaDTO;
import com.projectfinal.greenthumb_backend.entities.Categoria;
import com.projectfinal.greenthumb_backend.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map; // Para el cuerpo del borrado
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;
    // Temporalmente, para simular adminId. Esto se manejará con Spring Security luego.
    private static final Integer DEFAULT_ADMIN_ID = 10;

    @Autowired
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
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
    @PostMapping
    public ResponseEntity<?> crearCategoria(@RequestBody Categoria categoriaRequest) { // Podría ser CategoriaRequestDTO
        try {
            // Aquí podrías tener un DTO para el request si quieres ser más estricto con los campos de entrada
            // Ejemplo: CategoriaCreacionDTO categoriaParaCrear = new CategoriaCreacionDTO(categoriaRequest.getNombre(), ...);
            CategoriaDTO nuevaCategoriaDTO = categoriaService.createCategoria(categoriaRequest, DEFAULT_ADMIN_ID);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoriaDTO);
        } catch (RuntimeException e) {
            // Devolver el mensaje de error en el cuerpo de la respuesta
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error al crear la categoria", e.getMessage()));
        }
    }
    // PUT para actualizar una categoría existente
    // PUT /api/categorias/28
    // Body: {"nombreCategoria": "Nombre Actualizado", "descripcionCategoria": "Nueva Descripción"}
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCategoria(@PathVariable Integer id, @RequestBody Categoria categoriaDetails) { // Podría ser CategoriaUpdateDTO
        try {
            Optional<CategoriaDTO> categoriaActualizadaDTO = categoriaService.updateCategoria(id, categoriaDetails, DEFAULT_ADMIN_ID);
            return categoriaActualizadaDTO.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error al actualizar la categoria", e.getMessage()));
        }
    }

    // DELETE para borrado lógico de una categoría
    // DELETE /api/categorias/28
    // Body (opcional): {"motivoBaja": "Categoría descontinuada"}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrarCategoria(@PathVariable Integer id, @RequestBody(required = false) Map<String, String> payload) {
        String motivoBaja = (payload != null) ? payload.get("motivoBaja") : "Baja desde API";
        try {
            boolean borradoExitoso = categoriaService.softDeleteCategoria(id, motivoBaja, DEFAULT_ADMIN_ID);
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