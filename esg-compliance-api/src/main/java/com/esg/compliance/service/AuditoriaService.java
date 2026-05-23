package com.esg.compliance.service;

import com.esg.compliance.exception.ResourceNotFoundException;
import com.esg.compliance.model.Auditoria;
import com.esg.compliance.repository.AuditoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AuditoriaService {

    private final AuditoriaRepository repository;
    private final EmpresaService empresaService;

    public AuditoriaService(AuditoriaRepository repository, EmpresaService empresaService) {
        this.repository = repository;
        this.empresaService = empresaService;
    }

    public List<Auditoria> listarPorEmpresa(Long empresaId) {
        empresaService.buscarPorId(empresaId);
        return repository.findByEmpresaIdOrderByDataAuditoriaDesc(empresaId);
    }

    public Auditoria buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auditoria não encontrada com id: " + id));
    }

    @Transactional
    public Auditoria registrar(Long empresaId, Auditoria auditoria) {
        auditoria.setEmpresa(empresaService.buscarPorId(empresaId));
        return repository.save(auditoria);
    }

    @Transactional
    public Auditoria atualizar(Long id, Auditoria dados) {
        Auditoria auditoria = buscarPorId(id);
        auditoria.setTitulo(dados.getTitulo());
        auditoria.setTipo(dados.getTipo());
        auditoria.setAuditor(dados.getAuditor());
        auditoria.setDataAuditoria(dados.getDataAuditoria());
        auditoria.setResultado(dados.getResultado());
        auditoria.setScore(dados.getScore());
        auditoria.setRelatorio(dados.getRelatorio());
        return repository.save(auditoria);
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Auditoria não encontrada com id: " + id);
        repository.deleteById(id);
    }
}
