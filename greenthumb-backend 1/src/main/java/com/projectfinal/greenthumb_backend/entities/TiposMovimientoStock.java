package com.projectfinal.greenthumb_backend.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tiposmovimientostock")
public class TiposMovimientoStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TipoMovimientoID")
    private Integer tipoMovimientoId;

    @Column(name = "DescripcionTipoMovimiento", nullable = false, unique = true, length = 100)
    private String descripcionTipoMovimiento;

    @Column(name = "AfectaStockComo", nullable = false) // ej. 1 para suma, -1 para resta
    private Integer afectaStockComo;

    @OneToMany(mappedBy = "tipoMovimiento", fetch = FetchType.LAZY)
    private List<MovimientosStock> movimientosStock = new ArrayList<>();

    // Constructores
    public TiposMovimientoStock() {
    }

    public TiposMovimientoStock(String descripcionTipoMovimiento, Integer afectaStockComo) {
        this.descripcionTipoMovimiento = descripcionTipoMovimiento;
        this.afectaStockComo = afectaStockComo;
    }

    // Getters y Setters
    public Integer getTipoMovimientoId() {
        return tipoMovimientoId;
    }

    public void setTipoMovimientoId(Integer tipoMovimientoId) {
        this.tipoMovimientoId = tipoMovimientoId;
    }

    public String getDescripcionTipoMovimiento() {
        return descripcionTipoMovimiento;
    }

    public void setDescripcionTipoMovimiento(String descripcionTipoMovimiento) {
        this.descripcionTipoMovimiento = descripcionTipoMovimiento;
    }

    public Integer getAfectaStockComo() {
        return afectaStockComo;
    }

    public void setAfectaStockComo(Integer afectaStockComo) {
        this.afectaStockComo = afectaStockComo;
    }

    public List<MovimientosStock> getMovimientosStock() {
        return movimientosStock;
    }

    public void setMovimientosStock(List<MovimientosStock> movimientosStock) {
        this.movimientosStock = movimientosStock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TiposMovimientoStock that = (TiposMovimientoStock) o;
        return Objects.equals(tipoMovimientoId, that.tipoMovimientoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipoMovimientoId);
    }

    @Override
    public String toString() {
        return "TiposMovimientoStock{" +
                "tipoMovimientoId=" + tipoMovimientoId +
                ", descripcionTipoMovimiento='" + descripcionTipoMovimiento + '\'' +
                '}';
    }
}
