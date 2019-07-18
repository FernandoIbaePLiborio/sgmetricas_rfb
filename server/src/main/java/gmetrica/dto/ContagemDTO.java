package gmetrica.dto;

import java.math.BigDecimal;
import java.util.Date;

import gmetrica.enumeration.MetodoContagem;

public class ContagemDTO {

	private String projeto;
	private String tipoContagem;
	private Date data;
	private String tipoProjeto;
	private String responsavel;
	private BigDecimal pfBruto;
	private BigDecimal pfDemanda;
	private String situacao;
	private String demanda;
	private String metodoContagem;

	public ContagemDTO(String projeto, String tipoContagem, Date data, String tipoProjeto, String responsavel,
			BigDecimal pfBruto, BigDecimal pfDemanda, String situacao, String demanda, String metodoContagem) {
		this.projeto = projeto;
		this.tipoContagem = tipoContagem;
		this.data = data;
		this.tipoProjeto = tipoProjeto;
		this.responsavel = responsavel;
		this.pfBruto = pfBruto;
		this.pfDemanda = pfDemanda;
		this.situacao = situacao;
		this.demanda = demanda;
		this.metodoContagem = metodoContagem;
	}

	public String getProjeto() {
		return projeto;
	}

	public String getTipoContagem() {
		return tipoContagem;
	}

	public Date getData() {
		return data;
	}

	public String getTipoProjeto() {
		return tipoProjeto;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public BigDecimal getPfBruto() {
		return pfBruto;
	}

	public BigDecimal getPfDemanda() {
		return pfDemanda;
	}

	public String getSituacao() {
		return situacao;
	}

	public String getDemanda() {
		return demanda;
	}

	public String getMetodoContagem() {
		return metodoContagem;
	}
	
}
