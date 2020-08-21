package br.com.araujo.rastreabilidade.service.rcarga;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.araujo.rastreabilidade.constates.TipoElementoTela;
import br.com.araujo.rastreabilidade.model.rcarga.dto.EntregaNaoRealizadaDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.comum.CabecalhoDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroEntregaNaoRealizadaDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroTelaDTO;
import br.com.araujo.rastreabilidade.repository.rcarga.dao.ProdutoTransportadoDAO;
import br.com.araujo.rastreabilidade.repository.rcarga.dao.UnitizadorTransportadoDAO;
import br.com.araujo.rastreabilidade.service.rcarga.comum.BaseService;
import br.com.araujo.rastreabilidade.utils.DateUtils;

@Service
public class EntregaNaoRealizadaService extends BaseService {
	
	@Autowired
	private UnitizadorTransportadoDAO unitizadorTransportadoDAO;
	
	@Autowired
	private ProdutoTransportadoDAO produtoTransportadoDAO;
	
	public List<EntregaNaoRealizadaDTO> buscaEntregasNaoRealizadas(FiltroEntregaNaoRealizadaDTO filtro) {		
		List<EntregaNaoRealizadaDTO> lista = new ArrayList<>();
		
		Date dataRota = DateUtils.parseOrNull(filtro.getDataRota(), DateUtils.FORMATO_DATA_WEB);
		
		lista.addAll(unitizadorTransportadoDAO.buscaEntregaNaoRealizadasUz(dataRota, filtro.getRota()));
		lista.addAll(produtoTransportadoDAO.buscaEntregaNaoRealizadasVolumosos(dataRota, filtro.getRota()));
		
		lista.sort(new Comparator<EntregaNaoRealizadaDTO>() {
			public int compare(EntregaNaoRealizadaDTO o1, EntregaNaoRealizadaDTO o2) {
				
				if (o1.getRotaAcompanhamento().equals(o2.getRotaAcompanhamento())) {
					if (o1.getCodigoFilial().equals(o2.getCodigoFilial())) {
						if (o1.getTipoTransportado().equals(o2.getTipoTransportado())) {
							return o1.getDescricaoTransportado().compareTo(o2.getDescricaoTransportado());
						}
						else {
							return ("UZ".equals(o1.getTipoTransportado()) ? -1 : 1 );
						}
					}
					else {
						return o1.getCodigoFilial().compareTo(o2.getCodigoFilial());
					}
				}
				else {
					return o1.getRotaAcompanhamento().compareTo(o2.getRotaAcompanhamento());
				}
			}
		});
		
		return lista;
	}
	

	@Override
	public List<FiltroTelaDTO> montaFiltrosTela() {
		List<FiltroTelaDTO> filtros = new ArrayList<FiltroTelaDTO>();
		
		FiltroTelaDTO dataRota = new FiltroTelaDTO();
		dataRota.setTipo(TipoElementoTela.DATE);
		dataRota.setNome("dataRota");
		dataRota.setDescricao("Data da Rota");	
		dataRota.setRequerido("required");
		dataRota.setValorPadrao(DateUtils.formatDiaAtualFormatoWeb());
		
		filtros.add(dataRota);
		
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
		cabecalhos.add(new CabecalhoDTO("Rota", "text-center"));
		cabecalhos.add(new CabecalhoDTO("Filial", "text-center filial"));
		cabecalhos.add(new CabecalhoDTO("Tipo (UZ/Volumoso)", "text-center"));
		cabecalhos.add(new CabecalhoDTO("UZ / Volumoso (Descrição)", "text-center"));
		cabecalhos.add(new CabecalhoDTO("Embalagem", "text-center"));
		cabecalhos.add(new CabecalhoDTO("Quantidade Unitária", "text-center"));
		return cabecalhos;
	}
}
