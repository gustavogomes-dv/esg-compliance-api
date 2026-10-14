package com.esg.compliance.controller;

import com.esg.compliance.model.EmissaoCarbono;
import com.esg.compliance.service.EmissaoCarbonoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EmissaoCarbonoController {

    private final EmissaoCarbonoService service;

    public EmissaoCarbonoController(EmissaoCarbonoService service) { this.service = service; }

    @GetMapping("/empresas/{empresaId}/emissoes")
    public ResponseEntity<List<EmissaoCarbono>> listar(@PathVariable Long empresaId) {
        return ResponseEntity.ok(service.listarPorEmpresa(empresaId));
    }

    @GetMapping("/emissoes/{id}")
    public ResponseEntity<EmissaoCarbono> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/empresas/{empresaId}/emissoes/total")
    public ResponseEntity<TotalEmissaoResponse> total(@PathVariable Long empresaId) {
        return ResponseEntity.ok(new TotalEmissaoResponse(empresaId, service.totalEmissaoPorEmpresa(empresaId), "tCO2e"));
    }

    @PostMapping("/empresas/{empresaId}/emissoes")
    public ResponseEntity<EmissaoCarbono> registrar(@PathVariable Long empresaId, @RequestBody @Valid EmissaoCarbono emissao) {
        return ResponseEntity.status(201).body(service.registrar(empresaId, emissao));
    }

    @PutMapping("/emissoes/{id}")
    public ResponseEntity<EmissaoCarbono> atualizar(@PathVariable Long id, @RequestBody @Valid EmissaoCarbono emissao) {
        return ResponseEntity.ok(service.atualizar(id, emissao));
    }

    @DeleteMapping("/emissoes/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    public record TotalEmissaoResponse(Long empresaId, BigDecimal totalCo2, String unidade) {}
}
