
package com.projectfinal.greenthumb_backend.controller;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.projectfinal.greenthumb_backend.dto.CarritoItemDTO;
import com.projectfinal.greenthumb_backend.dto.CheckoutRequestDTO;
import com.projectfinal.greenthumb_backend.dto.MercadoPagoPreferenceResponseDTO;
import com.projectfinal.greenthumb_backend.dto.PedidoDTO;
import com.projectfinal.greenthumb_backend.repositories.ClienteRepository;
import com.projectfinal.greenthumb_backend.entities.Cliente;
import com.projectfinal.greenthumb_backend.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/carrito")
@CrossOrigin("*")
public class CarritoController {

    private final CarritoService carritoService;
    private final ClienteRepository clienteRepository;

    @Autowired
    public CarritoController(CarritoService carritoService, ClienteRepository clienteRepository) {
        this.carritoService = carritoService;
        this.clienteRepository = clienteRepository;
    }

    // Endpoint para agregar o actualizar un producto en el carrito
    @PostMapping("/{clienteId}/agregar")
    public ResponseEntity<CarritoItemDTO> agregarOActualizarProductoEnCarrito(
            @PathVariable Integer clienteId,
            @RequestParam Integer productoId,
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
    public ResponseEntity<List<CarritoItemDTO>> obtenerCarritoPorCliente(@PathVariable Integer clienteId) {
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
    public ResponseEntity<?> checkout(@RequestBody CheckoutRequestDTO checkoutRequest) {
        try {
            Cliente cliente = clienteRepository.findById(checkoutRequest.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + checkoutRequest.getClienteId()));

            MercadoPagoPreferenceResponseDTO preference = carritoService.confirmarCarritoComoPedido(
                    cliente,
                    checkoutRequest.getMetodoPago(),
                    checkoutRequest.getNotasCliente()
            );

            // Si todo va bien, devolvemos la preferencia
            return ResponseEntity.status(HttpStatus.CREATED).body(preference);

            // --- MANEJO DE ERRORES MEJORADO ---

        } catch (MPApiException e) {
            // Este catch es específico para errores de la API de Mercado Pago
            // Aquí sí podemos usar getApiResponse() para ver el error real.
            System.err.println("Error de API de Mercado Pago. Respuesta completa: " + e.getApiResponse().getContent());
            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of(
                            "error", "Error al comunicarse con el servicio de pagos.",
                            "details", e.getApiResponse().getContent()
                    ));

        } catch (MPException e) {
            // Este catch es para otros errores del SDK de Mercado Pago (ej. configuración)
            System.err.println("Error del SDK de Mercado Pago: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno con el servicio de pagos.", "details", e.getMessage()));

        } catch (IllegalStateException e) {
            // Para errores de lógica de negocio (carrito vacío, sin stock, etc.)
            System.err.println("Error de estado ilegal: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));

        } catch (Exception e) {
            // Para cualquier otro error inesperado
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Ocurrió un error inesperado en el servidor.", "details", e.getMessage()));
        }
    }

}