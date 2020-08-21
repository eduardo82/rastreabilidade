package br.com.araujo.rastreabilidade.controller.consultas;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.araujo.rastreabilidade.controller.BasePageController;

@Controller
public class UltimaAuditoriaController extends BasePageController {

	@GetMapping(value = "ultima-auditoria")
	public String carregaPagina(Model model) {
		return "consultas/ultima-auditoria";
	}

}
