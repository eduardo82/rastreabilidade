package br.com.araujo.rastreabilidade.repository.rcarga;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.araujo.rastreabilidade.model.rcarga.ConferenciaCarga;

@Repository
public interface FechamentoConferenciaRepository extends JpaRepository<ConferenciaCarga, Integer>{

}
