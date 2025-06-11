package com.projectfinal.greenthumb_backend.dto;

import java.util.List;

public class CarritoVistaDTO {
    private List<CarritoItemDTO> items;
    private int itemCount;
    private double total;

    public CarritoVistaDTO() {}

    public CarritoVistaDTO(List<CarritoItemDTO> items, int itemCount, double total) {
        this.items = items;
        this.itemCount = itemCount;
        this.total = total;
    }

    public List<CarritoItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CarritoItemDTO> items) {
        this.items = items;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}