package br.com.araujo.rastreabilidade.service.rcarga;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.araujo.rastreabilidade.constates.TipoElementoTela;
import br.com.araujo.rastreabilidade.model.rcarga.dto.ConferenciaDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.comum.CabecalhoDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroBaseDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroTelaDTO;
import br.com.araujo.rastreabilidade.repository.rcarga.dao.CancelamentoConferenciaDAO;
import br.com.araujo.rastreabilidade.service.rcarga.comum.BaseService;

@Service
public class CancelamentoConferenciaService extends BaseService {

	@Autowired
	private CancelamentoConferenciaDAO cancelamentoConferenciaDAO;
	
	public List<ConferenciaDTO> buscaListaCancelamentoConferencia(FiltroBaseDTO filtro) {
		return cancelamentoConferenciaDAO.buscaListaCancelamentoConferencia(filtro);
	}
	
	@Override
	public List<FiltroTelaDTO> montaFiltrosTela() {
		List<FiltroTelaDTO> filtros = new ArrayList<FiltroTelaDTO>();
		
		FiltroTelaDTO rota = new FiltroTelaDTO();
		rota.setTipo(TipoElementoTela.INPUT);
		rota.setNome("rota");
		rota.setDescricao("Rota");
		rota.setCustomCss("numeric");
		rota.setTamanhoMaximo(5);
		filtros.add(rota);
		
		return filtros;
	}

	@Override
	public List<CabecalhoDTO> montaCabecalhos() {
		List<CabecalhoDTO> cabecalhos = new ArrayList<>();
		cabecalhos.add(new CabecalhoDTO("Filial", "text-center filial"));
		cabecalhos.add(new CabecalhoDTO("N°Conferência", "text-center"));
		cabecalhos.add(new CabecalhoDTO("Data Abertura ", "text-center"));
		cabecalhos.add(new CabecalhoDTO("Responsável Abertura", "text-center"));
		cabecalhos.add(new CabecalhoDTO("Responsável Transporte", "text-center"));
		cabecalhos.add(new CabecalhoDTO("Data Cancelamento", "text-center"));
		cabecalhos.add(new CabecalhoDTO("Responsável Cancelamento", "text-center"));
		return cabecalhos;
	}

}
