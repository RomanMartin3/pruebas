// greenthumb-backend/src/main/java/com/projectfinal/greenthumb_backend/service/CarritoService.java
package com.projectfinal.greenthumb_backend.service;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.projectfinal.greenthumb_backend.dto.MercadoPagoPreferenceResponseDTO;
import com.projectfinal.greenthumb_backend.entities.CarritoItem;
import com.projectfinal.greenthumb_backend.entities.CarritoItemId;
import com.projectfinal.greenthumb_backend.entities.Cliente;
import com.projectfinal.greenthumb_backend.entities.Producto;
import com.projectfinal.greenthumb_backend.entities.PrecioProductoActual;
import com.projectfinal.greenthumb_backend.entities.ImagenesProducto;
import com.projectfinal.greenthumb_backend.entities.Pedido;
import com.projectfinal.greenthumb_backend.entities.DetallesPedido;
import com.projectfinal.greenthumb_backend.entities.EstadosPedido;
import com.projectfinal.greenthumb_backend.entities.MovimientosStock;
import com.projectfinal.greenthumb_backend.entities.TiposMovimientoStock;
import com.projectfinal.greenthumb_backend.entities.Administrador;

import com.projectfinal.greenthumb_backend.dto.CarritoItemDTO;
import com.projectfinal.greenthumb_backend.dto.PedidoDTO;
import com.projectfinal.greenthumb_backend.dto.DetallePedidoDTO;

import com.projectfinal.greenthumb_backend.repositories.CarritoItemRepository;
import com.projectfinal.greenthumb_backend.repositories.ClienteRepository;
import com.projectfinal.greenthumb_backend.repositories.ProductoRepository;
import com.projectfinal.greenthumb_backend.repositories.PrecioProductoActualRepository;
import com.projectfinal.greenthumb_backend.repositories.ImagenesProductoRepository;
import com.projectfinal.greenthumb_backend.repositories.PedidoRepository;
import com.projectfinal.greenthumb_backend.repositories.DetallesPedidoRepository;
import com.projectfinal.greenthumb_backend.repositories.EstadosPedidoRepository;
import com.projectfinal.greenthumb_backend.repositories.MovimientosStockRepository;
import com.projectfinal.greenthumb_backend.repositories.TiposMovimientoStockRepository;
import com.projectfinal.greenthumb_backend.repositories.AdministradorRepository;


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

    private final CarritoItemRepository carritoItemRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final PrecioProductoActualRepository precioProductoActualRepository;
    private final ImagenesProductoRepository imagenesProductoRepository;
    private final PedidoRepository pedidoRepository;
    private final DetallesPedidoRepository detallesPedidoRepository;
    private final EstadosPedidoRepository estadosPedidoRepository;
    private final MovimientosStockRepository movimientosStockRepository;
    private final TiposMovimientoStockRepository tiposMovimientoStockRepository;
    private final AdministradorRepository administradorRepository;
    private final MercadoPagoService mercadoPagoService;




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
                          MercadoPagoService mercadoPagoService) {
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
        this.mercadoPagoService = mercadoPagoService;
    }

    // 1. Agregar Producto al Carrito
    @Transactional
    public CarritoItemDTO agregarProductoAlCarrito(Integer clienteId, Integer productoId, Integer cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        System.out.println("--- LOG: Iniciando agregarProductoAlCarrito ---");
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

        System.out.println("LOG: Producto ID " + productoId + " tiene un precio de: " + precioAlAgregar); // <-- LOG CLAVE


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
        System.out.println("--- LOG: Obteniendo carrito para cliente ID: " + clienteId + " ---");
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

    // Nuevo método para confirmar el carrito como un pedido
    @Transactional
    public MercadoPagoPreferenceResponseDTO confirmarCarritoComoPedido(Cliente cliente, String metodoPago, String notasCliente) throws MPException, MPApiException {
        System.out.println("--- LOG: Iniciando confirmarCarritoComoPedido para cliente ID: " + cliente.getUsuarioId() + " ---");
        List<CarritoItem> itemsDelCarrito = carritoItemRepository.findByClienteUsuarioId( cliente.getUsuarioId());

        if (itemsDelCarrito.isEmpty()) {
            throw new IllegalStateException("El carrito está vacío, no se puede crear un pedido.");
        }
        System.out.println("LOG: Se encontraron " + itemsDelCarrito.size() + " items en el carrito.");

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
            System.out.println("LOG: Procesando item de pedido: Producto ID " + item.getProducto().getProductoId() +
                    ", Cantidad: " + item.getCantidad() +
                    ", Precio guardado en carrito: " + item.getPrecioAlAgregar());
            Producto producto = item.getProducto();
            int cantidadComprada = item.getCantidad();

            if (producto.getStockActual() < cantidadComprada) {
                throw new IllegalStateException("No hay suficiente stock para el producto: " + producto.getNombreProducto());
            }

            producto.setStockActual(producto.getStockActual() - cantidadComprada);
            productoRepository.save(producto);

            DetallesPedido detalle = new DetallesPedido();
            detalle.setPedido(pedidoGuardado);
            detalle.setProducto(producto);
            detalle.setCantidadComprada(cantidadComprada);
            // Ahora este método existe en la entidad DetallesPedido
            detalle.setPrecioUnitarioAlComprar(item.getPrecioAlAgregar());
            detallesParaGuardar.add(detalle);
        }

        detallesPedidoRepository.saveAll(detallesParaGuardar);

        pedidoGuardado.setDetalles(detallesParaGuardar);

        // Vaciar el carrito
        carritoItemRepository.deleteAll(itemsDelCarrito);

        System.out.println("LOG: Pasando el pedido al servicio de Mercado Pago...");
        // CORRECCIÓN: El método ahora devuelve la preferencia de MercadoPago.
        return mercadoPagoService.createPreference(pedidoGuardado);
    }


    // Método auxiliar para mapear CarritoItem a CarritoItemDTO
    private CarritoItemDTO mapToCarritoItemDTO(CarritoItem carritoItem) {
        // Obtenemos el producto desde el item del carrito
        Producto producto = carritoItem.getProducto();

        CarritoItemDTO dto = new CarritoItemDTO();
        dto.setClienteId(carritoItem.getCliente().getUsuarioId());
        dto.setProductoId(producto.getProductoId());
        dto.setCantidad(carritoItem.getCantidad());
        dto.setNombreProducto(producto.getNombreProducto());

        Optional<PrecioProductoActual> precioActual = precioProductoActualRepository.findByProducto(producto);
        precioActual.ifPresent(p ->{
            System.out.println("LOG (mapToCarritoItemDTO): Mapeando producto ID " + producto.getProductoId() + " con precio: " + p.getPrecioVenta());
         dto.setPrecioUnitario(p.getPrecioVenta().doubleValue());
        });

        String imageUrl = null;
        if (producto.getImagenes() != null && !producto.getImagenes().isEmpty()) {
            imageUrl = producto.getImagenes().get(0).getUrlImagen();
        }
        dto.setImagenProductoUrl(imageUrl);

        return dto;
    }

    // Método auxiliar para mapear Pedido a PedidoDTO
    private PedidoDTO mapPedidoToDTO(Pedido pedido, List<DetallePedidoDTO> detallesDTO) {
        PedidoDTO dto = new PedidoDTO();
        dto.setPedidoId(pedido.getPedidoId());
        dto.setClienteId(pedido.getCliente().getUsuarioId());
        dto.setNombreCliente(pedido.getCliente().getNombre() + " " + pedido.getCliente().getApellido());
        dto.setFechaPedido(pedido.getFechaPedido());
        dto.setEstadoPedido(pedido.getEstadoPedido().getNombreEstado());
        dto.setMetodoPagoSimulado(pedido.getMetodoPagoSimulado());
        dto.setNotasCliente(pedido.getNotasCliente());
        dto.setDetalles(detallesDTO);
        return dto;
    }
}