package com.projectfinal.greenthumb_backend.controller;

import com.projectfinal.greenthumb_backend.dto.ProductoCreacionRequestDTO;
import com.projectfinal.greenthumb_backend.dto.ProductoDetalleDTO;
import com.projectfinal.greenthumb_backend.dto.ProductoListadoDTO;
import com.projectfinal.greenthumb_backend.dto.ProductoActualizacionRequestDTO;
import com.projectfinal.greenthumb_backend.entities.Producto; // Para el request body
import com.projectfinal.greenthumb_backend.service.ProductoService;
import org.springframework.http.HttpStatus; // Importar HttpStatus
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos") // Simplificado, como hiciste con categorías
@CrossOrigin("*")
public class ProductoController {

    private final ProductoService productoService;
    private static final Integer DEFAULT_ADMIN_ID = 1; // Temporal


    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // GET /api/productos?page=0&size=10&sort=nombreProducto,asc&categoriaId=1&nombre=Helecho
    @GetMapping
    public ResponseEntity<Page<ProductoListadoDTO>> listarProductos(
            @PageableDefault(size = 10, sort = "nombreProducto") Pageable pageable,
            @RequestParam(required = false) Integer categoriaId,
            @RequestParam(required = false) String nombre) {
        Page<ProductoListadoDTO> productos = productoService.getAllActiveProductos(pageable, categoriaId, nombre);
        if (productos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
    }

    // GET /api/productos/1
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDetalleDTO> obtenerProductoPorId(@PathVariable Integer id) {
        Optional<ProductoDetalleDTO> productoDTO = productoService.getActiveProductoByIdDTO(id);
        return productoDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody ProductoCreacionRequestDTO requestDTO) {
        // El ID del administrador debería obtenerse del contexto de seguridad una vez implementado
        // Por ahora, usamos un ID por defecto si es necesario para las pruebas iniciales.
        // El DEFAULT_ADMIN_ID ya está definido en tu clase.
        try {
            ProductoDetalleDTO nuevoProductoDTO = productoService.createProducto(requestDTO, DEFAULT_ADMIN_ID);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProductoDTO);
        } catch (IllegalArgumentException e) {
            // Errores de validación de datos de entrada
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            // Otros errores, como "Categoría no encontrada" o problemas de base de datos
            // Es buena idea loggear la excepción completa en el servidor para depuración
            e.printStackTrace(); // Log para el desarrollador
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno al crear el producto: " + e.getMessage()));
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Integer id, @RequestBody ProductoActualizacionRequestDTO requestDTO) {
        try {
            Optional<ProductoDetalleDTO> productoActualizado = productoService.updateProductoDTO(id, requestDTO, DEFAULT_ADMIN_ID);

            return productoActualizado.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error al actualizar el producto: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrarProducto(@PathVariable Integer id, @RequestBody(required = false) Map<String, String> payload) {
        String motivoBaja = (payload != null) ? payload.get("motivoBaja") : "Baja desde API";
        try {
            boolean borradoExitoso = productoService.softDeleteProducto(id, motivoBaja, DEFAULT_ADMIN_ID);
            if (borradoExitoso) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
}