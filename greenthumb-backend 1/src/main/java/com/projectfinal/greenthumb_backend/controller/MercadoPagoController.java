// greenthumb-backend/src/main/java/com/projectfinal/greenthumb_backend/controller/MercadoPagoController.java
package com.projectfinal.greenthumb_backend.controller;

import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.projectfinal.greenthumb_backend.dto.MercadoPagoPreferenceRequestDTO;
import com.projectfinal.greenthumb_backend.dto.MercadoPagoPreferenceResponseDTO;
import com.projectfinal.greenthumb_backend.service.MercadoPagoService;
import com.projectfinal.greenthumb_backend.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mercadopago")
public class MercadoPagoController {

    private final PedidoService pedidoService;

    @Autowired
    public MercadoPagoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> receiveWebhook(@RequestBody String payload) {
        System.out.println("Webhook de Mercado Pago recibido: " + payload);
        try {
            JsonObject jsonPayload = JsonParser.parseString(payload).getAsJsonObject();
            String topic = jsonPayload.has("topic") ? jsonPayload.get("topic").getAsString() : jsonPayload.get("type").getAsString();

            // En un futuro, aquí se procesaría el webhook para actualizar el pedido.
            // Por ahora, solo lo registramos.

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }

}