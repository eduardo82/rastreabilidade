package br.com.araujo.rastreabilidade.repository.rcarga;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.araujo.rastreabilidade.model.rcarga.ConferenciaCarga;

@Repository
public interface ConferenciaCargaRepository extends JpaRepository<ConferenciaCarga, Integer> {

	@Query("select distinct obj " +
				"from ConferenciaCarga obj " +
				"left outer join obj.preNfTransportada prnf " +
				"where obj.flagStatus is not null " +
				"and obj.flagStatus not in ('A' , 'C') " + //ABERTA OU CANCELADA
				"and obj.dataFechamentoParcial is not null " +
				"and prnf.filial = :filial " +
				"and obj.dataAbertura >= :dataAberturaInicial " +
				"and obj.dataAbertura < :dataAberturaFinal " +
				"order by obj.dataAbertura")
	List<ConferenciaCarga> buscaConferenciaPorFilialDataAbertura(@Param("filial") Integer filial, @Param("dataAberturaInicial") Date dataAberturaInicial
			, @Param("dataAberturaFinal") Date dataAberturaFinal);
	
	@Query("select obj " +
			"from ConferenciaCarga obj " +
			"left outer join obj.preNfTransportada prnf " +
			"where obj.flagStatus is not null " +
			"and obj.flagStatus not in ('A' , 'C') " + //ABERTA OU CANCELADA
			"and obj.dataFechamentoParcial is not null " +
			"and prnf.filial = :filial " +
			"order by obj.dataAbertura")
	List<ConferenciaCarga> buscaConferenciaPorFilial(@Param("filial") Integer filial);
}
