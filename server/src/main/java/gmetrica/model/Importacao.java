package gmetrica.model;

import java.util.Date;

import gmetrica.enumeration.Fornecedor;

public class Importacao {
	
	private String arquivoBase64;
	private Long idTemplate;
	private Fornecedor fornecedor;
	private String numeroDemanda;
	private Date data;
	
	public String getArquivoBase64() {
		return arquivoBase64;
	}
	
	public void setArquivoBase64(String arquivoBase64) {
		this.arquivoBase64 = arquivoBase64;
	}
	
	public Long getIdTemplate() {
		return idTemplate;
	}

	public void setIdTemplate(Long idTemplate) {
		this.idTemplate = idTemplate;
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
	
	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}
}
