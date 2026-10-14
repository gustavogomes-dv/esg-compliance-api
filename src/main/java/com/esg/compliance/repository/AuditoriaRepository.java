package com.esg.compliance.repository;

import com.esg.compliance.model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
    List<Auditoria> findByEmpresaIdOrderByDataAuditoriaDesc(Long empresaId);
}
