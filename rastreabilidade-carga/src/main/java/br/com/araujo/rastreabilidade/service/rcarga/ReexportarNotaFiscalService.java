package br.com.araujo.rastreabilidade.service.rcarga;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.araujo.rastreabilidade.constates.TipoElementoTela;
import br.com.araujo.rastreabilidade.model.rcarga.dto.PreNfTransportadaDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.comum.CabecalhoDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroTelaDTO;
import br.com.araujo.rastreabilidade.repository.rcarga.ReexportarNotaFiscalRepository;
import br.com.araujo.rastreabilidade.service.rcarga.comum.BaseService;
import br.com.araujo.rastreabilidade.utils.DateUtils;

@Service
public class ReexportarNotaFiscalService extends BaseService {
	
	@Autowired
	private ReexportarNotaFiscalRepository reexportarNotaFiscalRepository;
	
	public List<PreNfTransportadaDTO> pesquisaPreNotaTransportada(Long idFilial, Date dataRota) throws ParseException {
		List<PreNfTransportadaDTO> preNotas = new ArrayList<>(); 
		List<Object[]> lista = reexportarNotaFiscalRepository.pesquisaPreNotaTransportada(idFilial, dataRota);
		
		for (Object[] preNota : lista) {
			PreNfTransportadaDTO notaVO = new PreNfTransportadaDTO();
			notaVO.setId((Integer) preNota[0]);
			notaVO.setDataExportacao(DateUtils.format((Date) preNota[1], DateUtils.FORMATO_DATA));
			notaVO.setNomeUsuario((String) preNota[2]);
			
			preNotas.add(notaVO);
		}
		
		return preNotas;
	}
	
	public List<FiltroTelaDTO> montaFiltrosTela() {
		List<FiltroTelaDTO> filtros = new ArrayList<FiltroTelaDTO>();
		
		FiltroTelaDTO dataRota = new FiltroTelaDTO();
		dataRota.setTipo(TipoElementoTela.DATE);
		dataRota.setNome("data-rota");
		dataRota.setDescricao("Data da Rota");	
		dataRota.setRequerido("required");
		dataRota.setValorPadrao(DateUtils.formatDiaAtualFormatoWeb());
		filtros.add(dataRota);
		return filtros;
	}
	
	public List<CabecalhoDTO> montaCabecalhos() {
		List<CabecalhoDTO> cabecalhos = new ArrayList<>();
		cabecalhos.add(new CabecalhoDTO("Nota Fiscal", "text-center"));
		cabecalhos.add(new CabecalhoDTO("Data", "text-center"));
		cabecalhos.add(new CabecalhoDTO("Nome Usuário", "text-center"));
		return cabecalhos;
	}
	
	public void atualizaPrenota(HttpServletRequest request) {
		List<Integer> ids = request.getParameterMap().keySet().stream()
				.map(chave -> Integer.valueOf(chave))
				.collect(Collectors.toList());
		
		reexportarNotaFiscalRepository.atualizaPreNFTransportadaReexportar(ids);
	}
}
