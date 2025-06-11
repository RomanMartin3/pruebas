package com.projectfinal.greenthumb_backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude; // Importante
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL) // Para no mostrar campos de detalle nulos en el JSON
public class ProductoDetalleDTO {
    private Integer productoId;
    private String nombreProducto;
    private String descripcionGeneral;
    private Integer stockActual;
    private Integer puntoDeReorden;
    private CategoriaDTO categoria;
    private TipoProductoDTO tipoProducto;
    private LocalDateTime fechaAlta;
    private LocalDateTime fechaUltimaModificacion;
    private PrecioProductoActualDTO precioActual;
    // private CostoProductoActualDTO costoActual; // Decidimos si lo exponemos aquí
    private List<ImagenProductoDTO> imagenes;

    // Campos específicos para cada tipo de detalle
    private DetallesPlantaDTO detallesPlanta;
    private DetallesHerramientaDTO detallesHerramienta;
    private DetallesSemillaDTO detallesSemilla;

    public ProductoDetalleDTO() {}

    // Constructor (puedes tener varios o usar setters)
    public ProductoDetalleDTO(Integer productoId, String nombreProducto, String descripcionGeneral,
                              Integer stockActual, Integer puntoDeReorden, CategoriaDTO categoria,
                              TipoProductoDTO tipoProducto, LocalDateTime fechaAlta, LocalDateTime fechaUltimaModificacion,
                              PrecioProductoActualDTO precioActual, List<ImagenProductoDTO> imagenes) {
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.descripcionGeneral = descripcionGeneral;
        this.stockActual = stockActual;
        this.puntoDeReorden = puntoDeReorden;
        this.categoria = categoria;
        this.tipoProducto = tipoProducto;
        this.fechaAlta = fechaAlta;
        this.fechaUltimaModificacion = fechaUltimaModificacion;
        this.precioActual = precioActual;
        this.imagenes = imagenes;
    }

    // Getters y Setters para todos los campos, incluyendo los nuevos de detalles específicos
    public Integer getProductoId() { return productoId; }
    public void setProductoId(Integer productoId) { this.productoId = productoId; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public String getDescripcionGeneral() { return descripcionGeneral; }
    public void setDescripcionGeneral(String descripcionGeneral) { this.descripcionGeneral = descripcionGeneral; }
    public Integer getStockActual() { return stockActual; }
    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }
    public Integer getPuntoDeReorden() { return puntoDeReorden; }
    public void setPuntoDeReorden(Integer puntoDeReorden) { this.puntoDeReorden = puntoDeReorden; }
    public CategoriaDTO getCategoria() { return categoria; }
    public void setCategoria(CategoriaDTO categoria) { this.categoria = categoria; }
    public TipoProductoDTO getTipoProducto() { return tipoProducto; }
    public void setTipoProducto(TipoProductoDTO tipoProducto) { this.tipoProducto = tipoProducto; }
    public LocalDateTime getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDateTime fechaAlta) { this.fechaAlta = fechaAlta; }
    public LocalDateTime getFechaUltimaModificacion() { return fechaUltimaModificacion; }
    public void setFechaUltimaModificacion(LocalDateTime fechaUltimaModificacion) { this.fechaUltimaModificacion = fechaUltimaModificacion; }
    public PrecioProductoActualDTO getPrecioActual() { return precioActual; }
    public void setPrecioActual(PrecioProductoActualDTO precioActual) { this.precioActual = precioActual; }
    public List<ImagenProductoDTO> getImagenes() { return imagenes; }
    public void setImagenes(List<ImagenProductoDTO> imagenes) { this.imagenes = imagenes; }

    public DetallesPlantaDTO getDetallesPlanta() { return detallesPlanta; }
    public void setDetallesPlanta(DetallesPlantaDTO detallesPlanta) { this.detallesPlanta = detallesPlanta; }
    public DetallesHerramientaDTO getDetallesHerramienta() { return detallesHerramienta; }
    public void setDetallesHerramienta(DetallesHerramientaDTO detallesHerramienta) { this.detallesHerramienta = detallesHerramienta; }
    public DetallesSemillaDTO getDetallesSemilla() { return detallesSemilla; }
    public void setDetallesSemilla(DetallesSemillaDTO detallesSemilla) { this.detallesSemilla = detallesSemilla; }
}