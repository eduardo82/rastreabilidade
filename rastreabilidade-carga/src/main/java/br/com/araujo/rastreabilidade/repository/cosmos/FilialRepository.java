package br.com.araujo.rastreabilidade.repository.cosmos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.araujo.rastreabilidade.model.cosmos.Filial;

@Repository
public interface FilialRepository extends JpaRepository<Filial, Integer> {

}
