<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style><%@include file="/css/busca.css"%></style>
<style>
	.codigo-conferente {
		width: 14%;
	}
	
	.descricao-conferente {
		width: 70% !important;
	}
</style>
<h2>Manter Conferente</h2>
<form id="form-conferente" method="post" action="gravar">
	<section class="filtros">
		<fieldset class="filtros-border">
			<legend class="filtros-border">Filtros</legend>
			<div class="row mb-3">
				<input type="hidden" id="id-conferente" />
				<div class="control-group col-4">			
					<c:import url="../componentes/filial.jsp" >
						<c:param name="filialRequerida" value="true"></c:param>
					</c:import>
				</div>
				
				<div class="control-group col-5">
					<label class="control-label input-label" for="codigo-conferente">Conferente *</label><br />
					<input type="text" id="codigo-conferente" name="chapa" maxlength="9" class="codigo-conferente numeric" required onblur="buscaNomeConferente(this)"/>
					<input type="text" id="descricao-conferente" name="nome" class="descricao-conferente" maxlength="40" tabindex="-1"/>
					<a href="#" class="open-popup" onclick="abrirPesquisaConferente()">...</a>
				</div>
				
				<div class="control-group col-3">
					<label class="control-label input-label" for="responsavel">Responsável por liberar fechamento final *</label>
					<select id="responsavel" required class="w-100" name="conferenteFinal">
						<option value="" label="">
						<option value="S" label="Sim">
						<option value="N" label="Não">
					</select>
				</div>
				
			</div>
			<div class="row">
				<div class="control-group col-4">
	 				<label class="control-label input-label">Responsável pelas auditorias </label>
					<div class="form-check">
						<input type="hidden" id="medicamentoHidden" name="audMedicaEspecias" value="S" />	
						<input type="checkbox" id="medicamento-especial" name="audMedicaEspeciasCheck" class="form-check-input" checked onchange="registraValor(this, 'medicamento')"/>
						<label for="medicamento-especial" class="form-check-label mt-1 ml-1"> Medicamentos Especiais</label>
					</div>
					<div class="form-check">
						<input type="hidden" id="controladoHidden" name="audControlados" value="S" />
						<input type="checkbox" id="controlados" name="audControladosCheck" class="form-check-input" checked onchange="registraValor(this, 'controlado')"/>
						<label for="controlados" class="form-check-label mt-1 ml-1"> Controlados</label>
					</div>										
				</div>
			</div>
			<section class="botoes float-right">
				<a href="#" class="limpar-button" onclick="excluirConferente()">Excluir</a>
				<a href="#" class="limpar-button" onclick="limparConferente()">Limpar</a>
				<input type="button" id="gravar" class="limpar-button" value="Gravar"/>
			</section>
			<section class="campo-obrigatorio">
				<label>* Campo obrigatório</label>
			</section>
			<div id="dialog-confirm" title="Apagar Conferente" class="d-none">
			  <p>
			  	<span class="ui-icon ui-icon-alert" style="margin:12px 10px 15px 0;"></span>
			  	<span>O conferente será apagado. Você tem certeza?</span>
			  </p>
			</div>
		</fieldset>
	</section>
</form>
<script>
	$("#gravar").on('click', function() {
		var form = document.getElementById('form-conferente');
		var isValidForm = form.reportValidity();

		if (isValidForm) {
			var sucesso = function() {
				document.getElementById('id-conferente').value = null;
				document.getElementById('form-conferente').reset(); 
				document.getElementById('controlados').checked = false;
				document.getElementById('medicamento-especial').checked = false;
				document.getElementById('medicamentoHidden').value = "N";
				document.getElementById('controladoHidden').value = "N";
				
				exibeModal("Conferente gravado com sucesso.");
			};

			requisacaoComParametro('gravar', 'post', montaObjeto(form), sucesso);
		} else {
			form.reportValidity();
		}
	});	

	function excluirConferente() {
		var form = document.getElementById('form-conferente');
		var isValidForm = form.reportValidity();

		if (isValidForm) {
			var deletarNodo = function() {
				var id = document.getElementById('id-conferente');
				var sucesso = function() {
					document.getElementById('id-conferente').value = null;
					document.getElementById('form-conferente').reset();
					exibeModal("Conferente apagado com sucesso.");
				};
				
				if (id.value) {
					var url = 'conferentes/' + id.value;
					requisacaoDelecaoSimples(url, sucesso);
				} else {
					var form = document.getElementById('form-conferente');
					requisacaoComParametro('exclui-conferente', 'post', montaObjeto(form), sucesso);
				}
				$(this).dialog("close");
			};
			
			exibeConfirmacao(deletarNodo);
		} else {
			form.reportValidity();
		}
	}

	function limparConferente() {
		document.getElementById('id-conferente').value = null;
		document.getElementById('form-conferente').reset(); 
		document.getElementById('controlados').checked = false;
		document.getElementById('medicamento-especial').checked = false;
		document.getElementById('medicamentoHidden').value = "N";
		document.getElementById('controladoHidden').value = "N";
	}

	function buscaNomeConferente(elemento) {
		if (elemento.value != "") {
			$.ajax({
				url: 'descricao-conferente',
				data: 'id=' + elemento.value,
				success: function(descricao) {
					$('#descricao-conferente').val(descricao);
				}
			});
		}
	}

	function abrirPesquisaConferente() {
		var url = "conferentes";
		var filial = document.getElementById('codigo-filial');
		var nome = document.getElementById('descricao-conferente');
		
		if (filial.value) {
			url = url + '?filial=' + filial.value;
		}

		if (nome.value) {
			if (nome.value.length >= 3) {
				url += url.includes("?") ? "&" : "?";
				url = url + 'nome=' + nome.value;
			} else {
				exibeModal("O nome deve conter no mínimo 3 caracteres.");
				return;
			}
		}
		
		window.open(url, "parametros", "location=no,top=150,left=300,width=850,height=550");
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
