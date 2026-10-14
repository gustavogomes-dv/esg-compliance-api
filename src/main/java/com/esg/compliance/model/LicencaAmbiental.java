package com.esg.compliance.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "licenca_ambiental")
public class LicencaAmbiental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    @NotNull(message = "Empresa é obrigatória")
    private Empresa empresa;

    @NotBlank(message = "Número da licença é obrigatório")
    @Column(name = "numero_licenca", nullable = false, unique = true, length = 50)
    private String numeroLicenca;

    @NotBlank(message = "Tipo é obrigatório")
    @Column(nullable = false, length = 100)
    private String tipo;

    @NotBlank(message = "Órgão emissor é obrigatório")
    @Column(name = "orgao_emissor", nullable = false, length = 150)
    private String orgaoEmissor;

    @NotNull(message = "Data de emissão é obrigatória")
    @Column(name = "data_emissao", nullable = false)
    private LocalDate dataEmissao;

    @NotNull(message = "Data de validade é obrigatória")
    @Column(name = "data_validade", nullable = false)
    private LocalDate dataValidade;

    @Column(nullable = false, length = 20)
    private String status = "ATIVA";

    @Column(length = 500)
    private String observacoes;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() { this.criadoEm = LocalDateTime.now(); }

    public LicencaAmbiental() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }
    public String getNumeroLicenca() { return numeroLicenca; }
    public void setNumeroLicenca(String numeroLicenca) { this.numeroLicenca = numeroLicenca; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getOrgaoEmissor() { return orgaoEmissor; }
    public void setOrgaoEmissor(String orgaoEmissor) { this.orgaoEmissor = orgaoEmissor; }
    public LocalDate getDataEmissao() { return dataEmissao; }
    public void setDataEmissao(LocalDate dataEmissao) { this.dataEmissao = dataEmissao; }
    public LocalDate getDataValidade() { return dataValidade; }
    public void setDataValidade(LocalDate dataValidade) { this.dataValidade = dataValidade; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}
