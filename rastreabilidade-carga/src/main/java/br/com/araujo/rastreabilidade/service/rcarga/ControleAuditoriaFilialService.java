package br.com.araujo.rastreabilidade.service.rcarga;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.araujo.rastreabilidade.constates.TipoSimNao;
import br.com.araujo.rastreabilidade.model.rcarga.ControleAuditoriaFilial;
import br.com.araujo.rastreabilidade.model.rcarga.dto.ControleAuditoriaFilialDTO;
import br.com.araujo.rastreabilidade.repository.rcarga.ControleAuditoriaFilialRepository;
import br.com.araujo.rastreabilidade.utils.DateUtils;
import br.com.araujo.rastreabilidade.utils.InformacaoUsuarioUtils;

@Service
public class ControleAuditoriaFilialService {

	@Autowired
	private ControleAuditoriaFilialRepository controleAuditoriaFilialRepository;

	public ControleAuditoriaFilial findByFilial(Integer filial) {
		ControleAuditoriaFilial controle = controleAuditoriaFilialRepository.findFirstByFilial(filial);

		if (controle == null) {
			controle = new ControleAuditoriaFilial();
		}

		return controle;
	}

	public Integer countByFilial(Integer filial) {
		long contador = controleAuditoriaFilialRepository.countByFilial(filial);
		return Long.valueOf(contador).intValue();
	}

	public void apagaParametro(Integer id) {
		controleAuditoriaFilialRepository.deleteById(id);
	}

	public void gravaParametroAuditoria(HttpServletRequest request, ControleAuditoriaFilialDTO controleDTO) {
		InformacaoUsuarioUtils usuario = (InformacaoUsuarioUtils) request.getSession().getAttribute("usuario");
		ControleAuditoriaFilial controle = null;
		
		if (controleDTO.getIdParametro() != null) {
			controle = controleAuditoriaFilialRepository.findById(controleDTO.getIdParametro()).orElse(new ControleAuditoriaFilial());
		} else {
			controle = new ControleAuditoriaFilial();
			controle.setFilial(controleDTO.getCodigoFilial());
			controle.setFlagParmais(TipoSimNao.N);
			controle.setFlagUsuarioRespAudParmais(TipoSimNao.N);
		}
	
		controle.setCodigoUsuario(Long.valueOf(usuario.getMatricula()));
		controle.setDataAlteracao(new Date());
		controle.setDataInicioRC(DateUtils.parseOrNull(controleDTO.getDataInicial(), DateUtils.FORMATO_DATA_WEB));
		controle.setFlagArquivos(TipoSimNao.valueOf(controleDTO.getTransfereArquivos()));
		controle.setFlagMedicamentosEspeciais(TipoSimNao.valueOf(controleDTO.getAudMedicaEspecias()));
		controle.setFlagPsicotropicos(TipoSimNao.valueOf(controleDTO.getAudControlados()));
		controle.setFlagRespAudMedicamentosEspeciais(TipoSimNao.valueOf(controleDTO.getInformarEspeciais()));
		controle.setFlagUsuarioRespAudPsicotropicos(TipoSimNao.valueOf(controleDTO.getInformarControlados()));
		controle.setPrazoExclusaoArquivos(controleDTO.getPrazoExclusaoArquivos());
		controle.setPrazoMaximoRealizacaoAuditoria(controleDTO.getPrazoRealizacaoAuditoria());
		controle.setVersao(controle.getVersao() + 1);

		controleAuditoriaFilialRepository.save(controle);
	}
}
