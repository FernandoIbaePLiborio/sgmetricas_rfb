package gmetrica.filter;

import java.math.BigDecimal;
import java.util.Date;

import gmetrica.enumeration.Fornecedor;
import gmetrica.enumeration.Situacao;
import gmetrica.enumeration.TipoContagem;
import gmetrica.enumeration.TipoProjeto;

public class ContagemFiltro extends AbstractFiltro {

	private String projeto;
	private String linguagem;
	private TipoContagem tipoContagem;
	private TipoProjeto tipoProjeto;
	private String propositoEscopo;
	private Fornecedor fornecedor;
	private String fronteira;
	private Date dataImportacaoDe;
	private Date dataImportacaoAte;
	private BigDecimal pfBrutoDe;
	private BigDecimal pfBrutoAte;
	private BigDecimal pfDemandaDe;
	private BigDecimal pfDemandaAte;
	private String responsavel;
	private String numeroDemanda;
	private Situacao situacao;

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

	public TipoContagem getTipoContagem() {
		return tipoContagem;
	}

	public void setTipoContagem(TipoContagem tipoContagem) {
		this.tipoContagem = tipoContagem;
	}

	public TipoProjeto getTipoProjeto() {
		return tipoProjeto;
	}

	public void setTipoProjeto(TipoProjeto tipoProjeto) {
		this.tipoProjeto = tipoProjeto;
	}

	public String getPropositoEscopo() {
		return propositoEscopo;
	}

	public void setPropositoEscopo(String propositoEscopo) {
		this.propositoEscopo = propositoEscopo;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public String getFronteira() {
		return fronteira;
	}

	public void setFronteira(String fronteira) {
		this.fronteira = fronteira;
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

	public BigDecimal getPfBrutoDe() {
		return pfBrutoDe;
	}

	public void setPfBrutoDe(BigDecimal pfBrutoDe) {
		this.pfBrutoDe = pfBrutoDe;
	}

	public BigDecimal getPfBrutoAte() {
		return pfBrutoAte;
	}

	public void setPfBrutoAte(BigDecimal pfBrutoAte) {
		this.pfBrutoAte = pfBrutoAte;
	}

	public BigDecimal getPfDemandaDe() {
		return pfDemandaDe;
	}

	public void setPfDemandaDe(BigDecimal pfDemandaDe) {
		this.pfDemandaDe = pfDemandaDe;
	}

	public BigDecimal getPfDemandaAte() {
		return pfDemandaAte;
	}

	public void setPfDemandaAte(BigDecimal pfDemandaAte) {
		this.pfDemandaAte = pfDemandaAte;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
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
}
