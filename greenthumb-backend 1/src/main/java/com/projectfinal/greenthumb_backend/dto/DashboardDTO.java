package com.projectfinal.greenthumb_backend.dto;

import java.util.List;

public class DashboardDTO {
    private long totalProductos;
    private long totalUsuariosActivos;
    private long totalPedidos;
    private List<ProductoMasVendidoDTO> top5ProductosMasVendidos;

    // Getters y Setters
    public long getTotalProductos() { return totalProductos; }
    public void setTotalProductos(long totalProductos) { this.totalProductos = totalProductos; }
    public long getTotalUsuariosActivos() { return totalUsuariosActivos; }
    public void setTotalUsuariosActivos(long totalUsuariosActivos) { this.totalUsuariosActivos = totalUsuariosActivos; }
    public long getTotalPedidos() { return totalPedidos; }
    public void setTotalPedidos(long totalPedidos) { this.totalPedidos = totalPedidos; }
    public List<ProductoMasVendidoDTO> getTop5ProductosMasVendidos() { return top5ProductosMasVendidos; }
    public void setTop5ProductosMasVendidos(List<ProductoMasVendidoDTO> top5ProductosMasVendidos) { this.top5ProductosMasVendidos = top5ProductosMasVendidos; }
}