package br.com.araujo.rastreabilidade.service.rcarga.comum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import br.com.araujo.rastreabilidade.mapper.InetOrgPersonAraujo;
import br.com.araujo.rastreabilidade.repository.cosmos.dao.CosmosDAO;
import br.com.araujo.rastreabilidade.service.cosmos.FilialService;
import br.com.araujo.rastreabilidade.utils.InformacaoUsuarioUtils;

@Service
public class InformacoesUsuarioService {
	
	@Autowired
	private CosmosDAO cosmosDAO;
	
	@Autowired
	private FilialService filialService;

	public void montaUsuarioLogado(HttpServletRequest request, Model model) {
		Authentication auth = (Authentication) SecurityContextHolder.getContext().getAuthentication();
		WebAuthenticationDetails details = (WebAuthenticationDetails) auth.getDetails();
		InetOrgPersonAraujo person = (InetOrgPersonAraujo) auth.getPrincipal();
		
		Integer codigoFilial = 1;
		String nomeFilial = "";
		
		if (person.getDepartmentNumber() != null && person.getDepartmentNumber().length > 0) {
			codigoFilial = Integer.valueOf(person.getDepartmentNumber()[0]);
		}
		
		nomeFilial = filialService.buscaDescricao(codigoFilial).getFiliNmFantasia();
		
		InformacaoUsuarioUtils usuario = new InformacaoUsuarioUtils();
		usuario.setDisplayName(person.getDisplayName());
		usuario.setCodigoFilial(codigoFilial.toString());
		usuario.setNomeFilial(nomeFilial);
		usuario.setIpMaquina(details.getRemoteAddress());
		usuario.setEmail(person.getMail());
		usuario.setNomeUsuario(person.getUsername());
		usuario.setSessionId(details.getSessionId());
		usuario.setMatricula(person.getEmployeeID());
		usuario.setImpressora(cosmosDAO.buscaNomeImpressora(details.getRemoteAddress()));
		usuario.setAdmin(false);
		
		model.addAttribute("nome", person.getDisplayName());
		
		HttpSession session = request.getSession();
		session.setAttribute("usuario", usuario);
		session.setAttribute("codigoFilial", codigoFilial);
		session.setAttribute("nomeFilial", nomeFilial);
		session.setAttribute("admin", usuario.getAdmin());
	}
}
