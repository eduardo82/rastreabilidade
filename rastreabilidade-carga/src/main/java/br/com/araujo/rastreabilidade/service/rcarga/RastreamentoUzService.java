package br.com.araujo.rastreabilidade.service.rcarga;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.araujo.rastreabilidade.constates.TipoElementoTela;
import br.com.araujo.rastreabilidade.model.rcarga.dto.FiltroRastreamentoUzDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.RastreamentoUzDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.comum.CabecalhoDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroTelaDTO;
import br.com.araujo.rastreabilidade.repository.rcarga.dao.RastreamentoUzDAO;
import br.com.araujo.rastreabilidade.service.rcarga.comum.BaseService;

@Service
public class RastreamentoUzService extends BaseService {

	@Autowired
	private RastreamentoUzDAO rastreamentoUzDAO;
	
	public List<RastreamentoUzDTO> buscaListaRastreamentoUz(FiltroRastreamentoUzDTO filtro) {
		return rastreamentoUzDAO.buscaRastreamentoUz(filtro);
	}
	
	@Override
	public List<FiltroTelaDTO> montaFiltrosTela() {
		List<FiltroTelaDTO> filtros = new ArrayList<FiltroTelaDTO>();
		
		FiltroTelaDTO uz = new FiltroTelaDTO();
		uz.setTipo(TipoElementoTela.INPUT);
		uz.setNome("uz");
		uz.setDescricao("UZ");
		uz.setRequerido("required");
		uz.setTamanhoMaximo(20);
		filtros.add(uz);
		
		return filtros;
	}

	@Override
	public List<CabecalhoDTO> montaCabecalhos() {
		List<CabecalhoDTO> cabecalhos = new ArrayList<>();
		cabecalhos.add(new CabecalhoDTO("Data/Hora", "text-center"));
		cabecalhos.add(new CabecalhoDTO("Filial", "text-center filial"));
		cabecalhos.add(new CabecalhoDTO("Situação", "text-center"));
		return cabecalhos;

	}

}
