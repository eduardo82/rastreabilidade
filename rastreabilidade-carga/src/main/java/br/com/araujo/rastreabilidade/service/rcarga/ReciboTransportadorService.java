package br.com.araujo.rastreabilidade.service.rcarga;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.print.PrintException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ibm.icu.util.Calendar;

import br.com.araujo.rastreabilidade.constates.TipoImpressao;
import br.com.araujo.rastreabilidade.constates.TipoSituacao;
import br.com.araujo.rastreabilidade.model.rcarga.ConferenciaCarga;
import br.com.araujo.rastreabilidade.model.rcarga.VwFuncionario;
import br.com.araujo.rastreabilidade.model.rcarga.dto.ReciboTransportadorDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroConferenciaDTO;
import br.com.araujo.rastreabilidade.repository.rcarga.ConferenciaCargaRepository;
import br.com.araujo.rastreabilidade.repository.rcarga.VwFuncionarioRepository;
import br.com.araujo.rastreabilidade.service.GeradorRelatorioBirt;
import br.com.araujo.rastreabilidade.utils.DateUtils;
import br.com.araujo.rastreabilidade.utils.InformacaoUsuarioUtils;

@Service
public class ReciboTransportadorService {
	
	private static String TIPO_VERIFICACAO = "V";
	
	@Value("${server.nome.relatorio.recibo.transportador}")
	private String nomeRelatorio;

	@Autowired
	private ConferenciaCargaRepository conferenciaCargaRepository;
	
	@Autowired
	private VwFuncionarioRepository vwFuncionarioRepository;
	
	public List<ReciboTransportadorDTO> buscaConferencia(HttpServletRequest request, FiltroConferenciaDTO filtro) throws ParseException {
		Integer filial = Integer.valueOf(filtro.getCodigoFilial());
		Date dataInicial = DateUtils.parseOrNull(filtro.getDataInicial(), DateUtils.FORMATO_DATA_WEB);
		List<ConferenciaCarga> conferencias = new ArrayList<>();
		Map<Integer, String> matriculas = new HashMap<>();

		if (dataInicial != null) {			
			Calendar diaInicial = Calendar.getInstance();
			diaInicial.setTime(dataInicial);
			diaInicial.set(Calendar.SECOND, 0);
			diaInicial.set(Calendar.MINUTE, 0);
			diaInicial.set(Calendar.HOUR, 0);
			
			Calendar diaFinal = Calendar.getInstance();
			diaFinal.setTime(dataInicial);
			diaFinal.set(Calendar.SECOND, 59);
			diaFinal.set(Calendar.MINUTE, 59);
			diaFinal.set(Calendar.HOUR, 23);
			
			conferencias = conferenciaCargaRepository.buscaConferenciaPorFilialDataAbertura(filial, 
					diaInicial.getTime(), 
					diaFinal.getTime());
		} else {
			conferencias = conferenciaCargaRepository.buscaConferenciaPorFilial(filial);
		}
		
		return conferencias.stream()
				.map(conferencia -> {
					ReciboTransportadorDTO dto = new ReciboTransportadorDTO();
					
					dto.setId(conferencia.getId());
					dto.setFlagTipo(conferencia.getFlagTipo());
					dto.setFlagStatus(conferencia.getFlagStatus());
					dto.setResponsavelLoja(montaResponsavel(matriculas, conferencia.getCdUsuAbert()));
					dto.setTransportadorResponsavel(montaResponsavel(matriculas, conferencia.getResponsavelTransporteAbertura()));
					
					return dto;
				}).collect(Collectors.toList());
	}
	
	public void gerarRelatorio(HttpServletRequest request, FiltroConferenciaDTO filtro) throws ParseException, PrintException {
		InformacaoUsuarioUtils usuario = (InformacaoUsuarioUtils) request.getSession().getAttribute("usuario");
		String tipoImpressao = TipoImpressao.buscaPorSigla(filtro.getTipoImpressao()).getSigla();
		
		if ("N".equals(tipoImpressao)) {
			tipoImpressao = TipoImpressao.REIMPRESSAO.getSigla();
		} else if (!"I".equals(tipoImpressao)) {
			tipoImpressao = TIPO_VERIFICACAO;
		}
		
		Map<String, Object> birtParams = new HashMap<>();
		birtParams.put("numeroConf", filtro.getNumeroConferencia());
		birtParams.put("tipoImpressao", tipoImpressao);
		birtParams.put("tipoSituacao", TipoSituacao.buscaValorPorSigla(filtro.getSituacao()));
		birtParams.put("exibeSegundaVia", "S");
		birtParams.put("exibirHora", DateUtils.format(new Date(), "HH:mm"));
		birtParams.put("exibirData", DateUtils.formatDiaAtual());

		String nomeImpressora = usuario.getImpressora();
		GeradorRelatorioBirt.gerarImprimirRelatorio(nomeRelatorio, birtParams, nomeImpressora);
	}


	private String montaResponsavel(Map<Integer, String> matriculas, Integer codigoResponsavel) {
		if (codigoResponsavel != null) {						
			String nomeResponsavel = matriculas.get(codigoResponsavel);
			
			if (StringUtils.isBlank(nomeResponsavel)) {			
				
				VwFuncionario funcionario = vwFuncionarioRepository
						.findFirstByUsuario(StringUtils.leftPad(codigoResponsavel.toString(), 9, "0"));
				
				if (funcionario != null) {
					nomeResponsavel = funcionario.getNome();
					matriculas.put(codigoResponsavel, nomeResponsavel);
				}
			}
			
			return codigoResponsavel.toString() + " - " + nomeResponsavel; 
		}
		
		return "";
	}
}
