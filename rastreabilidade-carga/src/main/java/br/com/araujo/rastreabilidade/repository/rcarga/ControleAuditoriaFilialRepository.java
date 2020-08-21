package br.com.araujo.rastreabilidade.repository.rcarga;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.araujo.rastreabilidade.model.rcarga.ControleAuditoriaFilial;

@Repository
public interface ControleAuditoriaFilialRepository extends JpaRepository<ControleAuditoriaFilial, Integer> {

	ControleAuditoriaFilial findFirstByFilial(Integer filial);
	
	long countByFilial(Integer filial);
}
