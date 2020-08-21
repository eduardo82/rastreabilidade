<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style><%@include file="/css/busca.css"%></style>
<style><%@include file="/css/filial.css"%></style>
<h2>Parâmetros de Auditoria</h2>
<form id="form-parametros-auditoria" method="post" action="grava-parametro">
	<section class="filtros">
		<fieldset class="filtros-border">
			<legend class="filtros-border">Filtros</legend>
			<div class="row mb-3">
				<input type="hidden" id="id-parametros-auditoria" name="idParametro"/>
				<div class="control-group col-3">
					<label class="control-label input-label" for="codigo-filial">Filial *</label><br />
					<input type="text" id="codigo-filial" name="codigoFilial" maxlength="4" class="codigo-filial numeric" required onblur="buscaDadosFilial(this)"/>
					<input type="text" id="descricao-filial" name="descricaoFilial" class="descricao-filial" readonly="readonly" maxlength="40" required tabindex="-1"/>
					<a href="#" class="open-popup" onclick="abrirPesquisa('filiais')">...</a>
				</div>
				
				<div class="control-group col-3">
					<label class="control-label input-label" for="data-inicial">Data Inicial </label><br />
					<input type="date" id="data-inicial" name="dataInicial" />
				</div>
				
				<div class="control-group col-3">
					<label class="control-label input-label" for="prazo-realizacao-auditoria">Prazo Máximo Realização de Auditoria *</label><br />
					<input type="text" id="prazo-realizacao-auditoria" name="prazoRealizacaoAuditoria" class="numeric mr-1" maxlength="2" placeholder="EM HORAS" required> HORA(S)
				</div>
				
				<div class="control-group col-3">
					<label class="control-label input-label" for="prazo-exclusao-arquivos">Prazo para Exclusão de Arquivos</label><br />
					<input type="text" id="prazo-exclusao-arquivos" name="prazoExclusaoArquivos" class="numeric mr-1" maxlength="2" placeholder="EM DIAS"> DIA(S)
				</div>
				
			</div>
			<div class="row">
				<div class="control-group col-3">
	 				<label class="control-label input-label">Auditar UZ's do(s) Tipo(s)</label>
					<div class="form-check">
						<input type="hidden" id="controladoHidden" name="audControlados" value="N" />
						<input type="checkbox" id="controlados" name="audControladosCheck" class="form-check-input" onchange="registraValor(this, 'controlado')"/>
						<label for="controlados" class="form-check-label mt-1 ml-1"> Controlados</label>
					</div>										
					<div class="form-check">
						<input type="hidden" id="medicamentoHidden" name="audMedicaEspecias" value="N" />	
						<input type="checkbox" id="medicamento-especial" name="audMedicaEspeciasCheck" class="form-check-input" onchange="registraValor(this, 'medicamento')"/>
						<label for="medicamento-especial" class="form-check-label mt-1 ml-1"> Medicamentos Especiais</label>
					</div>
				</div>
				<div class="control-group col-3">
					<label class="control-label input-label">Informar Responsável pela Auditoria</label><br />
					<div class="form-check form-check-inline mt-1">
					  <input class="form-check-input" type="radio" name="informarControlados" id="informarControladosSim" value="S">
					  <label class="form-check-label" for="informarControladosSim">Sim</label>
					</div>
					<div class="form-check form-check-inline mt-1">
					  <input class="form-check-input" type="radio" name="informarControlados" id="informarControladosNao" value="N">
					  <label class="form-check-label" for="informarControladosNao">Não</label>
					</div>
					<br />
					<div class="form-check form-check-inline mt-1">
					  <input class="form-check-input" type="radio" name="informarEspeciais" id="informarEspeciaisSim" value="S">
					  <label class="form-check-label" for="informarEspeciaisSim">Sim</label>
					</div>
					<div class="form-check form-check-inline mt-1">
					  <input class="form-check-input" type="radio" name="informarEspeciais" id="informarEspeciaisNao" value="N">
					  <label class="form-check-label" for="informarEspeciaisNao">Não</label>
					</div>
				</div>
				<div class="control-group col-3">
					<label class="control-label input-label" for="transfere-arquivos">Transfere arquivos do coletor para banco de dados</label>
					<select id="transfere-arquivos" class="w-100" name="transfereArquivos">
						<option value="" label="">
						<option value="S" label="Sim">
						<option value="N" label="Não" selected="selected">
					</select>
				</div>
			</div>
			<section class="botoes float-right">
				<a href="#" class="limpar-button" onclick="excluirParametroAuditoria()">Excluir</a>
				<a href="#" class="limpar-button" onclick="limparParametrosAuditoria()">Limpar</a>
				<input type="button" id="gravar" class="limpar-button" value="Gravar"/>
<!-- 				<a href="#" class="limpar-button" onclick="alert('Em Construção')">Gravar</a> -->
			</section>
			<section class="campo-obrigatorio">
				<label>* Campo obrigatório</label>
			</section>
			<div id="dialog-confirm" title="Apagar Parâmetro Auditoria" class="d-none">
			  <p>
			  	<span class="ui-icon ui-icon-alert" style="margin:12px 10px 15px 0;"></span>
			  	<span>O parâmetro de auditoria será apagado. Você tem certeza?</span>
			  </p>
			</div>
		</fieldset>
	</section>
</form>
<script>
	function buscaDadosFilial(elemento) {
		if (elemento.value != "") {
			$.ajax({
				url: 'descricao-filial',
				data: 'id=' + elemento.value,
				success: function(descricao) {
					if (descricao) {
						$('#descricao-filial').val(descricao);
					} else {
						exibeModal("Filial não encontrada.");
						limparParametrosAuditoria();
					}
				}
			});
			
			var sucesso = function(controle) {
				if (controle.id != null) {
					document.getElementById('id-parametros-auditoria').value = controle.id;
					document.getElementById('data-inicial').value = new Date(controle.dataInicioRC).toISOString().slice(0,10);
					document.getElementById('prazo-realizacao-auditoria').value = controle.prazoMaximoRealizacaoAuditoria;
					document.getElementById('prazo-exclusao-arquivos').value = controle.prazoExclusaoArquivos;
	
					document.getElementById('controlados').checked = controle.flagPsicotropicos == 'S';
					document.getElementById('controladoHidden').value = controle.flagPsicotropicos;
	
					document.getElementById('medicamento-especial').checked = controle.flagMedicamentosEspeciais == 'S';
					document.getElementById('medicamentoHidden').value = controle.flagMedicamentosEspeciais;
	
					document.getElementById('informarControladosSim').checked = controle.flagUsuarioRespAudPsicotropicos == 'S';
					document.getElementById('informarControladosNao').checked = controle.flagUsuarioRespAudPsicotropicos == 'N';
	
					document.getElementById('informarEspeciaisSim').checked = controle.flagRespAudMedicamentosEspeciais == 'S';
					document.getElementById('informarEspeciaisNao').checked = controle.flagRespAudMedicamentosEspeciais == 'N';
	
					document.getElementById('transfere-arquivos').value = controle.flagArquivos;
				}
			};

			requisacaoComParametro('busca-dados-auditoria-filial', 'post', elemento.value, sucesso);
		} else {
			$('#descricao-filial').val("");
		}
	}

	$("#gravar").on('click', function() {
		var form = document.getElementById('form-parametros-auditoria');
		var isValidForm = form.reportValidity();

		if (isValidForm) {
			var sucesso = function(controle) {
				exibeModal("Registro salvo com sucesso.");
				limparParametrosAuditoria();
			};

			requisacaoComParametro("grava-parametro", "post", montaObjeto(form), sucesso);
		} else {
			form.reportValidity();
		}
	});	

	function excluirParametroAuditoria() {
		var form = document.getElementById('form-parametros-auditoria');
		var isValidForm = form.reportValidity();

		if (isValidForm) {
			var deletarNodo = function() {
				var id = document.getElementById('id-parametros-auditoria');
				var sucesso = function() {
					document.getElementById('id-parametros-auditoria').value = null;
					limparParametrosAuditoria();
					exibeModal("Parâmetro de auditoria apagado com sucesso.");
				};
				
				if (id.value) {
					var url = 'parametros/' + id.value;
					requisacaoDelecaoSimples(url, sucesso);
				} else {
					exibModal("Os dados não podem ser apagados!");
				}
				
				$(this).dialog("close");
			};
			
			exibeConfirmacao(deletarNodo);
		} else {
			form.reportValidity();
		}
	}

	function limparParametrosAuditoria() {
		document.getElementById('id-parametros-auditoria').value = null;
		document.getElementById('form-parametros-auditoria').reset(); 
		document.getElementById('controlados').checked = false;
		document.getElementById('medicamento-especial').checked = false;
		document.getElementById('medicamentoHidden').value = "N";
		document.getElementById('controladoHidden').value = "N";
	}

	function registraValor(elemento, id) {
		var suffixo = "Hidden";

		if (elemento.checked) {
			document.getElementById(id + suffixo).value = "S";
		} else {
			document.getElementById(id + suffixo).value = "N";
		}
	}
	
</script>
