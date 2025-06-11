
package com.projectfinal.greenthumb_backend.controller;

import com.projectfinal.greenthumb_backend.dto.CarritoItemDTO;
import com.projectfinal.greenthumb_backend.dto.CarritoVistaDTO;
import com.projectfinal.greenthumb_backend.dto.CheckoutRequestDTO;
import com.projectfinal.greenthumb_backend.dto.PedidoDTO;
import com.projectfinal.greenthumb_backend.entities.Cliente;
import com.projectfinal.greenthumb_backend.entities.Usuario;
import com.projectfinal.greenthumb_backend.service.AuthService;
import com.projectfinal.greenthumb_backend.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private AuthService authService;

    @Autowired
    public CarritoController(CarritoService carritoService, AuthService authService) {
        this.carritoService = carritoService;
        this.authService = authService;
    }

    // Endpoint para agregar o actualizar un producto en el carrito
    @PostMapping("/{clienteId}/agregar")
    public ResponseEntity<CarritoItemDTO> agregarOActualizarProductoEnCarrito(
            @PathVariable Integer clienteId, // Cambiado a Integer
            @RequestParam Integer productoId, // Cambiado a Integer
            @RequestParam Integer cantidad) {
        try {
            CarritoItemDTO carritoItem = carritoService.agregarProductoAlCarrito(clienteId, productoId, cantidad);
            return new ResponseEntity<>(carritoItem, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint para obtener el carrito de un cliente
    @GetMapping("/{clienteId}")
    public ResponseEntity<List<CarritoItemDTO>> obtenerCarritoPorCliente(@PathVariable Integer clienteId) { // Cambiado a Integer
        try {
            List<CarritoItemDTO> carritoItems = carritoService.obtenerCarritoPorClienteId(clienteId);
            if (carritoItems.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(carritoItems, HttpStatus.OK);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para actualizar la cantidad de un producto en el carrito
    @PutMapping("/{clienteId}/actualizar")
    public ResponseEntity<CarritoItemDTO> actualizarCantidadProducto(
            @PathVariable Integer clienteId, // Cambiado a Integer
            @RequestParam Integer productoId, // Cambiado a Integer
            @RequestParam Integer nuevaCantidad) {
        try {
            CarritoItemDTO carritoItem = carritoService.actualizarCantidadProductoEnCarrito(clienteId, productoId, nuevaCantidad);
            if (carritoItem == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(carritoItem, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint para eliminar un producto del carrito
    @DeleteMapping("/{clienteId}/eliminar/{productoId}")
    public ResponseEntity<Void> eliminarProductoDelCarrito(
            @PathVariable Integer clienteId, // Cambiado a Integer
            @PathVariable Integer productoId) { // Cambiado a Integer
        try {
            carritoService.eliminarProductoDelCarrito(clienteId, productoId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para vaciar el carrito de un cliente
    @DeleteMapping("/{clienteId}/vaciar")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Integer clienteId) { // Cambiado a Integer
        try {
            carritoService.vaciarCarrito(clienteId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    } 
    
    // Nuevo endpoint para checkout (confirmar carrito como pedido)
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody CheckoutRequestDTO checkoutRequest, @AuthenticationPrincipal Jwt principal) {
        try {
            // Usamos el helper para obtener el cliente de forma segura
            Cliente cliente = getClienteDesdeToken(principal);

            PedidoDTO pedidoConfirmado = carritoService.confirmarCarritoComoPedido(
                    cliente, // Pasamos el objeto Cliente completo
                    checkoutRequest.getMetodoPago(),
                    checkoutRequest.getNotasCliente()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoConfirmado);
        } catch (IllegalStateException e) {
            // Captura errores como "carrito vacío" o "usuario no es cliente"
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            // Captura otros errores inesperados
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno al procesar el pedido."));
        }
    }

    @GetMapping("/items")
    public ResponseEntity<CarritoVistaDTO> getCarritoItems(@AuthenticationPrincipal Jwt principal) {
        String auth0Id = principal.getSubject();
        Usuario usuario = authService.findUsuarioByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con Auth0 ID: " + auth0Id));

        if (!(usuario instanceof Cliente)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        CarritoVistaDTO carrito = carritoService.getCarrito((long) usuario.getUsuarioId());
        return ResponseEntity.ok(carrito);
    }

    private Cliente getClienteDesdeToken(Jwt principal) {
        String auth0Id = principal.getSubject();
        Usuario usuario = authService.findUsuarioByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con Auth0 ID: " + auth0Id));

        if (!(usuario instanceof Cliente)) {
            throw new IllegalStateException("La operación solo puede ser realizada por un cliente.");
        }
        return (Cliente) usuario;
    }

}