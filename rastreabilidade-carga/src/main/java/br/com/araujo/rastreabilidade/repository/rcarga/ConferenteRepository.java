package br.com.araujo.rastreabilidade.repository.rcarga;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.araujo.rastreabilidade.model.rcarga.Conferente;

@Repository
public interface ConferenteRepository extends JpaRepository<Conferente, Integer>{

	@Query("SELECT new br.com.araujo.rastreabilidade.model.rcarga.dto.ConferenteDTO(MAX(chapa) as chapa, nome) "
			+ "FROM Conferente GROUP BY nome ORDER BY nome")
	List<Conferente> buscarConferentes();
	
	@Query("SELECT conferente "
			+ "FROM Conferente conferente ORDER BY conferente.nome")
	List<Conferente> buscarConferentesEdicao();
	
	List<Conferente> findByCodigoFilialOrderByNome(Integer codigoFilial);
	
	List<Conferente> findByCodigoFilialAndNomeContainingOrderByNome(Integer codigoFilial, String nome);
	
	List<Conferente> findByNomeContainingOrderByNome(String nome);
	
	Conferente findFirstByCodigoFilialAndChapa(Integer codigoFilial, String chapa);
	
	@Query("SELECT DISTINCT c.nome FROM Conferente c WHERE c.chapa = ?1 GROUP BY c.nome")
	String findByChapa(String chapa);
}
