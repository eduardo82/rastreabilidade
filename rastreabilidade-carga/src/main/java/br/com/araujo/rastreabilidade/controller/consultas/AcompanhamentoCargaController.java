package br.com.araujo.rastreabilidade.controller.consultas;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.araujo.rastreabilidade.controller.BasePageController;
import br.com.araujo.rastreabilidade.model.rcarga.dto.AcompanhamentoCargaDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroAcompanhamentoCargaDTO;
import br.com.araujo.rastreabilidade.service.rcarga.AcompanhamentoCargaService;

@Controller
public class AcompanhamentoCargaController extends BasePageController {

	@Autowired
	private AcompanhamentoCargaService service;
		
	@GetMapping(value = "acompanhamento")
	public String carregaPagina(Model model) {
		return "consultas/acompanhamentocarga";
	}
	
	@PostMapping(value = "busca-acompanhementos")
	@ResponseBody
	public List<AcompanhamentoCargaDTO> buscaAcompanhamentos(@RequestBody FiltroAcompanhamentoCargaDTO filtro) throws Exception {
		return service.buscaAcompanhamentoCarga(filtro);
	}

}
