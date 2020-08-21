package br.com.araujo.rastreabilidade.repository.rcarga;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.araujo.rastreabilidade.model.rcarga.VwFuncionario;

@Repository
public interface VwFuncionarioRepository extends JpaRepository<VwFuncionario, String>{

	VwFuncionario findFirstByUsuario(String usuario);
}
