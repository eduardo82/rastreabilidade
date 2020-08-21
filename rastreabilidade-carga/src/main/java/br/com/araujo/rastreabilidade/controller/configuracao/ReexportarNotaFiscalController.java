package br.com.araujo.rastreabilidade.controller.configuracao;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.araujo.rastreabilidade.controller.BasePageController;
import br.com.araujo.rastreabilidade.model.rcarga.dto.PreNfTransportadaDTO;
import br.com.araujo.rastreabilidade.service.rcarga.ReexportarNotaFiscalService;
import br.com.araujo.rastreabilidade.utils.DateUtils;

@Controller
public class ReexportarNotaFiscalController extends BasePageController {
	
	@Autowired
	private ReexportarNotaFiscalService reexportarNotaFiscalService;

	@RequestMapping(value = "/reexportar", method = RequestMethod.GET)
	public String carregaPagina(Model model) {
		model.addAttribute("filtros", reexportarNotaFiscalService.montaFiltrosTela());
		model.addAttribute("cabecalhos", reexportarNotaFiscalService.montaCabecalhos());
		return "configuracao/reexportar";
	}
	
	@RequestMapping(value="/pesquisa-notas", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<PreNfTransportadaDTO> pesquisaNotas(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		Long idFilial = Long.valueOf(request.getParameter("codigoFilial"));
		Date dataRota = DateUtils.parseOrNull(request.getParameter("data-rota"), DateUtils.FORMATO_DATA_WEB);
		
		return reexportarNotaFiscalService.pesquisaPreNotaTransportada(idFilial, dataRota);
	}
	
	@PostMapping(value = "/atualiza-prenotas")
	public void atualizaPrenota(HttpServletRequest request, HttpServletResponse response) {
		reexportarNotaFiscalService.atualizaPrenota(request);
	}
}