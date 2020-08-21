package br.com.araujo.rastreabilidade.service.rcarga;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.araujo.rastreabilidade.model.rcarga.ConferenciaCarga;
import br.com.araujo.rastreabilidade.model.rcarga.ConferenciaManualRealizada;
import br.com.araujo.rastreabilidade.model.rcarga.PreNfTransportada;
import br.com.araujo.rastreabilidade.model.rcarga.dto.ConferenciaManualRealizadaDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroConferenciaManualRealizadaDTO;
import br.com.araujo.rastreabilidade.repository.rcarga.ConferenciaCargaRepository;
import br.com.araujo.rastreabilidade.repository.rcarga.ConferenciaManualRealizadaRepository;
import br.com.araujo.rastreabilidade.repository.rcarga.PreNfTransportadaRepository;
import br.com.araujo.rastreabilidade.repository.rcarga.dao.ConferenciaManualRealizadaDAO;
import br.com.araujo.rastreabilidade.utils.DateUtils;
import br.com.araujo.rastreabilidade.utils.InformacaoUsuarioUtils;

@Service
public class ConferenciaManualRealizadaService {

	@Autowired
	private ConferenciaManualRealizadaDAO conferenciaManualRealizadoDAO;
	
	@Autowired
	private PreNfTransportadaRepository preNfTransportadaRepository;
	
	@Autowired
	private ConferenciaCargaRepository conferenciaCargaRepository;
	
	@Autowired
	private ConferenciaManualRealizadaRepository conferenciaManualRealizadaRepository;
	
	public List<ConferenciaManualRealizadaDTO> buscaListaConferencias(FiltroConferenciaManualRealizadaDTO parametro) {
		return conferenciaManualRealizadoDAO.buscaListaConferencia(parametro);
	}

	public void gravarConferenciaManual(HttpServletRequest request, List<ConferenciaManualRealizadaDTO> conferencias) throws Exception {
		
		Map<Integer, Integer> preNfTransportadaComConfManual = new HashMap<>();
		Set<Integer> conferenciaAtualizada = new HashSet<>();
		InformacaoUsuarioUtils usuario = (InformacaoUsuarioUtils) request.getSession().getAttribute("usuario");
		
		for (ConferenciaManualRealizadaDTO conferenciaManualRealizada : conferencias) {
			
			boolean temConferencia = false;
			boolean temExportacaoRealizada = false;
			
			//Verifica se Existe conferencia para o registro em questao
			if(conferenciaManualRealizada.getCodigoConferencia() != null) {
				temConferencia = true;
				if (!conferenciaAtualizada.contains(conferenciaManualRealizada.getCodigoConferencia()))
				{
					altualizaConferencia(usuario, conferenciaManualRealizada.getCodigoConferencia());
				}
			}
			
			if(conferenciaManualRealizada.getCodigoPrenotaFiscalTransportada() != null )
			{
				if (StringUtils.isBlank(conferenciaManualRealizada.getDataExportacaoView())) {
					atualizaPreNfTransportada(usuario, conferenciaManualRealizada.getCodigoPrenotaFiscalTransportada());
				} else {
					temExportacaoRealizada = true;
				}
			}
			
			// Executa a inclusão da conferência manual apenas uma vez por pré-nota
			if (!preNfTransportadaComConfManual.containsKey(conferenciaManualRealizada.getCodigoPrenotaFiscalTransportada()))
			{
				preNfTransportadaComConfManual.put(
					conferenciaManualRealizada.getCodigoPrenotaFiscalTransportada(),
					incluiConferenciaManual(usuario, conferenciaManualRealizada, !temConferencia && temExportacaoRealizada ) );
			}
		}
	}
	
	private Integer incluiConferenciaManual(InformacaoUsuarioUtils usuario, ConferenciaManualRealizadaDTO conferenciaManualRealizada, 
			boolean ordemExportadaSemConferencia) throws Exception {
		PreNfTransportada preNota = preNfTransportadaRepository
				.findById(conferenciaManualRealizada.getCodigoPrenotaFiscalTransportada())
				.orElseThrow(() -> new Exception());
		
		ConferenciaManualRealizada conferenciaManual = new ConferenciaManualRealizada();
		
		conferenciaManual.setPreNfTransportada(preNota);
		conferenciaManual.setDataInclusao(new Date());
		conferenciaManual.setFlagExclusaoCarga(ordemExportadaSemConferencia ? "S" : "N");
		conferenciaManual.setCodigoUsuario(Long.valueOf(usuario.getMatricula()));
		conferenciaManual.setDataAlteracao(new Date());
		conferenciaManual.setDataExclusaoCarga(new Date());
		conferenciaManualRealizadaRepository.saveAndFlush(conferenciaManual);
		
		return conferenciaManual.getId();
	}
	
	private void atualizaPreNfTransportada(InformacaoUsuarioUtils usuario, Integer numeroPrenota) {
		
		PreNfTransportada preNfTransportada = preNfTransportadaRepository.findByPreNotaFiscal(numeroPrenota);
		
		String dataExportacaoSemHora =  DateUtils.format(new Date());
		int anoDataExportacao = Integer.parseInt(dataExportacaoSemHora.substring(6, dataExportacaoSemHora.length()));
		int mesDataExportacao = Integer.parseInt(dataExportacaoSemHora.substring(3, 5));
		--mesDataExportacao; // 0-based
		int diaDataExportacao = Integer.parseInt( dataExportacaoSemHora.substring(0, 2));
		
		preNfTransportada.setDataExportacao(new GregorianCalendar(anoDataExportacao , mesDataExportacao, diaDataExportacao).getTime());
		preNfTransportada.setDataAlteracao(new Date());
		preNfTransportada.setCodigoUsuario(Long.valueOf(usuario.getMatricula()));
		
		preNfTransportadaRepository.saveAndFlush(preNfTransportada);
	}
	
	private void altualizaConferencia(InformacaoUsuarioUtils usuario, Integer idConferencia) throws Exception {
		
		ConferenciaCarga conferencia = conferenciaCargaRepository.findById(idConferencia)
				.orElseThrow(() -> new Exception("ID Conferência Carga não encontrada - " + idConferencia));
		
		conferencia.setFlagTipo("M");
		conferencia.setFlagStatus("M");
		conferencia.setCodigoUsuario(Long.valueOf(usuario.getMatricula()));
		conferencia.setDataAlteracao(new Date());
		
		conferenciaCargaRepository.saveAndFlush(conferencia);
	}
}
