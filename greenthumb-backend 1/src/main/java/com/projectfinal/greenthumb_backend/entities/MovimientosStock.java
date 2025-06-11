package com.projectfinal.greenthumb_backend.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "movimientosstock")
public class MovimientosStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MovimientoID")
    private Integer movimientoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductoID", nullable = false)
    private Producto producto;

    @Column(name = "FechaMovimiento", nullable = false, updatable = false)
    private LocalDateTime fechaMovimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TipoMovimientoID", nullable = false)
    private TiposMovimientoStock tipoMovimiento;

    @Column(name = "CantidadAfectada", nullable = false) // Positivo para entradas, negativo para salidas
    private Integer cantidadAfectada;

    @Column(name = "StockPrevio", nullable = false)
    private Integer stockPrevio;

    @Column(name = "StockNuevo", nullable = false)
    private Integer stockNuevo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UsuarioID_Admin", nullable = false)
    private Administrador administrador;

    @Column(name = "Notas", nullable = false, columnDefinition = "TEXT") // Original: NOT NULL. App provee "" si es vacío.
    private String notas;

    // Constructores
    public MovimientosStock() {
    }

    public MovimientosStock(Producto producto, TiposMovimientoStock tipoMovimiento, Integer cantidadAfectada, Integer stockPrevio, Integer stockNuevo,Administrador admin,String notas) {
        this.producto = producto;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidadAfectada = cantidadAfectada;
        this.stockPrevio = stockPrevio;
        this.stockNuevo = stockNuevo;
        this.administrador = admin;
        this.notas = notas;
    }

    @PrePersist
    protected void onPrePersist() {
        this.fechaMovimiento = LocalDateTime.now();
        if (this.notas == null) { // Si Notas es NOT NULL pero puede estar vacía
            this.notas = "";
        }
    }


    // Getters y Setters
    public Integer getMovimientoId() {
        return movimientoId;
    }

    public void setMovimientoId(Integer movimientoId) {
        this.movimientoId = movimientoId;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public LocalDateTime getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(LocalDateTime fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public TiposMovimientoStock getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TiposMovimientoStock tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public Integer getCantidadAfectada() {
        return cantidadAfectada;
    }

    public void setCantidadAfectada(Integer cantidadAfectada) {
        this.cantidadAfectada = cantidadAfectada;
    }

    public Integer getStockPrevio() {
        return stockPrevio;
    }

    public void setStockPrevio(Integer stockPrevio) {
        this.stockPrevio = stockPrevio;
    }

    public Integer getStockNuevo() {
        return stockNuevo;
    }

    public void setStockNuevo(Integer stockNuevo) {
        this.stockNuevo = stockNuevo;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovimientosStock that = (MovimientosStock) o;
        return Objects.equals(movimientoId, that.movimientoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movimientoId);
    }

    @Override
    public String toString() {
        return "MovimientosStock{" +
                "movimientoId=" + movimientoId +
                ", productoId=" + (producto != null ? producto.getProductoId() : "null") +
                ", cantidadAfectada=" + cantidadAfectada +
                ", stockNuevo=" + stockNuevo +
                '}';
    }
}
