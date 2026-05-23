package com.esg.compliance.repository;

import com.esg.compliance.model.EmissaoCarbono;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface EmissaoCarbonoRepository extends JpaRepository<EmissaoCarbono, Long> {

    List<EmissaoCarbono> findByEmpresaIdOrderByDataRegistroDesc(Long empresaId);

    List<EmissaoCarbono> findByEmpresaIdAndPeriodoRef(Long empresaId, String periodoRef);

    @Query("SELECT SUM(e.quantidadeCo2) FROM EmissaoCarbono e WHERE e.empresa.id = :empresaId")
    BigDecimal sumTotalEmissaoByEmpresaId(Long empresaId);
}
