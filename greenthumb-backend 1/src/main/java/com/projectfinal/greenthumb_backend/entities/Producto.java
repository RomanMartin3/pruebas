// greenthumb-backend/src/main/java/com/projectfinal/greenthumb_backend/entities/Producto.java
package com.projectfinal.greenthumb_backend.entities;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set; // Add this import
import java.util.HashSet; // Add this import

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProductoID")
    private Integer productoId;

    @Column(name = "NombreProducto", nullable = false, length = 200)
    private String nombreProducto;

    @Column(name = "DescripcionGeneral", nullable = false, columnDefinition = "TEXT")
    private String descripcionGeneral;

    @Column(name = "StockActual", nullable = false)
    private Integer stockActual = 0;

    @Column(name = "PuntoDeReorden", nullable = false)
    private Integer puntoDeReorden = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CategoriaID", nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TipoProductoID",referencedColumnName = "TipoProductoID" , nullable = false)
    private TipoProducto tipoProducto;

    @Column(name = "FechaAlta", nullable = false, updatable = false)
    private LocalDateTime fechaAlta;

    @Column(name = "FechaUltimaModificacion", nullable = false)
    private LocalDateTime fechaUltimaModificacion;

    @OneToOne(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private PrecioProductoActual precioActual;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PrecioProductoHistorial> historialPreciosVenta = new ArrayList<>();

    // Si implementas costos:
    @OneToOne(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private CostoProductoActual costoActual;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CostoProductoHistorial> historialCostos = new ArrayList<>();

    // Added OneToMany relationship for CarritoItem
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<CarritoItem> carritoItems = new HashSet<>();

    // Added OneToMany relationship for ImagenesProducto
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ImagenesProducto> imagenes = new ArrayList<>();


    // Constructores
    public Producto() {
    }

    public Producto(String nombreProducto, String descripcionGeneral, Integer stockActual, Integer puntoDeReorden, Categoria categoria, TipoProducto tipoProducto) {
        this.nombreProducto = nombreProducto;
        this.descripcionGeneral = descripcionGeneral;
        this.stockActual = stockActual;
        this.puntoDeReorden = puntoDeReorden;
        this.categoria = categoria;
        this.tipoProducto = tipoProducto;
    }

    @PrePersist
    protected void onPrePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.fechaAlta = now;
        this.fechaUltimaModificacion = now;
    }

    @PreUpdate
    protected void onPreUpdate() {
        this.fechaUltimaModificacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescripcionGeneral() {
        return descripcionGeneral;
    }

    public void setDescripcionGeneral(String descripcionGeneral) {
        this.descripcionGeneral = descripcionGeneral;
    }

    public Integer getStockActual() {
        return stockActual;
    }

    public void setStockActual(Integer stockActual) {
        this.stockActual = stockActual;
    }

    public Integer getPuntoDeReorden() {
        return puntoDeReorden;
    }

    public void setPuntoDeReorden(Integer puntoDeReorden) {
        this.puntoDeReorden = puntoDeReorden;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public TipoProducto getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(TipoProducto tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDateTime fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public LocalDateTime getFechaUltimaModificacion() {
        return fechaUltimaModificacion;
    }

    public void setFechaUltimaModificacion(LocalDateTime fechaUltimaModificacion) {
        this.fechaUltimaModificacion = fechaUltimaModificacion;
    }

    public PrecioProductoActual getPrecioActual() {
        return precioActual;
    }

    public void setPrecioActual(PrecioProductoActual precioActual) {
        this.precioActual = precioActual;
        if (precioActual != null) {
            precioActual.setProducto(this); // Mantener la bidireccionalidad
        }
    }

    public List<PrecioProductoHistorial> getHistorialPreciosVenta() {
        return historialPreciosVenta;
    }

    public void setHistorialPreciosVenta(List<PrecioProductoHistorial> historialPreciosVenta) {
        this.historialPreciosVenta = historialPreciosVenta;
    }

    // Métodos helper para historialPreciosVenta
    public void addPrecioVentaHistorial(PrecioProductoHistorial precioHistorial) {
        this.historialPreciosVenta.add(precioHistorial);
        precioHistorial.setProducto(this);
    }

    public void removePrecioVentaHistorial(PrecioProductoHistorial precioHistorial) {
        this.historialPreciosVenta.remove(precioHistorial);
        precioHistorial.setProducto(null);
    }

    // Getters y Setters para costos
    public CostoProductoActual getCostoActual() {
        return costoActual;
    }

    public void setCostoActual(CostoProductoActual costoActual) {
        this.costoActual = costoActual;
        if (costoActual != null) {
            costoActual.setProducto(this);
        }
    }

    public List<CostoProductoHistorial> getHistorialCostos() {
        return historialCostos;
    }

    public void setHistorialCostos(List<CostoProductoHistorial> historialCostos) {
        this.historialCostos = historialCostos;
    }

    // Métodos helper para historialCostos
    public void addCostoHistorial(CostoProductoHistorial costoHistorial) {
        this.historialCostos.add(costoHistorial);
        costoHistorial.setProducto(this);
    }

    public void removeCostoHistorial(CostoProductoHistorial costoHistorial) {
        this.historialCostos.remove(costoHistorial);
        costoHistorial.setProducto(null);
    }

    // Getter y Setter para carritoItems
    public Set<CarritoItem> getCarritoItems() {
        return carritoItems;
    }

    public void setCarritoItems(Set<CarritoItem> carritoItems) {
        this.carritoItems = carritoItems;
    }

    // Getters y Setters para imagenes
    public List<ImagenesProducto> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<ImagenesProducto> imagenes) {
        this.imagenes = imagenes;
    }

    // Métodos de conveniencia para manejar imágenes
    public void addImagen(ImagenesProducto imagen) {
        this.imagenes.add(imagen);
        imagen.setProducto(this);
    }

    public void removeImagen(ImagenesProducto imagen) {
        this.imagenes.remove(imagen);
        imagen.setProducto(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producto)) return false;
        Producto producto = (Producto) o;
        if (productoId == null) {
            return super.equals(o);
        }
        return Objects.equals(getProductoId(), producto.getProductoId());
    }

    @Override
    public int hashCode() {
        return productoId != null ? Objects.hash(getProductoId()) : super.hashCode();
    }

    @Override
    public String toString() {
        return "Producto{" +
                "productoId=" + productoId +
                ", nombreProducto='" + nombreProducto + '\'' +
                ", stockActual=" + stockActual +
                '}';
    }
}