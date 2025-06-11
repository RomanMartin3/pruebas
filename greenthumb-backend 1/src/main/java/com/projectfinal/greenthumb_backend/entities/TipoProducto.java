package com.projectfinal.greenthumb_backend.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tiposproducto")
public class TipoProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TipoProductoID")
    private Integer tipoProductoId;

    @Column(name = "NombreTipoProducto", nullable = false, unique = true, length = 50)
    private String nombreTipoProducto;

    @Column(name = "TablaDetalleAsociada", nullable = false, length = 50) // Original: DEFAULT ''
    private String tablaDetalleAsociada; // Aplicación debe proveer valor (ej. "" o nombre real)

    @OneToMany(mappedBy = "tipoProducto", fetch = FetchType.LAZY)
    private List<Producto> productos = new ArrayList<>();

    // Constructores
    public TipoProducto() {
    }

    public TipoProducto(String nombreTipoProducto, String tablaDetalleAsociada) {
        this.nombreTipoProducto = nombreTipoProducto;
        this.tablaDetalleAsociada = tablaDetalleAsociada;
    }

    // Getters y Setters
    public Integer getTipoProductoId() {
        return tipoProductoId;
    }

    public void setTipoProductoId(Integer tipoProductoId) {
        this.tipoProductoId = tipoProductoId;
    }

    public String getNombreTipoProducto() {
        return nombreTipoProducto;
    }

    public void setNombreTipoProducto(String nombreTipoProducto) {
        this.nombreTipoProducto = nombreTipoProducto;
    }

    public String getTablaDetalleAsociada() {
        return tablaDetalleAsociada;
    }

    public void setTablaDetalleAsociada(String tablaDetalleAsociada) {
        this.tablaDetalleAsociada = tablaDetalleAsociada;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipoProducto that = (TipoProducto) o;
        return Objects.equals(tipoProductoId, that.tipoProductoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipoProductoId);
    }

    @Override
    public String toString() {
        return "TipoProducto{" +
                "tipoProductoId=" + tipoProductoId +
                ", nombreTipoProducto='" + nombreTipoProducto + '\'' +
                '}';
    }
}
