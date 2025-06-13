package com.projectfinal.greenthumb_backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL) // Para que los campos no enviados (null) no aparezcan en el JSON
public class ProductoActualizacionRequestDTO {
    private String nombreProducto;
    private String descripcionGeneral;
    private Integer stockActual;
    private Integer puntoDeReorden;
    private Integer categoriaId;
    private Integer tipoProductoId;
   private BigDecimal precioVenta; // ANTES: nuevoPrecioVenta
    private BigDecimal costo; 

    // Constructor vacío
    public ProductoActualizacionRequestDTO() {}

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

}