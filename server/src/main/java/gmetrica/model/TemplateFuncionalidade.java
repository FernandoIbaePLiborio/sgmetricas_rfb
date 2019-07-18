package gmetrica.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import gmetrica.enumeration.TipoFuncionalidade;

@Entity
@Table(name = "template_funcionalidade")
public class TemplateFuncionalidade implements Serializable {
	
	
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 6170106143895516944L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_template")
	private Template template = new Template();
	
	@Column(name = "tipo_funcionalidade")
	@Enumerated(EnumType.STRING)
	private TipoFuncionalidade tipoFuncionalidade;
	
	@Column(name = "nome_funcionalidade_aba")
    private String nomeFuncionalidadeAba;
	
	@Column(name = "nome_funcionalidade_referencia")
    private String nomeFuncionalidadeReferencia;
	
	@Column(name = "tipo_aba")
    private String tipoAba;
	
	@Column(name = "tipo_referencia")
    private String tipoReferencia;
	
	@Column(name = "tipo_demanda_aba")
    private String tipoDemandaAba;
	
	@Column(name = "tipo_demanda_referencia")
    private String tipoDemandaReferencia;
	
	@Column(name = "rl_ar_aba")
    private String rlArAba;
	
	@Column(name = "rl_ar_referencia")
    private String rlArReferencia;
	
	@Column(name = "memoria_rl_ar_aba")
    private String memoriaRlArAba;
	
	@Column(name = "memoria_rl_ar_referencia")
    private String memoriaRlArReferencia;
	
	@Column(name = "rastreabilidade_aba")
    private String rastreabilidadeAba;
	
	@Column(name = "rastreabilidade_referencia")
    private String rastreabilidadeReferencia;
	
	@Column(name = "td_aba")
    private String tdAba;
	
	@Column(name = "td_referencia")
    private String tdReferencia;
	
	@Column(name = "memoria_td_aba")
    private String memoriaTdAba;
	
	@Column(name = "memoria_td_referencia")
    private String memoriaTdReferencia;
	
	@Column(name = "classificacao_aba")
    private String classificacaoAba;
	
	@Column(name = "classificacao_referencia")
    private String classificacaoReferencia;
	
	@Column(name = "complexidade_aba")
    private String complexidadeAba;
	
	@Column(name = "complexidade_referencia")
    private String complexidadeReferencia;
	
	@Column(name = "ponto_funcao_aba")
    private String pontoFuncaoAba;
	
	@Column(name = "ponto_funcao_referencia")
    private String pontoFuncaoReferencia;
	
	@Column(name = "divergencias_aba")
    private String divergenciasAba;
	
	@Column(name = "divergencias_referencia")
    private String divergenciasReferencia;
	
	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public TipoFuncionalidade getTipoFuncionalidade() {
		return tipoFuncionalidade;
	}

	public void setTipoFuncionalidade(TipoFuncionalidade tipoFuncionalidade) {
		this.tipoFuncionalidade = tipoFuncionalidade;
	}

	public String getNomeFuncionalidadeAba() {
		return nomeFuncionalidadeAba;
	}

	public void setNomeFuncionalidadeAba(String nomeFuncionalidadeAba) {
		this.nomeFuncionalidadeAba = nomeFuncionalidadeAba;
	}

	public String getNomeFuncionalidadeReferencia() {
		return nomeFuncionalidadeReferencia;
	}

	public void setNomeFuncionalidadeReferencia(String nomeFuncionalidadeReferencia) {
		this.nomeFuncionalidadeReferencia = nomeFuncionalidadeReferencia;
	}

	public String getTipoAba() {
		return tipoAba;
	}

	public void setTipoAba(String tipoAba) {
		this.tipoAba = tipoAba;
	}

	public String getTipoReferencia() {
		return tipoReferencia;
	}

	public void setTipoReferencia(String tipoReferencia) {
		this.tipoReferencia = tipoReferencia;
	}

	public String getTipoDemandaAba() {
		return tipoDemandaAba;
	}

	public void setTipoDemandaAba(String tipoDemandaAba) {
		this.tipoDemandaAba = tipoDemandaAba;
	}

	public String getTipoDemandaReferencia() {
		return tipoDemandaReferencia;
	}

	public void setTipoDemandaReferencia(String tipoDemandaReferencia) {
		this.tipoDemandaReferencia = tipoDemandaReferencia;
	}

	public String getRlArAba() {
		return rlArAba;
	}

	public void setRlArAba(String rlArAba) {
		this.rlArAba = rlArAba;
	}

	public String getRlArReferencia() {
		return rlArReferencia;
	}

	public void setRlArReferencia(String rlArReferencia) {
		this.rlArReferencia = rlArReferencia;
	}

	public String getMemoriaRlArAba() {
		return memoriaRlArAba;
	}

	public void setMemoriaRlArAba(String memoriaRlArAba) {
		this.memoriaRlArAba = memoriaRlArAba;
	}

	public String getMemoriaRlArReferencia() {
		return memoriaRlArReferencia;
	}

	public void setMemoriaRlArReferencia(String memoriaRlArReferencia) {
		this.memoriaRlArReferencia = memoriaRlArReferencia;
	}

	public String getRastreabilidadeAba() {
		return rastreabilidadeAba;
	}

	public void setRastreabilidadeAba(String rastreabilidadeAba) {
		this.rastreabilidadeAba = rastreabilidadeAba;
	}

	public String getRastreabilidadeReferencia() {
		return rastreabilidadeReferencia;
	}

	public void setRastreabilidadeReferencia(String rastreabilidadeReferencia) {
		this.rastreabilidadeReferencia = rastreabilidadeReferencia;
	}

	public String getTdAba() {
		return tdAba;
	}

	public void setTdAba(String tdAba) {
		this.tdAba = tdAba;
	}

	public String getTdReferencia() {
		return tdReferencia;
	}

	public void setTdReferencia(String tdReferencia) {
		this.tdReferencia = tdReferencia;
	}

	public String getMemoriaTdAba() {
		return memoriaTdAba;
	}

	public void setMemoriaTdAba(String memoriaTdAba) {
		this.memoriaTdAba = memoriaTdAba;
	}

	public String getMemoriaTdReferencia() {
		return memoriaTdReferencia;
	}

	public void setMemoriaTdReferencia(String memoriaTdReferencia) {
		this.memoriaTdReferencia = memoriaTdReferencia;
	}

	public String getClassificacaoAba() {
		return classificacaoAba;
	}

	public void setClassificacaoAba(String classificacaoAba) {
		this.classificacaoAba = classificacaoAba;
	}

	public String getClassificacaoReferencia() {
		return classificacaoReferencia;
	}

	public void setClassificacaoReferencia(String classificacaoReferencia) {
		this.classificacaoReferencia = classificacaoReferencia;
	}

	public String getComplexidadeAba() {
		return complexidadeAba;
	}

	public void setComplexidadeAba(String complexidadeAba) {
		this.complexidadeAba = complexidadeAba;
	}

	public String getComplexidadeReferencia() {
		return complexidadeReferencia;
	}

	public void setComplexidadeReferencia(String complexidadeReferencia) {
		this.complexidadeReferencia = complexidadeReferencia;
	}

	public String getPontoFuncaoAba() {
		return pontoFuncaoAba;
	}

	public void setPontoFuncaoAba(String pontoFuncaoAba) {
		this.pontoFuncaoAba = pontoFuncaoAba;
	}

	public String getPontoFuncaoReferencia() {
		return pontoFuncaoReferencia;
	}

	public void setPontoFuncaoReferencia(String pontoFuncaoReferencia) {
		this.pontoFuncaoReferencia = pontoFuncaoReferencia;
	}

	public String getDivergenciasAba() {
		return divergenciasAba;
	}

	public void setDivergenciasAba(String divergenciasAba) {
		this.divergenciasAba = divergenciasAba;
	}

	public String getDivergenciasReferencia() {
		return divergenciasReferencia;
	}

	public void setDivergenciasReferencia(String divergenciasReferencia) {
		this.divergenciasReferencia = divergenciasReferencia;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((tipoFuncionalidade == null) ? 0 : tipoFuncionalidade.hashCode());
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
		TemplateFuncionalidade other = (TemplateFuncionalidade) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (tipoFuncionalidade != other.tipoFuncionalidade)
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
