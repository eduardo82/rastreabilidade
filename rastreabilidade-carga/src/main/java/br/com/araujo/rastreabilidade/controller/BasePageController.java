package br.com.araujo.rastreabilidade.controller;

import org.springframework.ui.Model;

public abstract class BasePageController {

	public abstract String carregaPagina(Model model);
}
