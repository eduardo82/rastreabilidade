package br.com.araujo.rastreabilidade.service.rcarga;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.araujo.rastreabilidade.constates.TipoSimNao;
import br.com.araujo.rastreabilidade.model.rcarga.Conferente;
import br.com.araujo.rastreabilidade.model.rcarga.VwFuncionario;
import br.com.araujo.rastreabilidade.repository.rcarga.ConferenteRepository;
import br.com.araujo.rastreabilidade.repository.rcarga.VwFuncionarioRepository;
import br.com.araujo.rastreabilidade.utils.InformacaoUsuarioUtils;

@Service
public class ConferenteService {

	@Autowired
	private ConferenteRepository conferenteRepository;
	
	@Autowired
	private VwFuncionarioRepository vwFuncionarioRepository;
	
	public void salvar(HttpServletRequest request, Conferente conferente) {
		Conferente conferenteBanco = conferenteRepository
				.findFirstByCodigoFilialAndChapa(conferente.getCodigoFilial(), conferente.getChapa());
		
		InformacaoUsuarioUtils usuario = (InformacaoUsuarioUtils) request.getSession().getAttribute("usuario");
		
		if (conferenteBanco == null) {			
			conferente.setAudParMais(TipoSimNao.N.getSigla());
			conferente.setDataInclusao(new Date());
			conferente.setVersao(0);
			conferente.setDataAlteracao(new Date());
			conferente.setCodigoUsuario(Long.valueOf(usuario.getMatricula()));
			conferenteRepository.save(conferente);
		} else {
			conferenteBanco.setAudControlados(conferente.getAudControlados()); 
			conferenteBanco.setAudMedicaEspecias(conferente.getAudMedicaEspecias());
			conferenteBanco.setConferenteFinal(conferente.getConferenteFinal());
			conferenteBanco.setDataAlteracao(new Date());
			conferenteBanco.setCodigoUsuario(Long.valueOf(usuario.getMatricula()));
			conferenteBanco.setVersao(conferenteBanco.getVersao() + 1);
			conferenteRepository.save(conferente);
		}
	}
	
	public List<Conferente> buscaConferentes(HttpServletRequest request) {
		List<Conferente> conferentes = null;
		String filial = request.getParameter("filial");
		String nome = request.getParameter("nome");
		if (StringUtils.isNotBlank(filial) && StringUtils.isBlank(nome)) {
			conferentes = conferenteRepository.findByCodigoFilialOrderByNome(Integer.valueOf(filial));
		} else if (StringUtils.isNotBlank(filial) && StringUtils.isNotBlank(nome)) {
			conferentes =  conferenteRepository
					.findByCodigoFilialAndNomeContainingOrderByNome(Integer.valueOf(filial), nome);
		} else if (StringUtils.isNotBlank(nome)) {
			conferentes =  conferenteRepository.findByNomeContainingOrderByNome(nome);
		} 

		if (conferentes == null || (conferentes != null && conferentes.size() == 0)) {
			List<VwFuncionario> funcionarios = vwFuncionarioRepository.findAll(Sort.by("nome"));
			
			conferentes = funcionarios.stream().map(funcionario -> {
				Conferente conferente = new Conferente();
				conferente.setChapa(funcionario.getUsuario());
				conferente.setNome(funcionario.getNome());
				return conferente;
			}).collect(Collectors.toList());
		}
		
		return conferentes;
	}
	
	public String buscaNome(String id) {
		String nome =  conferenteRepository.findByChapa(id);
		
		if (StringUtils.isBlank(nome)) {
			VwFuncionario funcionario = vwFuncionarioRepository.findFirstByUsuario(id.toString());
			
			if (funcionario != null) {
				return funcionario.getNome();
			}
		}
		
		return nome;
	}
	
	public void excluiConferente(Integer id) {
		conferenteRepository.deleteById(id);
	}
	
	public void excluiConferente(Conferente conferente) {
		Conferente conferenteBanco = conferenteRepository
				.findFirstByCodigoFilialAndChapa(conferente.getCodigoFilial(), conferente.getChapa());
		conferenteRepository.delete(conferenteBanco);
	}
}
