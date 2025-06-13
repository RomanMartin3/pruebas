
package com.projectfinal.greenthumb_backend.service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import com.projectfinal.greenthumb_backend.dto.MercadoPagoPreferenceResponseDTO;
import com.projectfinal.greenthumb_backend.entities.Cliente;
import com.projectfinal.greenthumb_backend.entities.DetallesPedido;
import com.projectfinal.greenthumb_backend.entities.Pedido;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class MercadoPagoService {

    @Value("${mercado_pago.access_token}")
    private String accessToken;

    @PostConstruct
    public void init() {
        MercadoPagoConfig.setAccessToken(accessToken);
    }

    public MercadoPagoPreferenceResponseDTO createPreference(Pedido pedido) throws MPException, MPApiException {
        System.out.println("--- LOG (MercadoPagoService): Creando preferencia de pago ---");

        // 1. Convertimos los detalles del pedido en items para Mercado Pago
        List<PreferenceItemRequest> items = new ArrayList<>();
        for (DetallesPedido detalle : pedido.getDetalles()) {
            BigDecimal precioUnitario = detalle.getPrecioUnitarioAlComprar();
            System.out.println("LOG (MercadoPagoService): Añadiendo item a MP: " + detalle.getProducto().getNombreProducto() +
                    ", Cantidad: " + detalle.getCantidadComprada() +
                    ", Precio Unitario: " + precioUnitario);

            if (precioUnitario == null || precioUnitario.compareTo(BigDecimal.ZERO) <= 0) {
                System.err.println("LOG DE ERROR (MercadoPagoService): Omitiendo producto con precio cero o nulo: " + detalle.getProducto().getNombreProducto());
                continue;
            }

            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .id(detalle.getProducto().getProductoId().toString())
                    .title(detalle.getProducto().getNombreProducto())
                    .description("Producto de GreenThumb Market")
                    .quantity(detalle.getCantidadComprada())
                    .currencyId("ARS")
                    .unitPrice(precioUnitario)
                    .build();
            items.add(itemRequest);
        }

        if (items.isEmpty()) {
            System.err.println("LOG DE ERROR (MercadoPagoService): No hay items con precio válido para enviar a Mercado Pago.");
            throw new IllegalStateException("No se pueden procesar pedidos con un valor total de cero.");
        }

        // 2. --- CAMBIO CLAVE: AÑADIMOS DATOS DEL COMPRADOR (PAYER) ---
        Cliente cliente = pedido.getCliente();
        PreferencePayerRequest payer = PreferencePayerRequest.builder()
                .name(cliente.getNombre())
                .surname(cliente.getApellido())
                .email(cliente.getEmail()) // El email es muy importante para MP
                .build();

        // 3. Configuramos las URLs de redirección
        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("http://localhost:3000/payment-success")
                .failure("http://localhost:3000/payment-failure")
                .pending("http://localhost:3000/payment-pending")
                .build();

        // 4. Creamos la preferencia incluyendo al comprador
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .payer(payer)
                .backUrls(backUrls) // Las URLs de redirección son suficientes
                .build();

        PreferenceClient clientMP = new PreferenceClient();
        Preference preference = clientMP.create(preferenceRequest);

        System.out.println("LOG (MercadoPagoService): Preferencia creada con ID: " + preference.getId());
        return new MercadoPagoPreferenceResponseDTO(preference.getId());
    }
}