package com.projectfinal.greenthumb_backend.service;

import com.projectfinal.greenthumb_backend.dto.DashboardDTO;
import com.projectfinal.greenthumb_backend.dto.ProductoMasVendidoDTO;
import com.projectfinal.greenthumb_backend.repositories.ClienteRepository;
import com.projectfinal.greenthumb_backend.repositories.DetallesPedidoRepository;
import com.projectfinal.greenthumb_backend.repositories.PedidoRepository;
import com.projectfinal.greenthumb_backend.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DashboardService {

    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;
    private final PedidoRepository pedidoRepository;
    private final DetallesPedidoRepository detallesPedidoRepository;

    @Autowired
    public DashboardService(ProductoRepository productoRepository,
                            ClienteRepository clienteRepository,
                            PedidoRepository pedidoRepository,
                            DetallesPedidoRepository detallesPedidoRepository) {
        this.productoRepository = productoRepository;
        this.clienteRepository = clienteRepository;
        this.pedidoRepository = pedidoRepository;
        this.detallesPedidoRepository = detallesPedidoRepository;
    }

    @Transactional(readOnly = true)
    public DashboardDTO getDashboardMetrics() {
        DashboardDTO dto = new DashboardDTO();

        // 1. Obtener total de productos
        dto.setTotalProductos(productoRepository.count());

        // 2. Obtener total de usuarios/clientes
        dto.setTotalUsuariosActivos(clienteRepository.count());

        // 3. Obtener total de pedidos
        dto.setTotalPedidos(pedidoRepository.count());

        // 4. Obtener Top 5 de productos más vendidos
        List<ProductoMasVendidoDTO> topProducts = detallesPedidoRepository.findTopSellingProducts(PageRequest.of(0, 2));
        dto.setTop5ProductosMasVendidos(topProducts);

        return dto;
    }
}