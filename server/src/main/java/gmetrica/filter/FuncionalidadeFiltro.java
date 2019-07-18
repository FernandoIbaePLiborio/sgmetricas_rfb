package gmetrica.filter;

import java.math.BigDecimal;
import java.util.Date;

import gmetrica.enumeration.Classificacao;
import gmetrica.enumeration.Complexidade;
import gmetrica.enumeration.Situacao;
import gmetrica.enumeration.TipoFuncionalidade;
import gmetrica.enumeration.TipoRelatorio;

public class FuncionalidadeFiltro extends AbstractFiltro {

	private Long contagem;
	private TipoFuncionalidade tipo;
	private String nome;
	private Classificacao classificacao;
	private Complexidade complexidade;
	private BigDecimal pontoFuncao;
	private Integer td;
	private Integer lrAr;
	private TipoRelatorio tipoRelatorio;
    private Date dataImportacaoDe;
    private Date dataImportacaoAte;
    private String fronteira;
    private String projeto;
    private Situacao situacaoContagem;
    private String numeroDemanda;

	public Long getContagem() {
		return contagem;
	}

	public void setContagem(Long contagem) {
		this.contagem = contagem;
	}

	public TipoFuncionalidade getTipo() {
		return tipo;
	}

	public void setTipo(TipoFuncionalidade tipo) {
		this.tipo = tipo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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

	public Integer getTd() {
		return td;
	}

	public void setTd(Integer td) {
		this.td = td;
	}

	public Integer getLrAr() {
		return lrAr;
	}

	public void setLrAr(Integer lrAr) {
		this.lrAr = lrAr;
	}

	public TipoRelatorio getTipoRelatorio() {
		return tipoRelatorio;
	}

	public void setTipoRelatorio(TipoRelatorio tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
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
	
}
