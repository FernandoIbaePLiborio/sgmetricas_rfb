package gmetrica.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
import javax.persistence.Transient;

import gmetrica.enumeration.Classificacao;
import gmetrica.enumeration.Complexidade;
import gmetrica.enumeration.Situacao;
import gmetrica.enumeration.TipoDemanda;
import gmetrica.enumeration.TipoFuncionalidade;
import gmetrica.enumeration.TipoRelatorio;

@Entity
@Table(name = "funcionalidade")
public class Funcionalidade implements Serializable {
	
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 6170106143895516944L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_contagem")
	private Contagem contagem = new Contagem();
	
	@Column(name = "nome")
    private String nome;
    
	@Column(name = "tipo")
	@Enumerated(EnumType.STRING)
	private TipoFuncionalidade tipo;
	
	@Column(name = "tipo_demanda")
	@Enumerated(EnumType.STRING)
	private TipoDemanda tipoDemanda;
	
	@Column(name = "rl_ar")
	private Integer rlAr;
	
	@Column(name = "memoria_rl_ar")
	private String memoriaRlAr;
	
	@Column(name = "td")
	private Integer td;
	
	@Column(name = "memoria_td")
	private String memoriaTd;
	
	@Column(name = "classificacao")
	@Enumerated(EnumType.STRING)
	private Classificacao classificacao;
	
	@Column(name = "complexidade")
	@Enumerated(EnumType.STRING)
	private Complexidade complexidade;
	
	@Column(name = "ponto_funcao")
	private BigDecimal pontoFuncao;
	
	@Column(name = "rastreabilidade_justificativa")
	private String rastreabilidadeJustificativa;
	
	@Column(name = "divergencias")
	private String divergencias;
	
	@Transient
	private TipoRelatorio tipoRelatorio;
	
	@Transient
	private String fronteira;
	
	@Transient
	private String projeto;
	
	@Transient
	private Date dataImportacaoDe;
	
	@Transient
	private Date dataImportacaoAte;
	
	@Transient
	private Date dataImportacao;
	
	@Transient
	private Situacao situacaoContagem;
	
	@Transient
	private String numeroDemanda;

	public Contagem getContagem() {
		return contagem;
	}

	public void setContagem(Contagem contagem) {
		this.contagem = contagem;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public TipoFuncionalidade getTipo() {
		return tipo;
	}

	public void setTipo(TipoFuncionalidade tipo) {
		this.tipo = tipo;
	}

	public TipoDemanda getTipoDemanda() {
		return tipoDemanda;
	}

	public void setTipoDemanda(TipoDemanda tipoDemanda) {
		this.tipoDemanda = tipoDemanda;
	}

	public Integer getRlAr() {
		return rlAr;
	}

	public void setRlAr(Integer rlAr) {
		this.rlAr = rlAr;
	}

	public String getMemoriaRlAr() {
		return memoriaRlAr;
	}

	public void setMemoriaRlAr(String memoriaRlAr) {
		this.memoriaRlAr = memoriaRlAr;
	}

	public Integer getTd() {
		return td;
	}

	public void setTd(Integer td) {
		this.td = td;
	}

	public String getMemoriaTd() {
		return memoriaTd;
	}

	public void setMemoriaTd(String memoriaTd) {
		this.memoriaTd = memoriaTd;
	}

	public Classificacao getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(Classificacao classificacao) {
		this.classificacao = classificacao;
	}

	public Complexidade getComplexidade() {
		return complexidade;
	}

	public void setComplexidade(Complexidade complexidade) {
		this.complexidade = complexidade;
	}

	public BigDecimal getPontoFuncao() {
		return pontoFuncao;
	}

	public void setPontoFuncao(BigDecimal pontoFuncao) {
		this.pontoFuncao = pontoFuncao;
	}

	public String getRastreabilidadeJustificativa() {
		return rastreabilidadeJustificativa;
	}

	public void setRastreabilidadeJustificativa(String rastreabilidadeJustificativa) {
		this.rastreabilidadeJustificativa = rastreabilidadeJustificativa;
	}

	public String getDivergencias() {
		return divergencias;
	}

	public void setDivergencias(String divergencias) {
		this.divergencias = divergencias;
	}

	public TipoRelatorio getTipoRelatorio() {
		return tipoRelatorio;
	}

	public void setTipoRelatorio(TipoRelatorio tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public String getFronteira() {
		return fronteira;
	}

	public void setFronteira(String fronteira) {
		this.fronteira = fronteira;
	}

	public String getProjeto() {
		return projeto;
	}

	public void setProjeto(String projeto) {
		this.projeto = projeto;
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
	
	public Date getDataImportacao() {
		return dataImportacao;
	}

	public void setDataImportacao(Date dataImportacao) {
		this.dataImportacao = dataImportacao;
	}

	public Situacao getSituacaoContagem() {
		return situacaoContagem;
	}

	public void setSituacaoContagem(Situacao situacaoContagem) {
		this.situacaoContagem = situacaoContagem;
	}

	public String getNumeroDemanda() {
		return numeroDemanda;
	}

	public void setNumeroDemanda(String numeroDemanda) {
		this.numeroDemanda = numeroDemanda;
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
		Funcionalidade other = (Funcionalidade) obj;
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
