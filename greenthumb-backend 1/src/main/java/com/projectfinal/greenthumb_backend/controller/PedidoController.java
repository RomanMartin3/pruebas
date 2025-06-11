package com.projectfinal.greenthumb_backend.controller;

import com.projectfinal.greenthumb_backend.dto.EstadoPedidoUpdateRequestDTO;
import com.projectfinal.greenthumb_backend.dto.PedidoAdminDetalleDTO;
import com.projectfinal.greenthumb_backend.dto.PedidoAdminListadoDTO;
import com.projectfinal.greenthumb_backend.entities.Usuario;
import com.projectfinal.greenthumb_backend.repositories.AdministradorRepository;
import com.projectfinal.greenthumb_backend.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map; // <-- Importación necesaria para el manejo de errores

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final AdministradorRepository administradorRepository;


    @Autowired
    public PedidoController(PedidoService pedidoService, AdministradorRepository administradorRepository) {
        this.pedidoService = pedidoService;
        this.administradorRepository = administradorRepository;
    }

    @GetMapping
    public ResponseEntity<Page<PedidoAdminListadoDTO>> listarPedidos(Pageable pageable) {
        return ResponseEntity.ok(pedidoService.getAllPedidos(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoAdminDetalleDTO> obtenerPedidoPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(pedidoService.getPedidoById(id));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstadoPedido(
            @PathVariable Integer id,
            @RequestBody EstadoPedidoUpdateRequestDTO request) {
        try {
            // Temporalmente, usamos un ID de admin por defecto.
            final Integer ADMIN_ID = 1;
            Usuario adminUsuario = administradorRepository.findById(ADMIN_ID)
                    .orElseThrow(() -> new RuntimeException("Admin no encontrado"));

            PedidoAdminDetalleDTO pedidoActualizado = pedidoService.updateEstadoPedido(id, request.getNuevoEstado(), request.getNotas(), adminUsuario);
            return ResponseEntity.ok(pedidoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

}