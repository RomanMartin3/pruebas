package com.projectfinal.greenthumb_backend.controller;

import com.projectfinal.greenthumb_backend.dto.ProductoCreacionRequestDTO;
import com.projectfinal.greenthumb_backend.dto.ProductoDetalleDTO;
import com.projectfinal.greenthumb_backend.dto.ProductoListadoDTO;
import com.projectfinal.greenthumb_backend.dto.ProductoActualizacionRequestDTO;
import com.projectfinal.greenthumb_backend.dto.DetallesPlantaDTO; // Importar DetallesPlantaDTO
import com.projectfinal.greenthumb_backend.dto.DetallesHerramientaDTO; // Importar DetallesHerramientaDTO
import com.projectfinal.greenthumb_backend.dto.DetallesSemillaDTO; // Importar DetallesSemillaDTO
import com.projectfinal.greenthumb_backend.entities.Administrador;
import com.projectfinal.greenthumb_backend.repositories.AdministradorRepository;
import com.projectfinal.greenthumb_backend.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin("*")
public class ProductoController {

    private final ProductoService productoService;
    private final AdministradorRepository administradorRepository;

    @Autowired
    public ProductoController(ProductoService productoService, AdministradorRepository administradorRepository) {
        this.productoService = productoService;
        this.administradorRepository = administradorRepository;
    }

    private Integer getAdminGlobalId() {
        return administradorRepository.findByEmail("admin@greenthumb.com")
                .map(Administrador::getUsuarioId)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado para crear/actualizar el producto."));
    }

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

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDetalleDTO> obtenerProductoPorId(@PathVariable Integer id) {
        Optional<ProductoDetalleDTO> productoDTO = productoService.getActiveProductoByIdDTO(id);
        return productoDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> crearProducto(
            @RequestPart("producto") ProductoCreacionRequestDTO requestDTO,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen,
            // --- NUEVOS PARAMETROS PARA DETALLES ESPECIFICOS ---
            @RequestPart(value = "detallesPlanta", required = false) DetallesPlantaDTO detallesPlantaDTO,
            @RequestPart(value = "detallesHerramienta", required = false) DetallesHerramientaDTO detallesHerramientaDTO,
            @RequestPart(value = "detallesSemilla", required = false) DetallesSemillaDTO detallesSemillaDTO
    ) {
        try {
            Integer adminId = getAdminGlobalId();
            // --- CAMBIO EN LA LLAMADA AL SERVICIO: PASAR LOS DETALLES ---
            ProductoDetalleDTO nuevoProductoDTO = productoService.createProducto(
                    requestDTO,
                    imagen,
                    detallesPlantaDTO,
                    detallesHerramientaDTO,
                    detallesSemillaDTO,
                    adminId
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProductoDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno al crear el producto: " + e.getMessage()));
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> actualizarProducto(
            @PathVariable Integer id,
            @RequestPart("producto") ProductoActualizacionRequestDTO requestDTO,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen,
            // --- NUEVOS PARAMETROS PARA DETALLES ESPECIFICOS ---
            @RequestPart(value = "detallesPlanta", required = false) DetallesPlantaDTO detallesPlantaDTO,
            @RequestPart(value = "detallesHerramienta", required = false) DetallesHerramientaDTO detallesHerramientaDTO,
            @RequestPart(value = "detallesSemilla", required = false) DetallesSemillaDTO detallesSemillaDTO
    ) {
        try {
            Integer adminId = getAdminGlobalId();
            // --- CAMBIO EN LA LLAMADA AL SERVICIO: PASAR LOS DETALLES ---
            Optional<ProductoDetalleDTO> productoActualizado = productoService.updateProductoDTO(
                    id,
                    requestDTO,
                    imagen,
                    detallesPlantaDTO,
                    detallesHerramientaDTO,
                    detallesSemillaDTO,
                    adminId
            );
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
            Integer adminId = getAdminGlobalId();
            boolean borradoExitoso = productoService.softDeleteProducto(id, motivoBaja, adminId);
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