package com.projectfinal.greenthumb_backend.controller;

import com.projectfinal.greenthumb_backend.entities.NivelesLuz;
import com.projectfinal.greenthumb_backend.entities.FrecuenciasRiego;
import com.projectfinal.greenthumb_backend.repositories.NivelesLuzRepository;
import com.projectfinal.greenthumb_backend.repositories.FrecuenciasRiegoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api") // Puedes usar /api o /api/configuracion según prefieras
@CrossOrigin("*") // Asegúrate de que tu frontend pueda acceder
public class ConfiguracionController {

    private final NivelesLuzRepository nivelesLuzRepository;
    private final FrecuenciasRiegoRepository frecuenciasRiegoRepository;

    @Autowired
    public ConfiguracionController(NivelesLuzRepository nivelesLuzRepository,
                                   FrecuenciasRiegoRepository frecuenciasRiegoRepository) {
        this.nivelesLuzRepository = nivelesLuzRepository;
        this.frecuenciasRiegoRepository = frecuenciasRiegoRepository;
    }

    @GetMapping("/nivelesluz")
    public ResponseEntity<List<NivelesLuz>> getAllNivelesLuz() {
        List<NivelesLuz> niveles = nivelesLuzRepository.findAll();
        if (niveles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(niveles);
    }

    @GetMapping("/frecuenciasriego")
    public ResponseEntity<List<FrecuenciasRiego>> getAllFrecuenciasRiego() {
        List<FrecuenciasRiego> frecuencias = frecuenciasRiegoRepository.findAll();
        if (frecuencias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(frecuencias);
    }
}