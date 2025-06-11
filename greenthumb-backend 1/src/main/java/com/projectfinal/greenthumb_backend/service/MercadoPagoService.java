package com.projectfinal.greenthumb_backend.service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import com.projectfinal.greenthumb_backend.dto.MercadoPagoPreferenceRequestDTO;
import com.projectfinal.greenthumb_backend.dto.MercadoPagoPreferenceResponseDTO;
import com.projectfinal.greenthumb_backend.entities.Pedido;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MercadoPagoService {

    @Value("${mercadopago.access_token}")
    private String accessToken;

    @Value("${mercadopago.client_id}")
    private String clientId;

    @Value("${mercadopago.client_secret}")
    private String clientSecret;

    public MercadoPagoPreferenceResponseDTO createPreference(Pedido pedido) throws MPException, MPApiException {
        MercadoPagoConfig.setAccessToken(accessToken);

        List<PreferenceItemRequest> items = new ArrayList<>();
        pedido.getDetalles().forEach(detalle -> {
            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .id(detalle.getProducto().getProductoId().toString())
                    .title(detalle.getProducto().getNombreProducto())
                    .quantity(detalle.getCantidadComprada())
                    .unitPrice(detalle.getPrecioUnitarioAlComprar())
                    .build();
            items.add(itemRequest);
        });

        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("http://localhost:3000/pago/exitoso") // URL a la que volver si el pago es exitoso
                .failure("http://localhost:3000/pago/fallido")
                .pending("http://localhost:3000/pago/pendiente")
                .build();

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .externalReference(pedido.getPedidoId().toString()) // Asociamos el pago a nuestro ID de Pedido
                .backUrls(backUrls)
                .autoReturn("approved") // Redirige automáticamente en caso de pago aprobado
                .build();

        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);

        return new MercadoPagoPreferenceResponseDTO(preference.getId(), preference.getInitPoint(), preference.getSandboxInitPoint());
    }
}