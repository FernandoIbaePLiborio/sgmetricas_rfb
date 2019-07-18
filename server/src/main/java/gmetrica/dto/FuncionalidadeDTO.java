package gmetrica.dto;

import java.math.BigDecimal;

public class FuncionalidadeDTO {

	private String tipo;
	private String nome;
	private String classificacao;
	private String complexidade;
	private BigDecimal pontoFuncao;
	private Integer td;
	private Integer lrAr;

	public FuncionalidadeDTO(String tipo, String nome, String classificacao, String complexidade,
			BigDecimal pontoFuncao, Integer td, Integer lrAr) {
		this.tipo = tipo;
		this.nome = nome;
		this.classificacao = classificacao;
		this.complexidade = complexidade;
		this.pontoFuncao = pontoFuncao;
		this.td = td;
		this.lrAr = lrAr;
	}

	public String getTipo() {
		return tipo;
	}

	public String getNome() {
		return nome;
	}

	public String getClassificacao() {
		return classificacao;
	}

	public String getComplexidade() {
		return complexidade;
	}

	public BigDecimal getPontoFuncao() {
		return pontoFuncao;
	}

	public Integer getTd() {
		return td;
	}

	public Integer getLrAr() {
		return lrAr;
	}
}
