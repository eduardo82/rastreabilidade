package br.com.araujo.rastreabilidade.service.rcarga.comum;

import java.util.List;

import br.com.araujo.rastreabilidade.model.rcarga.dto.comum.CabecalhoDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroTelaDTO;

public abstract class BaseService {

	public abstract List<FiltroTelaDTO> montaFiltrosTela();
	
	public abstract List<CabecalhoDTO> montaCabecalhos();
}
