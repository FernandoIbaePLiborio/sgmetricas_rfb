package gmetrica.enumeration;

import javax.validation.constraints.NotNull;

public enum TipoExportacao {
	
	PDF("PDF"),
	EXCEL("Excel");
	
	@NotNull
	private String descricao;
	
	private TipoExportacao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static TipoExportacao fromValue(String descricao) {
		if(descricao == null) {
			return null;
		}
		
		for(TipoExportacao valor: TipoExportacao.values()) {
			
			if(valor.getDescricao().equals(descricao)) {
				return valor;
			}
		}
		
		return null;
	}
}