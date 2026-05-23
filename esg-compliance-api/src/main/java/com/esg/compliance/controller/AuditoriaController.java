package com.esg.compliance.controller;

import com.esg.compliance.model.Auditoria;
import com.esg.compliance.service.AuditoriaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AuditoriaController {

    private final AuditoriaService service;

    public AuditoriaController(AuditoriaService service) { this.service = service; }

    @GetMapping("/empresas/{empresaId}/auditorias")
    public ResponseEntity<List<Auditoria>> listar(@PathVariable Long empresaId) {
        return ResponseEntity.ok(service.listarPorEmpresa(empresaId));
    }

    @GetMapping("/auditorias/{id}")
    public ResponseEntity<Auditoria> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping("/empresas/{empresaId}/auditorias")
    public ResponseEntity<Auditoria> registrar(@PathVariable Long empresaId, @RequestBody @Valid Auditoria auditoria) {
        return ResponseEntity.status(201).body(service.registrar(empresaId, auditoria));
    }

    @PutMapping("/auditorias/{id}")
    public ResponseEntity<Auditoria> atualizar(@PathVariable Long id, @RequestBody @Valid Auditoria auditoria) {
        return ResponseEntity.ok(service.atualizar(id, auditoria));
    }

    @DeleteMapping("/auditorias/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
