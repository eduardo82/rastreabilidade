package br.com.araujo.rastreabilidade.controller.comum;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.araujo.rastreabilidade.service.cosmos.FilialService;

@Controller
public class FilialController {

	@Autowired
	private FilialService service;
	
	@GetMapping(value = "filiais")
	public String buscaTodas(Model model) {
		model.addAttribute("filiais", service.listaFiliais());
		return "comuns/busca-filial";
	}

	@GetMapping(value = "descricao-filial")
	@ResponseBody
	public String buscaDescricao(@RequestParam("id") String id) {
		
		if (StringUtils.isNotBlank(id)) {			
			return service.buscaDescricao(Integer.valueOf(id)).getFiliNmFantasia();
		}
		return null;
	}	
}
