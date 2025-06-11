package com.projectfinal.greenthumb_backend.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "detallesherramienta")
public class DetallesHerramienta {

    @Id
    @Column(name = "ProductoID") // Esta es la PK y también FK a Producto
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Indica que 'id' usa el mismo valor que la PK de 'producto' y es la FK.
    @JoinColumn(name = "ProductoID")
    private Producto producto;

    @Column(name = "MaterialPrincipal", nullable = false, length = 100) // Original: DEFAULT 'No especificado'
    private String materialPrincipal;

    @Column(name = "Dimensiones", nullable = false, length = 100) // Original: DEFAULT 'No especificadas'
    private String dimensiones;

    @Column(name = "PesoKG", nullable = false, precision = 6, scale = 2) // Original: DEFAULT 0.00
    private BigDecimal pesoKG;

    @Column(name = "UsoRecomendado", nullable = false, columnDefinition = "TEXT")
    private String usoRecomendado;

    @Column(name = "RequiereMantenimiento", nullable = false) // Original: TINYINT(1) DEFAULT 0
    private boolean requiereMantenimiento;

    // Constructores
    public DetallesHerramienta() {
    }

    public DetallesHerramienta(Producto producto, String materialPrincipal, String dimensiones, BigDecimal pesoKG, String usoRecomendado, boolean requiereMantenimiento) {
        this.producto = producto;
        this.materialPrincipal = materialPrincipal;
        this.dimensiones = dimensiones;
        this.pesoKG = pesoKG;
        this.usoRecomendado = usoRecomendado;
        this.requiereMantenimiento = requiereMantenimiento;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        if (producto != null) {
            this.id = producto.getProductoId();
        } else {
            this.id = null;
        }
    }

    public String getMaterialPrincipal() {
        return materialPrincipal;
    }

    public void setMaterialPrincipal(String materialPrincipal) {
        this.materialPrincipal = materialPrincipal;
    }

    public String getDimensiones() {
        return dimensiones;
    }

    public void setDimensiones(String dimensiones) {
        this.dimensiones = dimensiones;
    }

    public BigDecimal getPesoKG() {
        return pesoKG;
    }

    public void setPesoKG(BigDecimal pesoKG) {
        this.pesoKG = pesoKG;
    }

    public String getUsoRecomendado() {
        return usoRecomendado;
    }

    public void setUsoRecomendado(String usoRecomendado) {
        this.usoRecomendado = usoRecomendado;
    }

    public boolean isRequiereMantenimiento() {
        return requiereMantenimiento;
    }

    public void setRequiereMantenimiento(boolean requiereMantenimiento) {
        this.requiereMantenimiento = requiereMantenimiento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetallesHerramienta that = (DetallesHerramienta) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DetallesHerramienta{" +
                "id=" + id +
                ", materialPrincipal='" + materialPrincipal + '\'' +
                '}';
    }
}

