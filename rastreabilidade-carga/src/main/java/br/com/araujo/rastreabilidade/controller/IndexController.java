package br.com.araujo.rastreabilidade.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.araujo.rastreabilidade.service.rcarga.comum.InformacoesUsuarioService;

@Controller
public class IndexController {		
	
	@Autowired
	private InformacoesUsuarioService service;
	
	@GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
	public String index(HttpServletRequest request, Model model) {
		service.montaUsuarioLogado(request, model);
		
		return "index";
	}
	
	@GetMapping(value = "/index", produces = MediaType.TEXT_HTML_VALUE)
	public String indexLogin(HttpServletRequest request, Model model) {

		service.montaUsuarioLogado(request, model);
		
		return "index";
	}		
}
