package br.com.techsync.repository;

import br.com.techsync.models.Financeiro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinanceiroRepository extends JpaRepository<Financeiro, Integer> {
}
