package com.esg.compliance.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria")
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    @NotNull(message = "Empresa é obrigatória")
    private Empresa empresa;

    @NotBlank(message = "Título é obrigatório")
    @Column(nullable = false, length = 200)
    private String titulo;

    @NotBlank(message = "Tipo é obrigatório")
    @Column(nullable = false, length = 100)
    private String tipo;

    @NotBlank(message = "Auditor é obrigatório")
    @Column(nullable = false, length = 150)
    private String auditor;

    @NotNull(message = "Data da auditoria é obrigatória")
    @Column(name = "data_auditoria", nullable = false)
    private LocalDate dataAuditoria;

    @NotBlank(message = "Resultado é obrigatório")
    @Pattern(regexp = "CONFORME|NAO_CONFORME|PARCIALMENTE_CONFORME",
             message = "Resultado deve ser: CONFORME, NAO_CONFORME ou PARCIALMENTE_CONFORME")
    @Column(nullable = false, length = 20)
    private String resultado;

    @DecimalMin(value = "0.0") @DecimalMax(value = "100.0")
    @Column(precision = 5, scale = 2)
    private BigDecimal score;

    @Column(columnDefinition = "CLOB")
    private String relatorio;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() { this.criadoEm = LocalDateTime.now(); }

    public Auditoria() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getAuditor() { return auditor; }
    public void setAuditor(String auditor) { this.auditor = auditor; }
    public LocalDate getDataAuditoria() { return dataAuditoria; }
    public void setDataAuditoria(LocalDate dataAuditoria) { this.dataAuditoria = dataAuditoria; }
    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }
    public BigDecimal getScore() { return score; }
    public void setScore(BigDecimal score) { this.score = score; }
    public String getRelatorio() { return relatorio; }
    public void setRelatorio(String relatorio) { this.relatorio = relatorio; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}
