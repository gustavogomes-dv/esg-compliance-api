package com.esg.compliance.repository;

import com.esg.compliance.model.LicencaAmbiental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LicencaAmbientalRepository extends JpaRepository<LicencaAmbiental, Long> {

    List<LicencaAmbiental> findByEmpresaId(Long empresaId);

    // Licenças que vencem nos próximos X dias
    @Query("SELECT l FROM LicencaAmbiental l WHERE l.dataValidade BETWEEN :hoje AND :limite AND l.status = 'ATIVA'")
    List<LicencaAmbiental> findLicencasVencendoEm(LocalDate hoje, LocalDate limite);

    boolean existsByNumeroLicenca(String numeroLicenca);
}
