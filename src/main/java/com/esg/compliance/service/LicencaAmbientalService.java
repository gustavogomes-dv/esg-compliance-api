package com.esg.compliance.service;

import com.esg.compliance.exception.*;
import com.esg.compliance.model.LicencaAmbiental;
import com.esg.compliance.repository.LicencaAmbientalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class LicencaAmbientalService {

    private final LicencaAmbientalRepository repository;
    private final EmpresaService empresaService;

    public LicencaAmbientalService(LicencaAmbientalRepository repository, EmpresaService empresaService) {
        this.repository = repository;
        this.empresaService = empresaService;
    }

    public List<LicencaAmbiental> listarPorEmpresa(Long empresaId) {
        empresaService.buscarPorId(empresaId);
        return repository.findByEmpresaId(empresaId);
    }

    public LicencaAmbiental buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Licença não encontrada com id: " + id));
    }

    public List<LicencaAmbiental> buscarLicencasProximasDoVencimento(int dias) {
        return repository.findLicencasVencendoEm(LocalDate.now(), LocalDate.now().plusDays(dias));
    }

    @Transactional
    public LicencaAmbiental criar(Long empresaId, LicencaAmbiental licenca) {
        if (repository.existsByNumeroLicenca(licenca.getNumeroLicenca()))
            throw new BusinessException("Número de licença já cadastrado: " + licenca.getNumeroLicenca());
        licenca.setEmpresa(empresaService.buscarPorId(empresaId));
        return repository.save(licenca);
    }

    @Transactional
    public LicencaAmbiental atualizar(Long id, LicencaAmbiental dados) {
        LicencaAmbiental licenca = buscarPorId(id);
        licenca.setTipo(dados.getTipo());
        licenca.setOrgaoEmissor(dados.getOrgaoEmissor());
        licenca.setDataValidade(dados.getDataValidade());
        licenca.setStatus(dados.getStatus());
        licenca.setObservacoes(dados.getObservacoes());
        return repository.save(licenca);
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Licença não encontrada com id: " + id);
        repository.deleteById(id);
    }
}
