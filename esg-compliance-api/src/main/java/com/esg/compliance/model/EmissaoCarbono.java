package com.esg.compliance.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "emissao_carbono")
public class EmissaoCarbono {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    @NotNull(message = "Empresa é obrigatória")
    private Empresa empresa;

    @NotNull(message = "Quantidade de CO2 é obrigatória")
    @Positive(message = "Quantidade deve ser positiva")
    @Column(name = "quantidade_co2", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidadeCo2;

    @Column(nullable = false, length = 20)
    private String unidade = "tCO2e";

    @NotBlank(message = "Fonte é obrigatória")
    @Column(nullable = false, length = 200)
    private String fonte;

    @NotNull(message = "Data de registro é obrigatória")
    @Column(name = "data_registro", nullable = false)
    private LocalDate dataRegistro;

    @NotBlank(message = "Período de referência é obrigatório")
    @Pattern(regexp = "\\d{4}-\\d{2}", message = "Período deve estar no formato YYYY-MM")
    @Column(name = "periodo_ref", nullable = false, length = 7)
    private String periodoRef;

    @Column(nullable = false)
    private Boolean compensado = false;

    @Column(length = 500)
    private String observacoes;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() { this.criadoEm = LocalDateTime.now(); }

    public EmissaoCarbono() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }
    public BigDecimal getQuantidadeCo2() { return quantidadeCo2; }
    public void setQuantidadeCo2(BigDecimal quantidadeCo2) { this.quantidadeCo2 = quantidadeCo2; }
    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }
    public String getFonte() { return fonte; }
    public void setFonte(String fonte) { this.fonte = fonte; }
    public LocalDate getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(LocalDate dataRegistro) { this.dataRegistro = dataRegistro; }
    public String getPeriodoRef() { return periodoRef; }
    public void setPeriodoRef(String periodoRef) { this.periodoRef = periodoRef; }
    public Boolean getCompensado() { return compensado; }
    public void setCompensado(Boolean compensado) { this.compensado = compensado; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}
