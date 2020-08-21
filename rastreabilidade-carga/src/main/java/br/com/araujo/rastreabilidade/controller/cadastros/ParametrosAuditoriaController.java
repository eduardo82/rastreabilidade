package br.com.araujo.rastreabilidade.controller.cadastros;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.araujo.rastreabilidade.controller.BasePageController;
import br.com.araujo.rastreabilidade.model.rcarga.ControleAuditoriaFilial;
import br.com.araujo.rastreabilidade.model.rcarga.dto.ControleAuditoriaFilialDTO;
import br.com.araujo.rastreabilidade.service.rcarga.ControleAuditoriaFilialService;

@Controller
public class ParametrosAuditoriaController extends BasePageController {
	
	@Autowired
	private ControleAuditoriaFilialService controleAuditoriaFilialService;

	@GetMapping(value = "parametros-auditoria")
	public String carregaPagina(Model model) {
		return "cadastros/parametros-auditoria";
	}
	
	@PostMapping(value = "busca-dados-auditoria-filial")
	@ResponseBody
	public ControleAuditoriaFilial buscaDadosAutoditoriaFilial(@RequestBody Integer filial) {
		return controleAuditoriaFilialService.findByFilial(filial);
	}
	
	@PostMapping(value = "grava-parametro", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> gravaAuditoriaFilial(HttpServletRequest request, @RequestBody ControleAuditoriaFilialDTO controleDTO) {
		controleAuditoriaFilialService.gravaParametroAuditoria(request, controleDTO);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping(value = "parametros/{id}")
	public ResponseEntity<Void> apagaParametro(@PathVariable Integer id) {
		controleAuditoriaFilialService.apagaParametro(id);
		return ResponseEntity.noContent().build();
	}
}
