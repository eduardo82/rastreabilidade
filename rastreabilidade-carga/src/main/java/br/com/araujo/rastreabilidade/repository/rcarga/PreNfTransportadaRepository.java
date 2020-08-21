package br.com.araujo.rastreabilidade.repository.rcarga;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.araujo.rastreabilidade.model.rcarga.PreNfTransportada;

@Repository
public interface PreNfTransportadaRepository extends JpaRepository<PreNfTransportada, Integer>{

	PreNfTransportada findByPreNotaFiscal(Integer preNotaFiscal);
	
}
