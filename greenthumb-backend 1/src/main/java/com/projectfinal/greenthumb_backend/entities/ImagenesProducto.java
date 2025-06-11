package com.projectfinal.greenthumb_backend.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "imagenesproducto")
public class ImagenesProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ImagenID")
    private Integer imagenId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductoID", nullable = false)
    private Producto producto;

    @Column(name = "URLImagen", nullable = false, length = 500)
    private String urlImagen;

    @Column(name = "TextoAlternativo", nullable = false, length = 200) // Original: DEFAULT 'Imagen del producto'
    private String textoAlternativo;

    // Constructores
    public ImagenesProducto() {
    }

    public ImagenesProducto(Producto producto, String urlImagen, String textoAlternativo) {
        this.producto = producto;
        this.urlImagen = urlImagen;
        this.textoAlternativo = textoAlternativo;
    }

    // Getters y Setters
    public Integer getImagenId() {
        return imagenId;
    }

    public void setImagenId(Integer imagenId) {
        this.imagenId = imagenId;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public String getTextoAlternativo() {
        return textoAlternativo;
    }

    public void setTextoAlternativo(String textoAlternativo) {
        this.textoAlternativo = textoAlternativo;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImagenesProducto that = (ImagenesProducto) o;
        return Objects.equals(imagenId, that.imagenId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imagenId);
    }

    @Override
    public String toString() {
        return "ImagenesProducto{" +
                "imagenId=" + imagenId +
                ", urlImagen='" + urlImagen + '\'' +
                ", productoId=" + (producto != null ? producto.getProductoId() : "null") +
                '}';
    }
}
