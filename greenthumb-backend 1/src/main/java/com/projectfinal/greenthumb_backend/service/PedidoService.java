package com.projectfinal.greenthumb_backend.service;

import com.projectfinal.greenthumb_backend.dto.*;
import com.projectfinal.greenthumb_backend.entities.*;
import com.projectfinal.greenthumb_backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final AdministradorRepository administradorRepository;
    private final EstadosPedidoRepository estadosPedidoRepository;
    private final HistorialEstadosPedidoRepository historialEstadosPedidoRepository;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository,
                         AdministradorRepository administradorRepository,
                         EstadosPedidoRepository estadosPedidoRepository,
                         HistorialEstadosPedidoRepository historialEstadosPedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.administradorRepository = administradorRepository;
        this.estadosPedidoRepository = estadosPedidoRepository;
        this.historialEstadosPedidoRepository = historialEstadosPedidoRepository;
    }

    @Transactional(readOnly = true)
    public Page<PedidoAdminListadoDTO> getAllPedidos(Pageable pageable) {
        return pedidoRepository.findAll(pageable)
                .map(this::mapToAdminListadoDTO);
    }

    @Transactional(readOnly = true)
    public PedidoAdminDetalleDTO getPedidoById(Integer pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + pedidoId));
        return mapToAdminDetalleDTO(pedido);
    }

    @Transactional
    public PedidoAdminDetalleDTO updateEstadoPedido(Integer pedidoId, String nuevoEstadoNombre, String notas, Usuario adminUsuario) {
        if (!(adminUsuario instanceof Administrador)) {
            throw new IllegalStateException("Solo un administrador puede cambiar el estado de un pedido.");
        }
        Administrador admin = (Administrador) adminUsuario;

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + pedidoId));

        EstadosPedido nuevoEstado = estadosPedidoRepository.findByNombreEstado(nuevoEstadoNombre)
                .orElseThrow(() -> new RuntimeException("Estado de pedido no válido: " + nuevoEstadoNombre));

        pedido.setEstadoPedido(nuevoEstado);

        HistorialEstadosPedido historial = new HistorialEstadosPedido(pedido, nuevoEstado, admin, notas);
        historialEstadosPedidoRepository.save(historial);

        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        return mapToAdminDetalleDTO(pedidoGuardado);
    }


    // --- Mappers a DTOs ---

    private PedidoAdminListadoDTO mapToAdminListadoDTO(Pedido pedido) {
        String nombreCompleto = pedido.getCliente().getNombre() + " " + pedido.getCliente().getApellido();
        return new PedidoAdminListadoDTO(
                pedido.getPedidoId(),
                pedido.getFechaPedido(),
                nombreCompleto,
                pedido.getEstadoPedido().getNombreEstado(),
                pedido.getMetodoPagoSimulado()
        );
    }

    private PedidoAdminDetalleDTO mapToAdminDetalleDTO(Pedido pedido) {
        PedidoAdminDetalleDTO dto = new PedidoAdminDetalleDTO();
        Cliente cliente = pedido.getCliente();

        dto.setPedidoId(pedido.getPedidoId());
        dto.setFechaPedido(pedido.getFechaPedido());
        dto.setEstadoActual(pedido.getEstadoPedido().getNombreEstado());

        dto.setClienteId(cliente.getUsuarioId());
        dto.setNombreCliente(cliente.getNombre() + " " + cliente.getApellido());
        dto.setEmailCliente(cliente.getEmail());
        dto.setTelefonoCliente(cliente.getTelefono());
        dto.setDireccionCliente(String.format("%s %s, %s, %s", cliente.getCalle(), cliente.getNumero(), cliente.getCiudad(), cliente.getProvincia()));

        dto.setMetodoPago(pedido.getMetodoPagoSimulado());
        dto.setNotasCliente(pedido.getNotasCliente());
        dto.setNotasAdmin(pedido.getNotasAdmin());

        // Mapear detalles del pedido
        List<DetallePedidoDTO> detallesDTO = pedido.getDetalles().stream().map(detalle ->
                new DetallePedidoDTO(
                        detalle.getProducto().getProductoId(),
                        detalle.getProducto().getNombreProducto(),
                        detalle.getCantidadComprada(),
                        // Aquí asumimos que el precio está en el producto. Para un sistema real, el precio debería guardarse en DetallesPedido.
                        detalle.getProducto().getPrecioActual() != null ? detalle.getProducto().getPrecioActual().getPrecioVenta() : null
                )).collect(Collectors.toList());
        dto.setDetalles(detallesDTO);

        // Mapear historial de estados
        List<HistorialEstadoDTO> historialDTO = historialEstadosPedidoRepository.findByPedidoOrderByFechaCambioAsc(pedido)
                .stream().map(historial -> new HistorialEstadoDTO(
                        historial.getEstadoPedido().getNombreEstado(),
                        historial.getFechaCambio(),
                        historial.getAdministrador() != null ? historial.getAdministrador().getEmail() : "Sistema",
                        historial.getNotas()
                )).collect(Collectors.toList());
        dto.setHistorialEstados(historialDTO);

        return dto;
    }
}