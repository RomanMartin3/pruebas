// greenthumb-backend/src/main/java/com/projectfinal/greenthumb_backend/service/CarritoService.java
package com.projectfinal.greenthumb_backend.service;

import com.projectfinal.greenthumb_backend.dto.CarritoVistaDTO;
import com.projectfinal.greenthumb_backend.entities.*;

import com.projectfinal.greenthumb_backend.dto.CarritoItemDTO;
import com.projectfinal.greenthumb_backend.dto.PedidoDTO;
import com.projectfinal.greenthumb_backend.dto.DetallePedidoDTO;

import com.projectfinal.greenthumb_backend.repositories.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarritoService {

    @Autowired
    private final CarritoItemRepository carritoItemRepository;
    @Autowired
    private final ClienteRepository clienteRepository;
    @Autowired
    private final ProductoRepository productoRepository;
    @Autowired
    private final PrecioProductoActualRepository precioProductoActualRepository;
    @Autowired
    private final ImagenesProductoRepository imagenesProductoRepository;
    @Autowired
    private final PedidoRepository pedidoRepository;
    @Autowired
    private final DetallesPedidoRepository detallesPedidoRepository;
    @Autowired
    private final EstadosPedidoRepository estadosPedidoRepository;
    @Autowired
    private final MovimientosStockRepository movimientosStockRepository;
    @Autowired
    private final TiposMovimientoStockRepository tiposMovimientoStockRepository;
    @Autowired
    private final AdministradorRepository administradorRepository;
    @Autowired
    private final HistorialEstadosPedidoRepository historialEstadosPedidoRepository;

    @Autowired
    public CarritoService(CarritoItemRepository carritoItemRepository,
                          ClienteRepository clienteRepository,
                          ProductoRepository productoRepository,
                          PrecioProductoActualRepository precioProductoActualRepository,
                          ImagenesProductoRepository imagenesProductoRepository,
                          PedidoRepository pedidoRepository,
                          DetallesPedidoRepository detallesPedidoRepository,
                          EstadosPedidoRepository estadosPedidoRepository,
                          MovimientosStockRepository movimientosStockRepository,
                          TiposMovimientoStockRepository tiposMovimientoStockRepository,
                          AdministradorRepository administradorRepository,
                          HistorialEstadosPedidoRepository historialEstadosPedidoRepository) {
        this.carritoItemRepository = carritoItemRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.precioProductoActualRepository = precioProductoActualRepository;
        this.imagenesProductoRepository = imagenesProductoRepository;
        this.pedidoRepository = pedidoRepository;
        this.detallesPedidoRepository = detallesPedidoRepository;
        this.estadosPedidoRepository = estadosPedidoRepository;
        this.movimientosStockRepository = movimientosStockRepository;
        this.tiposMovimientoStockRepository = tiposMovimientoStockRepository;
        this.administradorRepository = administradorRepository;
        this.historialEstadosPedidoRepository = historialEstadosPedidoRepository;
    }

    // 1. Agregar Producto al Carrito
    @Transactional
    public CarritoItemDTO agregarProductoAlCarrito(Integer clienteId, Integer productoId, Integer cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clienteId));
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productoId));

        // Verificar si el producto tiene stock suficiente
        if (producto.getStockActual() < cantidad) {
            throw new IllegalArgumentException("No hay suficiente stock para el producto: " + producto.getNombreProducto() + ". Stock disponible: " + producto.getStockActual());
        }

        CarritoItemId id = new CarritoItemId(clienteId, productoId);
        Optional<CarritoItem> existingItem = carritoItemRepository.findById(id);

        // Obtener el precio actual del producto al momento de agregarlo
        PrecioProductoActual precioActual = precioProductoActualRepository.findByProducto(producto)
                .orElseThrow(() -> new RuntimeException("Precio actual del producto no encontrado para ID: " + productoId));
        BigDecimal precioAlAgregar = precioActual.getPrecioVenta();

        CarritoItem carritoItem;
        if (existingItem.isPresent()) {
            carritoItem = existingItem.get();
            carritoItem.setCantidad(carritoItem.getCantidad() + cantidad);
            carritoItem.setPrecioAlAgregar(precioAlAgregar);
        } else {
            carritoItem = new CarritoItem(cliente, producto, cantidad, precioAlAgregar);
        }

        CarritoItem savedItem = carritoItemRepository.save(carritoItem);
        return mapToCarritoItemDTO(savedItem);
    }

    // 2. Obtener Carrito de un Cliente
    @Transactional(readOnly = true)
    public List<CarritoItemDTO> obtenerCarritoPorClienteId(Integer clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clienteId));

        List<CarritoItem> carritoItems = carritoItemRepository.findByCliente(cliente);
        return carritoItems.stream()
                .map(this::mapToCarritoItemDTO)
                .collect(Collectors.toList());
    }

    // 3. Actualizar Cantidad de un Producto en el Carrito
    @Transactional
    public CarritoItemDTO actualizarCantidadProductoEnCarrito(Integer clienteId, Integer productoId, Integer nuevaCantidad) {
        if (nuevaCantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa.");
        }
        if (nuevaCantidad == 0) {
            eliminarProductoDelCarrito(clienteId, productoId);
            return null;
        }

        CarritoItemId id = new CarritoItemId(clienteId, productoId);
        CarritoItem carritoItem = carritoItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado en el carrito para el cliente con ID: " + clienteId + " y producto con ID: " + productoId));

        if (carritoItem.getProducto().getStockActual() < nuevaCantidad) {
            throw new IllegalArgumentException("No hay suficiente stock para el producto: " + carritoItem.getProducto().getNombreProducto() + ". Stock disponible: " + carritoItem.getProducto().getStockActual());
        }

        PrecioProductoActual precioActual = precioProductoActualRepository.findByProducto(carritoItem.getProducto())
                .orElseThrow(() -> new RuntimeException("Precio actual del producto no encontrado para ID: " + productoId));
        BigDecimal precioAlActualizar = precioActual.getPrecioVenta();

        carritoItem.setCantidad(nuevaCantidad);
        carritoItem.setPrecioAlAgregar(precioAlActualizar);
        CarritoItem savedItem = carritoItemRepository.save(carritoItem);
        return mapToCarritoItemDTO(savedItem);
    }

    // 4. Eliminar Producto del Carrito
    @Transactional
    public void eliminarProductoDelCarrito(Integer clienteId, Integer productoId) {
        CarritoItemId id = new CarritoItemId(clienteId, productoId);
        if (!carritoItemRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado en el carrito para el cliente con ID: " + clienteId + " y producto con ID: " + productoId);
        }
        carritoItemRepository.deleteById(id);
    }

    // 5. Vaciar Carrito de un Cliente
    @Transactional
    public void vaciarCarrito(Integer clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clienteId));
        carritoItemRepository.deleteByCliente(cliente);
    }

    @Transactional
    public PedidoDTO confirmarCarritoComoPedido(Cliente cliente, String metodoPago, String notasCliente) {
        List<CarritoItem> itemsDelCarrito = carritoItemRepository.findByClienteUsuarioId((long)cliente.getUsuarioId());

        if (itemsDelCarrito.isEmpty()) {
            throw new IllegalStateException("El carrito está vacío, no se puede crear un pedido.");
        }

        EstadosPedido estadoPendiente = estadosPedidoRepository.findByNombreEstado("Pendiente")
                .orElseThrow(() -> new RuntimeException("Estado 'Pendiente' no encontrado."));

        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setCliente(cliente);
        nuevoPedido.setFechaPedido(LocalDateTime.now());
        nuevoPedido.setEstadoPedido(estadoPendiente);
        nuevoPedido.setMetodoPagoSimulado(metodoPago);
        nuevoPedido.setNotasCliente(notasCliente != null ? notasCliente : "");
        nuevoPedido.setNotasAdmin("");

        Pedido pedidoGuardado = pedidoRepository.save(nuevoPedido);

        List<DetallesPedido> detallesParaGuardar = new ArrayList<>();
        for (CarritoItem item : itemsDelCarrito) {
            Producto producto = item.getProducto();
            int cantidadComprada = item.getCantidad();

            if (producto.getStockActual() < cantidadComprada) {
                throw new IllegalStateException("No hay suficiente stock para el producto: " + producto.getNombreProducto());
            }

            // Actualizar stock del producto
            int stockPrevio = producto.getStockActual();
            producto.setStockActual(stockPrevio - cantidadComprada);
            productoRepository.save(producto);

            // Crear detalle de pedido
            DetallesPedido detalle = new DetallesPedido();
            detalle.setPedido(pedidoGuardado);
            detalle.setProducto(producto);
            detalle.setCantidadComprada(cantidadComprada);
            // Guardamos el precio al momento de la compra para mantener un registro histórico fiel.
            detalle.setPrecioUnitarioAlComprar(producto.getPrecioActual().getPrecioVenta());
            detallesParaGuardar.add(detalle);

            // Crear movimiento de stock por venta
            TiposMovimientoStock tipoVenta = tiposMovimientoStockRepository.findByDescripcionTipoMovimiento("Venta Cliente")
                    .orElseThrow(() -> new RuntimeException("Tipo de movimiento 'Venta Cliente' no encontrado."));
            MovimientosStock mov = new MovimientosStock(producto, tipoVenta, -cantidadComprada, stockPrevio, producto.getStockActual(), null, "Venta en pedido #" + pedidoGuardado.getPedidoId());
            movimientosStockRepository.save(mov);
        }

        detallesPedidoRepository.saveAll(detallesParaGuardar);

        // Crear historial de estado inicial
        HistorialEstadosPedido historial = new HistorialEstadosPedido(pedidoGuardado, estadoPendiente, null, "Pedido creado por el cliente.");
        historialEstadosPedidoRepository.save(historial);

        // Vaciar el carrito
        carritoItemRepository.deleteAll(itemsDelCarrito);

        return mapPedidoToDTO(pedidoGuardado); // Usamos un mapper
    }

    public CarritoVistaDTO getCarrito(Long clienteId) {
        List<CarritoItem> itemsEntidad = carritoItemRepository.findByClienteUsuarioId(clienteId);

        List<CarritoItemDTO> itemsDto = itemsEntidad.stream()
                .map(item -> new CarritoItemDTO(
                        item.getProducto().getProductoId(),
                        item.getProducto().getNombreProducto(),
                        item.getCantidad(),
                        item.getPrecioAlAgregar(),
                        item.getProducto().getImagenes().isEmpty() ? null : item.getProducto().getImagenes().get(0).getUrlImagen()
                ))
                .collect(Collectors.toList());

        int itemCount = itemsDto.stream().mapToInt(CarritoItemDTO::getCantidad).sum();

        double total = itemsDto.stream()
                .mapToDouble(item -> item.getPrecioUnitario() * item.getCantidad())
                .sum();

        CarritoVistaDTO carritoVista = new CarritoVistaDTO();
        carritoVista.setItems(itemsDto);
        carritoVista.setItemCount(itemCount);
        carritoVista.setTotal(total);

        return carritoVista;
    }

    // Método auxiliar para mapear CarritoItem a CarritoItemDTO
    private CarritoItemDTO mapToCarritoItemDTO(CarritoItem carritoItem) {
        CarritoItemDTO dto = new CarritoItemDTO();
        dto.setProductoId(carritoItem.getProducto().getProductoId());
        dto.setCantidad(carritoItem.getCantidad());
        dto.setNombreProducto(carritoItem.getProducto().getNombreProducto());

        Optional<PrecioProductoActual> precioActual = precioProductoActualRepository.findByProducto(carritoItem.getProducto());
        precioActual.ifPresent(p -> dto.setPrecioUnitario(p.getPrecioVenta().doubleValue()));

        List<ImagenesProducto> imagenes = carritoItem.getProducto().getImagenes();
        if (imagenes != null && !imagenes.isEmpty()) {
            dto.setImagenProductoUrl(imagenes.get(0).getUrlImagen());
        }

        return dto;
    }

    // Método auxiliar para mapear Pedido a PedidoDTO
    private PedidoDTO mapPedidoToDTO(Pedido pedido) {
        // 1. Mapeamos la lista de entidades DetallesPedido a una lista de DetallePedidoDTO
        List<DetallePedidoDTO> detallesDTO = pedido.getDetalles().stream()
                .map(detalle -> new DetallePedidoDTO(
                        detalle.getProducto().getProductoId(),
                        detalle.getProducto().getNombreProducto(),
                        detalle.getCantidadComprada(),
                        detalle.getPrecioUnitarioAlComprar()
                ))
                .collect(Collectors.toList());

        // 2. Ahora creamos el DTO principal y le pasamos la lista que acabamos de crear
        PedidoDTO dto = new PedidoDTO();
        dto.setPedidoId(pedido.getPedidoId());
        dto.setClienteId(pedido.getCliente().getUsuarioId());
        dto.setNombreCliente(pedido.getCliente().getNombre() + " " + pedido.getCliente().getApellido());
        dto.setFechaPedido(pedido.getFechaPedido());
        dto.setEstadoPedido(pedido.getEstadoPedido().getNombreEstado());
        dto.setMetodoPagoSimulado(pedido.getMetodoPagoSimulado());
        dto.setNotasCliente(pedido.getNotasCliente());
        dto.setDetalles(detallesDTO); // <-- Ahora 'detallesDTO' existe y es válido

        return dto;
    }
}