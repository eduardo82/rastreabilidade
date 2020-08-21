package br.com.araujo.rastreabilidade.model.cosmos;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "FILIAL")
@Access(AccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filial {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column (name = "FILI_CD_FILIAL", columnDefinition = "NUMERIC(10,0)")
	private Integer id;
	
	@Column (name = "FILI_TX_PATH_IMP")
	private String filiTxPathImp;

	@Column (name = "FILI_NM_FANTASIA")
	private String filiNmFantasia;
	
	@Column (name = "FILI_FL_SITUACAO")
	private Character situacaoFilial;
	
	@Column (name = "CHAVE_LOCAL")
	private String chaveLocal;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FILI_HR_ABERSEG", nullable = true)
	private Date aberturaSegunda ;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FILI_HR_ABERTER", nullable = true)
	private Date aberturaTerca ;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FILI_HR_ABERQUA", nullable = true)
	private Date aberturaQuarta ;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FILI_HR_ABERQUI", nullable = true)
	private Date aberturaQuinta ;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FILI_HR_ABERSEX", nullable = true)
	private Date aberturaSexta;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FILI_HR_ABERSAB", nullable = true)
	private Date aberturaSabado;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FILI_HR_ABERDOM", nullable = true)
	private Date aberturaDomingo;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FILI_HR_ABERFER", nullable = true)
	private Date aberturaFeriado;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FILI_HR_FECHSEG", nullable = true)
	private Date fechamentoSegunda;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FILI_HR_FECHTER", nullable = true)
	private Date fechamentoTerca;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FILI_HR_FECHQUA", nullable = true)
	private Date fechamentoQuarta;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FILI_HR_FECHQUI", nullable = true)
	private Date fechamentoQuinta;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FILI_HR_FECHSEX", nullable = true)
	private Date fechamentoSexta;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FILI_HR_FECHSAB", nullable = true)
	private Date fechamentoSabado;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FILI_HR_FECHDOM", nullable = true)
	private Date fechamentoDomingo;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FILI_HR_FECHFER", nullable = true)
	private Date fechamentoFeriado;
}
