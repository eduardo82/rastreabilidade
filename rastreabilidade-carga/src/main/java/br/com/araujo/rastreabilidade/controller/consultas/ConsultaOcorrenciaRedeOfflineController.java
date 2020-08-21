package br.com.araujo.rastreabilidade.controller.consultas;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.araujo.rastreabilidade.controller.BasePageController;
import br.com.araujo.rastreabilidade.model.rcarga.dto.ConsultaOcorrenciaRedeOfflineDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroBaseDTO;
import br.com.araujo.rastreabilidade.service.rcarga.ConsultaOcorrenciaRedeOfflineService;

@Controller
public class ConsultaOcorrenciaRedeOfflineController extends BasePageController {
	
	@Autowired
	private ConsultaOcorrenciaRedeOfflineService service;

	@GetMapping(value = "consulta-ocorrencia-rede-offline")
	public String carregaPagina(Model model) {
		model.addAttribute("cabecalhos", service.montaCabecalhos());
		return "consultas/consulta-ocorrencia-rede-offline";
	}

	@PostMapping(value = "busca-consulta-ocorrencia-rede-offline", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ConsultaOcorrenciaRedeOfflineDTO> buscaConsultaOcorrenciaRedeOffline(@RequestBody FiltroBaseDTO filtro) {
		return service.buscaConsultaOcorrenciaRedeOffline(filtro);
	}
}
