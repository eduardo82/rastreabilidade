package br.com.araujo.rastreabilidade.service.rcarga;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.araujo.rastreabilidade.model.rcarga.dto.AcompanhamentoCargaDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroAcompanhamentoCargaDTO;
import br.com.araujo.rastreabilidade.repository.rcarga.dao.AcompanhamentoCargaDAO;

@Service
public class AcompanhamentoCargaService {

	@Autowired
	private AcompanhamentoCargaDAO dao;
	
	public List<AcompanhamentoCargaDTO> buscaAcompanhamentoCarga(FiltroAcompanhamentoCargaDTO filtro) throws Exception {
		return dao.buscaAcompanhamentoCarga(filtro);
	}
}
