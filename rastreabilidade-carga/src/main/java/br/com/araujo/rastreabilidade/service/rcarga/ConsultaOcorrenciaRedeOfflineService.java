package br.com.araujo.rastreabilidade.service.rcarga;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.araujo.rastreabilidade.model.rcarga.dto.ConsultaOcorrenciaRedeOfflineDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.comum.CabecalhoDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroBaseDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroTelaDTO;
import br.com.araujo.rastreabilidade.repository.rcarga.dao.ConsultaOcorrenciaRedeOfflineDAO;
import br.com.araujo.rastreabilidade.service.rcarga.comum.BaseService;

@Service
public class ConsultaOcorrenciaRedeOfflineService extends BaseService {

	@Autowired
	private ConsultaOcorrenciaRedeOfflineDAO consultaOcorrenciaRedeOfflineDAO;
	
	public List<ConsultaOcorrenciaRedeOfflineDTO> buscaConsultaOcorrenciaRedeOffline(FiltroBaseDTO filtro) {
		return consultaOcorrenciaRedeOfflineDAO.buscaListaCancelamentoConferencia(filtro);
	}

	@Override
	public List<FiltroTelaDTO> montaFiltrosTela() {
		return null;
	}

	@Override
	public List<CabecalhoDTO> montaCabecalhos() {
		List<CabecalhoDTO> cabecalhos = new ArrayList<>();
		cabecalhos.add(new CabecalhoDTO("Data Evento", "text-center"));
		cabecalhos.add(new CabecalhoDTO("Hora Evento", "text-center"));
		cabecalhos.add(new CabecalhoDTO("Filial ", "text-center filial"));
		cabecalhos.add(new CabecalhoDTO("Evento", "text-center"));
		return cabecalhos;
	}

}
