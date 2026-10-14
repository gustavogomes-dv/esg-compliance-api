package com.esg.compliance.service;

import com.esg.compliance.exception.ResourceNotFoundException;
import com.esg.compliance.model.EmissaoCarbono;
import com.esg.compliance.repository.EmissaoCarbonoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class EmissaoCarbonoService {

    private final EmissaoCarbonoRepository repository;
    private final EmpresaService empresaService;

    public EmissaoCarbonoService(EmissaoCarbonoRepository repository, EmpresaService empresaService) {
        this.repository = repository;
        this.empresaService = empresaService;
    }

    public List<EmissaoCarbono> listarPorEmpresa(Long empresaId) {
        empresaService.buscarPorId(empresaId);
        return repository.findByEmpresaIdOrderByDataRegistroDesc(empresaId);
    }

    public EmissaoCarbono buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emissão não encontrada com id: " + id));
    }

    public BigDecimal totalEmissaoPorEmpresa(Long empresaId) {
        empresaService.buscarPorId(empresaId);
        BigDecimal total = repository.sumTotalEmissaoByEmpresaId(empresaId);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Transactional
    public EmissaoCarbono registrar(Long empresaId, EmissaoCarbono emissao) {
        emissao.setEmpresa(empresaService.buscarPorId(empresaId));
        return repository.save(emissao);
    }

    @Transactional
    public EmissaoCarbono atualizar(Long id, EmissaoCarbono dados) {
        EmissaoCarbono emissao = buscarPorId(id);
        emissao.setQuantidadeCo2(dados.getQuantidadeCo2());
        emissao.setFonte(dados.getFonte());
        emissao.setDataRegistro(dados.getDataRegistro());
        emissao.setPeriodoRef(dados.getPeriodoRef());
        emissao.setCompensado(dados.getCompensado());
        emissao.setObservacoes(dados.getObservacoes());
        return repository.save(emissao);
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Emissão não encontrada com id: " + id);
        repository.deleteById(id);
    }
}
