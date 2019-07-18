package gmetrica.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import gmetrica.dto.ResumoContagemDTO;
import gmetrica.enumeration.Fornecedor;
import gmetrica.enumeration.MetodoContagem;
import gmetrica.enumeration.Situacao;
import gmetrica.enumeration.TipoContagem;
import gmetrica.enumeration.TipoProjeto;

@JsonIgnoreProperties({"funcionalidadeList"})
@Entity
@Table(name = "contagem")
public class Contagem implements Serializable {
	
	
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 6170106143895516944L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Column(name = "projeto")
    private String projeto;
    
	@Column(name = "linguagem")
	private String linguagem;
	
	@Column(name = "tipo_projeto")
	@Enumerated(EnumType.STRING)
	private TipoProjeto tipoProjeto;
	
	@Column(name = "tipo_contagem")
	@Enumerated(EnumType.STRING)
	private TipoContagem tipoContagem;
	
	@Column(name = "plataforma")
	private String plataforma;
	
	@Column(name = "lider_projeto")
	private String liderProjeto;
	
	@Column(name = "fronteira")
	private String fronteira;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_importacao")
	private Date dataImportacao;
	
	@Column(name = "fornecedor")
	@Enumerated(EnumType.STRING)
	private Fornecedor fornecedor;
	
	@Column(name = "numero_demanda")
	private String numeroDemanda;
	
	@Column(name = "situacao")
	@Enumerated(EnumType.STRING)
	private Situacao situacao;
	
	@Column(name = "proposito_escopo")
	private String propositoEscopo;
	
	@Column(name = "artefatos_usados_contagem")
	private String artefatosUsadosContagem;
	
	@Column(name = "observacoes")
	private String observacoes;
	
	@Column(name = "responsavel")
	private String responsavel;
	
	@Column(name = "scope_creep")
	private String ScopeCreep;
	
	@Column(name = "total_pf_demanda")
	private BigDecimal totalPfDemanda;
	
	@Column(name = "pf_funcao_dados")
	private BigDecimal pfFuncaoDados;
	
	@Column(name = "pf_funcao_transacional")
	private BigDecimal pfFuncaoTransacional;
	
	@Column(name = "pf_retrabalho")
	private BigDecimal pfRetrabalho;
	
	@Column(name = "total_pf_bruto")
	private BigDecimal totalPfBruto;
	
	@Column(name = "metodo_contagem")
	@Enumerated(EnumType.STRING)
	private MetodoContagem metodoContagem;
	
	@OneToMany(mappedBy = "contagem", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
	private Set<Funcionalidade> funcionalidadeList = new LinkedHashSet<>();
	
	@Transient
	private Date dataImportacaoDe;
	
	@Transient
	private Date dataImportacaoAte;
	
	@Transient
	private BigDecimal totalPfBrutoDe;
	
	@Transient
	private BigDecimal totalPfBrutoAte;
	
	@Transient
	private BigDecimal totalPfDemandaDe;
	
	@Transient
	private BigDecimal totalPfDemandaAte;
	
	@Transient
	private ResumoContagemDTO resumoContagem;

	public String getProjeto() {
		return projeto;
	}

	public void setProjeto(String projeto) {
		this.projeto = projeto;
	}

	public String getLinguagem() {
		return linguagem;
	}

	public void setLinguagem(String linguagem) {
		this.linguagem = linguagem;
	}

	public TipoProjeto getTipoProjeto() {
		return tipoProjeto;
	}

	public void setTipoProjeto(TipoProjeto tipoProjeto) {
		this.tipoProjeto = tipoProjeto;
	}

	public TipoContagem getTipoContagem() {
		return tipoContagem;
	}

	public void setTipoContagem(TipoContagem tipoContagem) {
		this.tipoContagem = tipoContagem;
	}

	public String getPlataforma() {
		return plataforma;
	}

	public void setPlataforma(String plataforma) {
		this.plataforma = plataforma;
	}

	public String getLiderProjeto() {
		return liderProjeto;
	}

	public void setLiderProjeto(String liderProjeto) {
		this.liderProjeto = liderProjeto;
	}

	public String getFronteira() {
		return fronteira;
	}

	public void setFronteira(String fronteira) {
		this.fronteira = fronteira;
	}

	public Date getDataImportacao() {
		return dataImportacao;
	}

	public void setDataImportacao(Date dataImportacao) {
		this.dataImportacao = dataImportacao;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public String getNumeroDemanda() {
		return numeroDemanda;
	}

	public void setNumeroDemanda(String numeroDemanda) {
		this.numeroDemanda = numeroDemanda;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public String getPropositoEscopo() {
		return propositoEscopo;
	}

	public void setPropositoEscopo(String propositoEscopo) {
		this.propositoEscopo = propositoEscopo;
	}

	public String getArtefatosUsadosContagem() {
		return artefatosUsadosContagem;
	}

	public void setArtefatosUsadosContagem(String artefatosUsadosContagem) {
		this.artefatosUsadosContagem = artefatosUsadosContagem;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public String getScopeCreep() {
		return ScopeCreep;
	}

	public void setScopeCreep(String scopeCreep) {
		ScopeCreep = scopeCreep;
	}

	public BigDecimal getTotalPfDemanda() {
		return totalPfDemanda;
	}

	public void setTotalPfDemanda(BigDecimal totalPfDemanda) {
		this.totalPfDemanda = totalPfDemanda;
	}

	public BigDecimal getPfFuncaoDados() {
		return pfFuncaoDados;
	}

	public void setPfFuncaoDados(BigDecimal pfFuncaoDados) {
		this.pfFuncaoDados = pfFuncaoDados;
	}

	public BigDecimal getPfFuncaoTransacional() {
		return pfFuncaoTransacional;
	}

	public void setPfFuncaoTransacional(BigDecimal pfFuncaoTransacional) {
		this.pfFuncaoTransacional = pfFuncaoTransacional;
	}

	public BigDecimal getPfRetrabalho() {
		return pfRetrabalho;
	}

	public void setPfRetrabalho(BigDecimal pfRetrabalho) {
		this.pfRetrabalho = pfRetrabalho;
	}

	public BigDecimal getTotalPfBruto() {
		return totalPfBruto;
	}

	public void setTotalPfBruto(BigDecimal totalPfBruto) {
		this.totalPfBruto = totalPfBruto;
	}

	public Set<Funcionalidade> getFuncionalidadeList() {
		return funcionalidadeList;
	}

	public void setFuncionalidadeList(Set<Funcionalidade> funcionalidadeList) {
		this.funcionalidadeList = funcionalidadeList;
	}

	public Date getDataImportacaoDe() {
		return dataImportacaoDe;
	}

	public void setDataImportacaoDe(Date dataImportacaoDe) {
		this.dataImportacaoDe = dataImportacaoDe;
	}

	public Date getDataImportacaoAte() {
		return dataImportacaoAte;
	}

	public void setDataImportacaoAte(Date dataImportacaoAte) {
		this.dataImportacaoAte = dataImportacaoAte;
	}

	public BigDecimal getTotalPfBrutoDe() {
		return totalPfBrutoDe;
	}

	public void setTotalPfBrutoDe(BigDecimal totalPfBrutoDe) {
		this.totalPfBrutoDe = totalPfBrutoDe;
	}

	public BigDecimal getTotalPfBrutoAte() {
		return totalPfBrutoAte;
	}

	public void setTotalPfBrutoAte(BigDecimal totalPfBrutoAte) {
		this.totalPfBrutoAte = totalPfBrutoAte;
	}

	public BigDecimal getTotalPfDemandaDe() {
		return totalPfDemandaDe;
	}

	public void setTotalPfDemandaDe(BigDecimal totalPfDemandaDe) {
		this.totalPfDemandaDe = totalPfDemandaDe;
	}

	public BigDecimal getTotalPfDemandaAte() {
		return totalPfDemandaAte;
	}

	public void setTotalPfDemandaAte(BigDecimal totalPfDemandaAte) {
		this.totalPfDemandaAte = totalPfDemandaAte;
	}

	public ResumoContagemDTO getResumoContagem() {
		return resumoContagem;
	}

	public void setResumoContagem(ResumoContagemDTO resumoContagem) {
		this.resumoContagem = resumoContagem;
	}
	
	public MetodoContagem getMetodoContagem() {
		return metodoContagem;
	}

	public void setMetodoContagem(MetodoContagem metodoContagem) {
		this.metodoContagem = metodoContagem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contagem other = (Contagem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
