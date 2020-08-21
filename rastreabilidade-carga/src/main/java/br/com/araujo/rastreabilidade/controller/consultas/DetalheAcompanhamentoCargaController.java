package br.com.araujo.rastreabilidade.controller.consultas;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroDetalheAcompanhamentoDTO;
import br.com.araujo.rastreabilidade.service.rcarga.DetalheAcompanhamentoCargaService;

@Controller
public class DetalheAcompanhamentoCargaController {
	
	@Autowired
	private DetalheAcompanhamentoCargaService service;

	@GetMapping(value = "detalhe-uz")
	public String detalheUz(Model model, FiltroDetalheAcompanhamentoDTO parametro) throws SQLException {
		return service.buscaDetalheUz(model, parametro);
	}
	
	@GetMapping(value = "detalhe-auditoria")
	public String detalheAuditoria(Model model, FiltroDetalheAcompanhamentoDTO parametro) throws SQLException {
		return service.buscaDetalheAuditoria(model, parametro);
	}
	
	@GetMapping(value = "detalhe-volumoso")
	public String detalheVolumoso(Model model, FiltroDetalheAcompanhamentoDTO parametro) throws SQLException {
		return service.buscaDetalheVolumoso(model, parametro);
	}
}
