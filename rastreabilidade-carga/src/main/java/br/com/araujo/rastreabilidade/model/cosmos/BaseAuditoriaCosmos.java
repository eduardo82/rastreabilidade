package br.com.araujo.rastreabilidade.model.cosmos;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import lombok.Data;

@Data
public class BaseAuditoriaCosmos implements Serializable {
	
	private static final long serialVersionUID = -5164487113535932849L;

	@Column(name="XXXX_CD_USUALT", nullable=true)
	private Long codigoUsuario = 0L;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "XXXX_DH_ALT", nullable = true)
	private Date dataAlteracao = new Date();
	
	@Version
	@Column(name = "XXXX_CT_LOCK", nullable = false)
	private int versao;
}
