package com.projectfinal.greenthumb_backend.controller;

import com.projectfinal.greenthumb_backend.dto.*;
import com.projectfinal.greenthumb_backend.entities.Usuario;
import com.projectfinal.greenthumb_backend.service.AuthService;
import com.projectfinal.greenthumb_backend.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final AuthService authService; // 1. Inyectamos AuthService para buscar usuarios

    // Eliminamos la constante de ID hardcodeado
    // private static final Integer DEFAULT_ADMIN_ID = 1; 

    @Autowired
    public ProductoController(ProductoService productoService, AuthService authService) { // 2. Lo añadimos al constructor
        this.productoService = productoService;
        this.authService = authService;
    }

    // --- ENDPOINTS PÚBLICOS (GET) ---
    // Estos métodos no cambian porque son públicos
    @GetMapping
    public ResponseEntity<Page<ProductoListadoDTO>> listarProductos(@PageableDefault(size = 10, page = 0) Pageable pageable, @RequestParam(required = false) Integer categoriaId) {
        return ResponseEntity.ok(productoService.getAllActiveProductos(pageable, categoriaId, null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDetalleDTO> obtenerProductoPorId(@PathVariable Integer id) {
        return productoService.getActiveProductoByIdDTO(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // --- ENDPOINTS PROTEGIDOS (SOLO ADMIN) ---

    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody ProductoCreacionRequestDTO requestDTO, @AuthenticationPrincipal Jwt principal) {
        try {
            // 3. Obtenemos el usuario real desde el token
            Usuario adminUsuario = authService.findUsuarioByAuth0Id(principal.getSubject())
                    .orElseThrow(() -> new RuntimeException("Admin no encontrado"));

            // 4. Pasamos el objeto Usuario al servicio
            ProductoDetalleDTO nuevoProducto = productoService.createProducto(requestDTO, adminUsuario);
            return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Integer id, @RequestBody ProductoActualizacionRequestDTO requestDTO, @AuthenticationPrincipal Jwt principal) {
        try {
            // 3. Obtenemos el usuario real desde el token
            Usuario adminUsuario = authService.findUsuarioByAuth0Id(principal.getSubject())
                    .orElseThrow(() -> new RuntimeException("Admin no encontrado"));

            // 4. Pasamos el objeto Usuario al servicio
            return productoService.updateProductoDTO(id, requestDTO, adminUsuario)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrarProducto(@PathVariable Integer id, @RequestBody(required = false) Map<String, String> payload, @AuthenticationPrincipal Jwt principal) {
        try {
            // 3. Obtenemos el usuario real desde el token
            Usuario adminUsuario = authService.findUsuarioByAuth0Id(principal.getSubject())
                    .orElseThrow(() -> new RuntimeException("Admin no encontrado"));

            String motivoBaja = (payload != null) ? payload.get("motivoBaja") : "Baja desde API";

            // 4. Pasamos el objeto Usuario al servicio
            boolean borradoExitoso = productoService.softDeleteProducto(id, motivoBaja, adminUsuario);

            if (borradoExitoso) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/imagen")
    public ResponseEntity<?> subirImagenProducto(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
        try {
            ImagenProductoDTO imagenDTO = productoService.addImagenToProducto(id, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(imagenDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}