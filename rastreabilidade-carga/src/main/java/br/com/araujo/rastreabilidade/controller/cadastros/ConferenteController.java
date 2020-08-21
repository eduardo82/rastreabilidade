package br.com.araujo.rastreabilidade.controller.cadastros;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.araujo.rastreabilidade.controller.BasePageController;
import br.com.araujo.rastreabilidade.model.rcarga.Conferente;
import br.com.araujo.rastreabilidade.service.rcarga.ConferenteService;

@Controller
public class ConferenteController extends BasePageController {

	@Autowired
	private ConferenteService service;
	
	@GetMapping(value = "conferente") 
	public String carregaPagina(Model model) {
		return "cadastros/conferente";
	}
	
	@GetMapping(value = "conferentes") 
	public String buscaConferentes(HttpServletRequest request, Model model) {
		model.addAttribute("conferentes", service.buscaConferentes(request));
		model.addAttribute("novo", Boolean.valueOf(request.getParameter("novo")));
		return "comuns/busca-conferente";
	}
	
	@GetMapping(value = "descricao-conferente")
	@ResponseBody
	public String buscaDescricao(@RequestParam("id") String id) {
		
		if (StringUtils.isNotBlank(id)) {
			id = StringUtils.leftPad(id, 9, "0");
			return service.buscaNome(id);
		}
		return null;
	}
	
	@PostMapping(value = "gravar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> gravar(HttpServletRequest request, @RequestBody Conferente conferente) {
		service.salvar(request, conferente);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping(value = "conferentes/{id}")
	public ResponseEntity<Void> excluiConferente(@PathVariable Integer id) {
		service.excluiConferente(id);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping(value = "exclui-conferente")
	public ResponseEntity<Void> excluiConferentePorFilialEChapa(@RequestBody Conferente conferente) {
		service.excluiConferente(conferente);
		return ResponseEntity.noContent().build();
	}
}
