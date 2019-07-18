package gmetrica.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "template_contagem")
public class TemplateContagem implements Serializable {
	
	
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 6170106143895516944L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Column(name = "projeto_aba")
    private String projetoAba;
	
	@Column(name = "projeto_referencia")
    private String projetoReferencia;
	
	@Column(name = "linguagem_aba")
    private String linguagemAba;
	
	@Column(name = "linguagem_referencia")
    private String linguagemReferencia;
	
	@Column(name = "tipo_projeto_aba")
    private String tipoProjetoAba;
	
	@Column(name = "tipo_projeto_referencia")
    private String tipoProjetoReferencia;
	
	@Column(name = "tipo_contagem_aba")
    private String tipoContagemAba;
	
	@Column(name = "tipo_contagem_referencia")
    private String tipoContagemReferencia;
	
	@Column(name = "plataforma_aba")
    private String plataformaAba;
	
	@Column(name = "plataforma_referencia")
    private String plataformaReferencia;
	
	@Column(name = "lider_projeto_aba")
    private String liderProjetoAba;
	
	@Column(name = "lider_projeto_referencia")
    private String liderProjetoReferencia;
	
	@Column(name = "observacoes_aba")
    private String observacoesAba;
	
	@Column(name = "observacoes_referencia")
    private String observacoesReferencia;
	
	@Column(name = "artefatos_usados_aba")
    private String artefatosUsadosAba;
	
	@Column(name = "artefatos_usados_referencia")
    private String artefatorUsadosReferencia;
	
	@Column(name = "proposito_escopo_aba")
    private String propositoEscopoAba;
	
	@Column(name = "proposito_escopo_referencia")
    private String propositoEscopoReferencia;
	
	@Column(name = "fronteira_aba")
    private String fronteiraAba;
	
	@Column(name = "fronteira_referencia")
    private String fronteiraReferencia;
	
	@Column(name = "total_pf_demanda_aba")
    private String totalPfDemandaAba;
	
	@Column(name = "total_pf_demanda_referencia")
    private String totalPfDemandaReferencia;
	
	@Column(name = "pf_funcao_dados_aba")
    private String pfFuncaoDadosAba;
	
	@Column(name = "pf_funcao_dados_referencia")
    private String pfFuncaoDadosReferencia;
	
	@Column(name = "pf_retrabalho_aba")
    private String pfRetrabalhoAba;
	
	@Column(name = "pf_retrabalho_referencia")
    private String pfRetrabalhoReferencia;
	
	@Column(name = "pf_funcao_transacional_aba")
    private String pfFuncaoTransacionalAba;
	
	@Column(name = "pf_funcao_transacional_referencia")
    private String pfFuncaoTransacionalReferencia;
	
	@Column(name = "total_pf_bruto_aba")
    private String totalPfBrutoAba;
	
	@Column(name = "total_pf_bruto_referencia")
    private String totalPfBrutoReferencia;
	
	@Column(name = "scope_creep_aba")
    private String scopeCreepAba;
	
	@Column(name = "scope_creep_referencia")
    private String scopeCreepReferencia;
	
	@Column(name = "metodo_contagem_aba")
    private String metodoContagemAba;
	
	@Column(name = "metodo_contagem_referencia")
	private String metodoContagemReferencia;
	
	@OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="id_template")
	private Template template;
	
	public String getProjetoAba() {
		return projetoAba;
	}

	public void setProjetoAba(String projetoAba) {
		this.projetoAba = projetoAba;
	}

	public String getProjetoReferencia() {
		return projetoReferencia;
	}

	public void setProjetoReferencia(String projetoReferencia) {
		this.projetoReferencia = projetoReferencia;
	}

	public String getLinguagemAba() {
		return linguagemAba;
	}

	public void setLinguagemAba(String linguagemAba) {
		this.linguagemAba = linguagemAba;
	}

	public String getLinguagemReferencia() {
		return linguagemReferencia;
	}

	public void setLinguagemReferencia(String linguagemReferencia) {
		this.linguagemReferencia = linguagemReferencia;
	}

	public String getTipoProjetoAba() {
		return tipoProjetoAba;
	}

	public void setTipoProjetoAba(String tipoProjetoAba) {
		this.tipoProjetoAba = tipoProjetoAba;
	}

	public String getTipoProjetoReferencia() {
		return tipoProjetoReferencia;
	}

	public void setTipoProjetoReferencia(String tipoProjetoReferencia) {
		this.tipoProjetoReferencia = tipoProjetoReferencia;
	}

	public String getTipoContagemAba() {
		return tipoContagemAba;
	}

	public void setTipoContagemAba(String tipoContagemAba) {
		this.tipoContagemAba = tipoContagemAba;
	}

	public String getTipoContagemReferencia() {
		return tipoContagemReferencia;
	}

	public void setTipoContagemReferencia(String tipoContagemReferencia) {
		this.tipoContagemReferencia = tipoContagemReferencia;
	}

	public String getPlataformaAba() {
		return plataformaAba;
	}

	public void setPlataformaAba(String plataformaAba) {
		this.plataformaAba = plataformaAba;
	}

	public String getPlataformaReferencia() {
		return plataformaReferencia;
	}

	public void setPlataformaReferencia(String plataformaReferencia) {
		this.plataformaReferencia = plataformaReferencia;
	}

	public String getLiderProjetoAba() {
		return liderProjetoAba;
	}

	public void setLiderProjetoAba(String liderProjetoAba) {
		this.liderProjetoAba = liderProjetoAba;
	}

	public String getLiderProjetoReferencia() {
		return liderProjetoReferencia;
	}

	public void setLiderProjetoReferencia(String liderProjetoReferencia) {
		this.liderProjetoReferencia = liderProjetoReferencia;
	}

	public String getObservacoesAba() {
		return observacoesAba;
	}

	public void setObservacoesAba(String observacoesAba) {
		this.observacoesAba = observacoesAba;
	}

	public String getObservacoesReferencia() {
		return observacoesReferencia;
	}

	public void setObservacoesReferencia(String observacoesReferencia) {
		this.observacoesReferencia = observacoesReferencia;
	}

	public String getArtefatosUsadosAba() {
		return artefatosUsadosAba;
	}

	public void setArtefatosUsadosAba(String artefatosUsadosAba) {
		this.artefatosUsadosAba = artefatosUsadosAba;
	}

	public String getArtefatorUsadosReferencia() {
		return artefatorUsadosReferencia;
	}

	public void setArtefatorUsadosReferencia(String artefatorUsadosReferencia) {
		this.artefatorUsadosReferencia = artefatorUsadosReferencia;
	}

	public String getPropositoEscopoAba() {
		return propositoEscopoAba;
	}

	public void setPropositoEscopoAba(String propositoEscopoAba) {
		this.propositoEscopoAba = propositoEscopoAba;
	}

	public String getPropositoEscopoReferencia() {
		return propositoEscopoReferencia;
	}

	public void setPropositoEscopoReferencia(String propositoEscopoReferencia) {
		this.propositoEscopoReferencia = propositoEscopoReferencia;
	}

	public String getFronteiraAba() {
		return fronteiraAba;
	}

	public void setFronteiraAba(String fronteiraAba) {
		this.fronteiraAba = fronteiraAba;
	}

	public String getFronteiraReferencia() {
		return fronteiraReferencia;
	}

	public void setFronteiraReferencia(String fronteiraReferencia) {
		this.fronteiraReferencia = fronteiraReferencia;
	}

	public String getTotalPfDemandaAba() {
		return totalPfDemandaAba;
	}

	public void setTotalPfDemandaAba(String totalPfDemandaAba) {
		this.totalPfDemandaAba = totalPfDemandaAba;
	}

	public String getTotalPfDemandaReferencia() {
		return totalPfDemandaReferencia;
	}

	public void setTotalPfDemandaReferencia(String totalPfDemandaReferencia) {
		this.totalPfDemandaReferencia = totalPfDemandaReferencia;
	}

	public String getPfFuncaoDadosAba() {
		return pfFuncaoDadosAba;
	}

	public void setPfFuncaoDadosAba(String pfFuncaoDadosAba) {
		this.pfFuncaoDadosAba = pfFuncaoDadosAba;
	}

	public String getPfFuncaoDadosReferencia() {
		return pfFuncaoDadosReferencia;
	}

	public void setPfFuncaoDadosReferencia(String pfFuncaoDadosReferencia) {
		this.pfFuncaoDadosReferencia = pfFuncaoDadosReferencia;
	}

	public String getPfRetrabalhoAba() {
		return pfRetrabalhoAba;
	}

	public void setPfRetrabalhoAba(String pfRetrabalhoAba) {
		this.pfRetrabalhoAba = pfRetrabalhoAba;
	}

	public String getPfRetrabalhoReferencia() {
		return pfRetrabalhoReferencia;
	}

	public void setPfRetrabalhoReferencia(String pfRetrabalhoReferencia) {
		this.pfRetrabalhoReferencia = pfRetrabalhoReferencia;
	}

	public String getPfFuncaoTransacionalAba() {
		return pfFuncaoTransacionalAba;
	}

	public void setPfFuncaoTransacionalAba(String pfFuncaoTransacionalAba) {
		this.pfFuncaoTransacionalAba = pfFuncaoTransacionalAba;
	}

	public String getPfFuncaoTransacionalReferencia() {
		return pfFuncaoTransacionalReferencia;
	}

	public void setPfFuncaoTransacionalReferencia(String pfFuncaoTransacionalReferencia) {
		this.pfFuncaoTransacionalReferencia = pfFuncaoTransacionalReferencia;
	}

	public String getTotalPfBrutoAba() {
		return totalPfBrutoAba;
	}

	public void setTotalPfBrutoAba(String totalPfBrutoAba) {
		this.totalPfBrutoAba = totalPfBrutoAba;
	}

	public String getTotalPfBrutoReferencia() {
		return totalPfBrutoReferencia;
	}

	public void setTotalPfBrutoReferencia(String totalPfBrutoReferencia) {
		this.totalPfBrutoReferencia = totalPfBrutoReferencia;
	}

	public String getScopeCreepAba() {
		return scopeCreepAba;
	}

	public void setScopeCreepAba(String scopeCreepAba) {
		this.scopeCreepAba = scopeCreepAba;
	}

	public String getScopeCreepReferencia() {
		return scopeCreepReferencia;
	}

	public void setScopeCreepReferencia(String scopeCreepReferencia) {
		this.scopeCreepReferencia = scopeCreepReferencia;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}
	
	public String getMetodoContagemAba() {
		return metodoContagemAba;
	}

	public void setMetodoContagemAba(String metodoContagemAba) {
		this.metodoContagemAba = metodoContagemAba;
	}
	
	public String getMetodoContagemReferencia() {
		return metodoContagemReferencia;
	}

	public void setMetodoContagemReferencia(String metodoContagemReferencia) {
		this.metodoContagemReferencia = metodoContagemReferencia;
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
		TemplateContagem other = (TemplateContagem) obj;
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
