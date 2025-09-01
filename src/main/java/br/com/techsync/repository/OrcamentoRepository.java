package br.com.techsync.repository;

import br.com.techsync.dto.OrcamentoStatusResumo;
import br.com.techsync.models.Orcamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Integer> {
    @Query("SELECT new br.com.techsync.dto.OrcamentoStatusResumo(" +
            "TO_CHAR(o.createdAt, 'MM/YYYY'), " +
            "o.status, COUNT(o)) " +
            "FROM Orcamento o " +
            "GROUP BY TO_CHAR(o.createdAt, 'MM/YYYY'), o.status " +
            "ORDER BY MIN(o.createdAt)")
    List<OrcamentoStatusResumo> resumoPorMesEStatus();

}