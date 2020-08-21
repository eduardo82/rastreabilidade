package br.com.araujo.rastreabilidade.service.rcarga;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.PrintException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.araujo.rastreabilidade.constates.AppConstantes;
import br.com.araujo.rastreabilidade.constates.TipoElementoTela;
import br.com.araujo.rastreabilidade.constates.TipoImpressao;
import br.com.araujo.rastreabilidade.constates.TipoSituacao;
import br.com.araujo.rastreabilidade.model.rcarga.ConferenciaCarga;
import br.com.araujo.rastreabilidade.model.rcarga.dto.ConferenciaDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.comum.CabecalhoDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.comum.OpcaoDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroConferenciaDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroTelaDTO;
import br.com.araujo.rastreabilidade.repository.rcarga.ConferenciaCargaRepository;
import br.com.araujo.rastreabilidade.repository.rcarga.dao.FechamentoConferenciaDAO;
import br.com.araujo.rastreabilidade.service.GeradorRelatorioBirt;
import br.com.araujo.rastreabilidade.service.rcarga.comum.BaseService;
import br.com.araujo.rastreabilidade.utils.DateUtils;
import br.com.araujo.rastreabilidade.utils.InformacaoUsuarioUtils;

@Service
public class FechamentoConferenciaService extends BaseService {

	@Value("${server.nome.relatorio.fechamento.conferencia}")
	private String nomeRelatorio;

	@Autowired
	private FechamentoConferenciaDAO fechamentoConferenciaDAO;
	
	@Autowired
	private ConferenciaCargaRepository conferenciaCargaRepository;

	public List<ConferenciaDTO> buscaConferencias(HttpServletRequest request, FiltroConferenciaDTO filtro) {
		List<ConferenciaDTO> resultados = fechamentoConferenciaDAO.buscaConferencias(filtro);
		resultados.stream().forEach(item -> {
			item.setDataAberturaView(DateUtils.format(item.getDataAbertura()));
			item.setDataFechamentoView(DateUtils.format(item.getDataFechamento()));
			item.setDataFechamentoParcialView(DateUtils.format(item.getDataFechamentoParcial()));
			
			if (item.getDataAbertura() == null && item.getDataFechamento() == null
					&& item.getDataFechamentoParcial() == null) {
				item.setSituacaoView("ABERTO");
			} else if (item.getDataAbertura() != null && item.getDataFechamento() != null) {
				item.setSituacaoView("FECHADO");
			} else if (item.getDataAbertura() != null && item.getDataFechamentoParcial() != null
					&& item.getDataFechamento() == null) {
				item.setSituacaoView("FECHADO PARCIALMENTE");
			} else {
				item.setSituacaoView("");
			}

			if (item.getDataAbertura() != null && item.getDataFechamento() == null && item.getDataImpressao() == null
					&& item.getDataReimpressao() == null) {
				item.setTipoImpressaoView("VERIFICAÇÃO");
			} else if (item.getDataAbertura() != null && item.getDataFechamento() != null
					&& item.getDataImpressao() != null && item.getDataReimpressao() == null) {
				item.setTipoImpressaoView("IMPRESSÃO");
			} else if (item.getDataAbertura() != null && item.getDataFechamento() != null
					&& item.getDataImpressao() != null && item.getDataReimpressao() != null) {
				item.setTipoImpressaoView("REIMPRESSÃO");
			} else {
				item.setTipoImpressaoView("");
			}

			item.setNomeFilial(filtro.getDescricaoFilial());
		});

		request.getSession().setAttribute("conferencias", resultados);
		return resultados;
	}

	public List<FiltroTelaDTO> montaFiltrosTela() {
		List<FiltroTelaDTO> filtros = new ArrayList<FiltroTelaDTO>();

		FiltroTelaDTO situacao = new FiltroTelaDTO();
		situacao.setTipo(TipoElementoTela.SELECT);
		situacao.setNome("situacao");
		situacao.setDescricao("Situação");
		situacao.setValorPadrao(TipoSituacao.AMBOS.getSigla());
		situacao.setOpcoes(montaListaOpcoesSituacao());
		filtros.add(situacao);

		FiltroTelaDTO tipoImpressao = new FiltroTelaDTO();
		tipoImpressao.setTipo(TipoElementoTela.SELECT);
		tipoImpressao.setNome("tipoImpressao");
		tipoImpressao.setDescricao("Tipo de Impressão");
		tipoImpressao.setValorPadrao(TipoImpressao.NAO_IMPRESSO.getSigla());
		tipoImpressao.setOpcoes(montaListaOpcoesTipoImpressao());
		tipoImpressao.setCustomCss("mb-4");
		filtros.add(tipoImpressao);

		FiltroTelaDTO dataAberturaInicial = new FiltroTelaDTO();
		dataAberturaInicial.setTipo(TipoElementoTela.DATE);
		dataAberturaInicial.setNome("dataInicial");
		dataAberturaInicial.setDescricao("Data Abertura Inicial");
		dataAberturaInicial.setRequerido("required");
		dataAberturaInicial.setValorPadrao(DateUtils.formatDiaAtualFormatoWeb());
		filtros.add(dataAberturaInicial);

		FiltroTelaDTO dataAberturaFinal = new FiltroTelaDTO();
		dataAberturaFinal.setTipo(TipoElementoTela.DATE);
		dataAberturaFinal.setNome("dataFinal");
		dataAberturaFinal.setDescricao("Data Abertura Final");
		dataAberturaFinal.setRequerido("required");
		dataAberturaFinal.setValorPadrao(DateUtils.formatDiaAtualFormatoWeb());
		filtros.add(dataAberturaFinal);

		FiltroTelaDTO exibeUz = new FiltroTelaDTO();
		exibeUz.setTipo(TipoElementoTela.CHECKBOX);
		exibeUz.setNome("exibeUz");
		exibeUz.setDescricao("Exibir UZ conferidas sem problemas");
		exibeUz.setValorPadrao("N");
		exibeUz.setHint("Marque se desejar exibir UZ conferidas sem problemas no relatório");
		filtros.add(exibeUz);

		return filtros;
	}

	private List<OpcaoDTO> montaListaOpcoesSituacao() {
		List<OpcaoDTO> opcoes = new ArrayList<>();
		opcoes.add(new OpcaoDTO(AppConstantes.STRING_VAZIA, AppConstantes.STRING_VAZIA));
		
		for (TipoSituacao tipo : TipoSituacao.values()) {			
			opcoes.add(new OpcaoDTO(tipo.getDescricao(), tipo.getSigla()));
		}
		
		return opcoes;
	}

	private List<OpcaoDTO> montaListaOpcoesTipoImpressao() {
		List<OpcaoDTO> opcoes = new ArrayList<>();
		opcoes.add(new OpcaoDTO(AppConstantes.STRING_VAZIA, AppConstantes.STRING_VAZIA));
		
		for (TipoImpressao tipo : TipoImpressao.values()) {
			opcoes.add(new OpcaoDTO(tipo.getDescricao(), tipo.getSigla()));
		}
		
		return opcoes;
	}

	public List<CabecalhoDTO> montaCabecalhos() {
		List<CabecalhoDTO> cabecalhos = new ArrayList<>();
		cabecalhos.add(new CabecalhoDTO("Filial", ""));
		cabecalhos.add(new CabecalhoDTO("Nº Conferência", "text-center"));
		cabecalhos.add(new CabecalhoDTO("Data Abertura", "text-center"));
		cabecalhos.add(new CabecalhoDTO("Data Fechamento Parcial", "text-center"));
		cabecalhos.add(new CabecalhoDTO("Data Fechamento", "text-center"));
		cabecalhos.add(new CabecalhoDTO("Situação", "text-center"));
		cabecalhos.add(new CabecalhoDTO("Tipo de Impressão", "text-center"));
		cabecalhos.add(new CabecalhoDTO("", "text-center"));

		return cabecalhos;
	}

	@SuppressWarnings({ "unchecked"})
	public void gerarRelatorio(HttpServletRequest request, FiltroConferenciaDTO filtro) throws ParseException, PrintException {
		List<ConferenciaDTO> conferencias = (List<ConferenciaDTO>) request.getSession().getAttribute("conferencias");
		InformacaoUsuarioUtils usuario = (InformacaoUsuarioUtils) request.getSession().getAttribute("usuario");

		ConferenciaDTO conferencia = conferencias.stream()
				.filter(item -> item.getId().toString().equals(filtro.getNumeroConferencia())).findFirst().get();
		
		Map<String, Object> birtParams = new HashMap<>();
		birtParams.put("numeroConf", filtro.getNumeroConferencia());
		birtParams.put("exibirUzConferidas", filtro.getExibeUz());
		birtParams.put("codigoFilial", conferencia.getCodigoFilial());
		birtParams.put("tipoImpressao", "R");
		birtParams.put("tipoSituacao", conferencia.getSituacaoView());
		birtParams.put("exibirHora", DateUtils.format(new Date(), "HH:mm"));
		birtParams.put("exibirData", DateUtils.formatDiaAtual());

		String nomeImpressora = usuario.getImpressora();
		GeradorRelatorioBirt.gerarImprimirRelatorio(nomeRelatorio, birtParams, nomeImpressora);
		
		atualizaDataImpressao(conferencia);
	}
	
	private  void atualizaDataImpressao(ConferenciaDTO conferencia) {
		ConferenciaCarga conferenciaCarga = conferenciaCargaRepository.findById(conferencia.getId()).get();
		
		if(conferenciaCarga.getDataFechamento() != null) {
			if (conferenciaCarga.getDataImpressao() == null) {
				conferenciaCarga.setDataImpressao(new Date());
			} 	
			else if (conferenciaCarga.getDataReimpressao() == null) {
				conferenciaCarga.setDataReimpressao(new Date());
			}
				
			conferenciaCargaRepository.save(conferenciaCarga);
		}
	}
}