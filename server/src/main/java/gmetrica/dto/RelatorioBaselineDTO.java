package gmetrica.dto;

import java.math.BigDecimal;
import java.util.Date;

public class RelatorioBaselineDTO {

	private String nome;
	private String tipo;
	private String complexidade;
	private Date dataContagem;
	private String numeroDemanda;
	private String contagem;
	private BigDecimal pontoFuncao;

	public RelatorioBaselineDTO(String nome, String tipo, String complexidade, Date dataContagem, String numeroDemanda,
			String contagem, BigDecimal pontoFuncao) {
		this.nome = nome;
		this.tipo = tipo;
		this.complexidade = complexidade;
		this.dataContagem = dataContagem;
		this.numeroDemanda = numeroDemanda;
		this.contagem = contagem;
		this.pontoFuncao = pontoFuncao;
	}

	public String getNome() {
		return nome;
	}

	public String getTipo() {
		return tipo;
	}

	public String getComplexidade() {
		return complexidade;
	}

	public Date getDataContagem() {
		return dataContagem;
	}

	public String getNumeroDemanda() {
		return numeroDemanda;
	}

	public String getContagem() {
		return contagem;
	}

	public BigDecimal getPontoFuncao() {
		return pontoFuncao;
	}

}
