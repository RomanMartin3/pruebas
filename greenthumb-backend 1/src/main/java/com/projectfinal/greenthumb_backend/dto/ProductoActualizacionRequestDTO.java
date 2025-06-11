package com.projectfinal.greenthumb_backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL) // Para que los campos no enviados (null) no aparezcan en el JSON
public class ProductoActualizacionRequestDTO {
    private String nombreProducto;
    private String descripcionGeneral;
    private Integer stockActual; // Generalmente, el stock se actualiza con movimientos, pero lo incluimos por si quieres permitirlo aquí.
    private Integer puntoDeReorden;
    private Integer categoriaId;
    private Integer tipoProductoId;
    private BigDecimal nuevoPrecioVenta; // Cambiado el nombre para claridad, o puedes usar "precioVenta"
    private BigDecimal nuevoCosto;       // Cambiado el nombre para claridad, o puedes usar "costo"

    // Para actualizar detalles, el frontend enviaría el objeto completo del detalle que quiere actualizar.
    // Si se cambia el tipo de producto, la lógica de actualización tendría que manejar la eliminación
    // del detalle viejo y la creación del nuevo.
    private DetallesPlantaDTO detallesPlanta;
    private DetallesHerramientaDTO detallesHerramienta;
    private DetallesSemillaDTO detallesSemilla;

    // Constructor vacío
    public ProductoActualizacionRequestDTO() {}

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
    public BigDecimal getNuevoPrecioVenta() { return nuevoPrecioVenta; }
    public void setNuevoPrecioVenta(BigDecimal nuevoPrecioVenta) { this.nuevoPrecioVenta = nuevoPrecioVenta; }
    public BigDecimal getNuevoCosto() { return nuevoCosto; }
    public void setNuevoCosto(BigDecimal nuevoCosto) { this.nuevoCosto = nuevoCosto; }
    public DetallesPlantaDTO getDetallesPlanta() { return detallesPlanta; }
    public void setDetallesPlanta(DetallesPlantaDTO detallesPlanta) { this.detallesPlanta = detallesPlanta; }
    public DetallesHerramientaDTO getDetallesHerramienta() { return detallesHerramienta; }
    public void setDetallesHerramienta(DetallesHerramientaDTO detallesHerramienta) { this.detallesHerramienta = detallesHerramienta; }
    public DetallesSemillaDTO getDetallesSemilla() { return detallesSemilla; }
    public void setDetallesSemilla(DetallesSemillaDTO detallesSemilla) { this.detallesSemilla = detallesSemilla; }
}