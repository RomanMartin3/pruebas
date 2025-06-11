package com.projectfinal.greenthumb_backend.dto;


import com.projectfinal.greenthumb_backend.dto.DetallesHerramientaDTO;
import com.projectfinal.greenthumb_backend.dto.DetallesPlantaDTO;
import com.projectfinal.greenthumb_backend.dto.DetallesSemillaDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL) // No incluir campos null en el JSON
public class ProductoCreacionRequestDTO {
    private String nombreProducto;
    private String descripcionGeneral;
    private Integer stockActual;
    private Integer puntoDeReorden;
    private Integer categoriaId;       // ID de la Categoria existente
    private Integer tipoProductoId;    // ID del TipoProducto existente
    private BigDecimal precioVenta;    // Para PrecioProductoActual
    private BigDecimal costo;          // Para CostoProductoActual

    // Solo uno de estos DTOs de detalle debería ser enviado desde el frontend
    private DetallesPlantaDTO detallesPlanta;
    private DetallesHerramientaDTO detallesHerramienta;
    private DetallesSemillaDTO detallesSemilla;

    public ProductoCreacionRequestDTO() {
    }

    public ProductoCreacionRequestDTO(String nombreProducto, String descripcionGeneral, Integer stockActual, Integer puntoDeReorden, Integer categoriaId, Integer tipoProductoId, BigDecimal precioVenta, BigDecimal costo, DetallesPlantaDTO detallesPlanta, DetallesHerramientaDTO detallesHerramienta, DetallesSemillaDTO detallesSemilla) {
        this.nombreProducto = nombreProducto;
        this.descripcionGeneral = descripcionGeneral;
        this.stockActual = stockActual;
        this.puntoDeReorden = puntoDeReorden;
        this.categoriaId = categoriaId;
        this.tipoProductoId = tipoProductoId;
        this.precioVenta = precioVenta;
        this.costo = costo;
        this.detallesPlanta = detallesPlanta;
        this.detallesHerramienta = detallesHerramienta;
        this.detallesSemilla = detallesSemilla;
    }

    // Getters y Setters para todos los campos
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public String getDescripcionGeneral() { return descripcionGeneral; }
    public void setDescripcionGeneral(String descripcionGeneral) { this.descripcionGeneral = descripcionGeneral; }
    public Integer getStockActual() { return stockActual; }
    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }
    public Integer getPuntoDeReorden() { return puntoDeReorden; }
    public void setPuntoDeReorden(Integer puntoDeReorden) { this.puntoDeReorden = puntoDeReorden; }
    public Integer getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Integer categoriaId) { this.categoriaId = categoriaId; }
    public Integer getTipoProductoId() { return tipoProductoId; }
    public void setTipoProductoId(Integer tipoProductoId) { this.tipoProductoId = tipoProductoId; }
    public BigDecimal getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; }
    public BigDecimal getCosto() { return costo; }
    public void setCosto(BigDecimal costo) { this.costo = costo; }
    public DetallesPlantaDTO getDetallesPlanta() { return detallesPlanta; }
    public void setDetallesPlanta(DetallesPlantaDTO detallesPlanta) { this.detallesPlanta = detallesPlanta; }
    public DetallesHerramientaDTO getDetallesHerramienta() { return detallesHerramienta; }
    public void setDetallesHerramienta(DetallesHerramientaDTO detallesHerramienta) { this.detallesHerramienta = detallesHerramienta; }
    public DetallesSemillaDTO getDetallesSemilla() { return detallesSemilla; }
    public void setDetallesSemilla(DetallesSemillaDTO detallesSemilla) { this.detallesSemilla = detallesSemilla; }
}