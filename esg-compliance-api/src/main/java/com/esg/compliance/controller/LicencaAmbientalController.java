package com.esg.compliance.controller;

import com.esg.compliance.model.LicencaAmbiental;
import com.esg.compliance.service.LicencaAmbientalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LicencaAmbientalController {

    private final LicencaAmbientalService service;

    public LicencaAmbientalController(LicencaAmbientalService service) { this.service = service; }

    @GetMapping("/empresas/{empresaId}/licencas")
    public ResponseEntity<List<LicencaAmbiental>> listar(@PathVariable Long empresaId) {
        return ResponseEntity.ok(service.listarPorEmpresa(empresaId));
    }

    @GetMapping("/licencas/{id}")
    public ResponseEntity<LicencaAmbiental> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/licencas/vencendo")
    public ResponseEntity<List<LicencaAmbiental>> vencendo(@RequestParam(defaultValue = "30") int dias) {
        return ResponseEntity.ok(service.buscarLicencasProximasDoVencimento(dias));
    }

    @PostMapping("/empresas/{empresaId}/licencas")
    public ResponseEntity<LicencaAmbiental> criar(@PathVariable Long empresaId, @RequestBody @Valid LicencaAmbiental licenca) {
        return ResponseEntity.status(201).body(service.criar(empresaId, licenca));
    }

    @PutMapping("/licencas/{id}")
    public ResponseEntity<LicencaAmbiental> atualizar(@PathVariable Long id, @RequestBody @Valid LicencaAmbiental licenca) {
        return ResponseEntity.ok(service.atualizar(id, licenca));
    }

    @DeleteMapping("/licencas/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
