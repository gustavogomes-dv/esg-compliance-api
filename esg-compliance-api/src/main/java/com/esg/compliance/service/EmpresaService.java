package com.esg.compliance.service;

import com.esg.compliance.exception.*;
import com.esg.compliance.model.Empresa;
import com.esg.compliance.repository.EmpresaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class EmpresaService {

    private final EmpresaRepository repository;

    public EmpresaService(EmpresaRepository repository) {
        this.repository = repository;
    }

    public List<Empresa> listarTodas() { return repository.findAll(); }

    public Empresa buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com id: " + id));
    }

    @Transactional
    public Empresa criar(Empresa empresa) {
        if (repository.existsByCnpj(empresa.getCnpj()))
            throw new BusinessException("Já existe empresa com CNPJ: " + empresa.getCnpj());
        return repository.save(empresa);
    }

    @Transactional
    public Empresa atualizar(Long id, Empresa dados) {
        Empresa empresa = buscarPorId(id);
        if (!empresa.getCnpj().equals(dados.getCnpj()) && repository.existsByCnpj(dados.getCnpj()))
            throw new BusinessException("CNPJ já em uso por outra empresa");
        empresa.setNome(dados.getNome());
        empresa.setCnpj(dados.getCnpj());
        empresa.setSetor(dados.getSetor());
        empresa.setEmail(dados.getEmail());
        empresa.setTelefone(dados.getTelefone());
        empresa.setAtiva(dados.getAtiva());
        return repository.save(empresa);
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Empresa não encontrada com id: " + id);
        repository.deleteById(id);
    }
}
