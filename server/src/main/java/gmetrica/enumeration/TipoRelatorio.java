package gmetrica.enumeration;

public enum TipoRelatorio {
	
	TODAS ("Todas as funcionalidades"),
	BASELINE ("Baseline");
	
	private String descricao;
	
	private TipoRelatorio(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static TipoRelatorio fromDescricao(String descricao) {
		if(descricao == null) {
			return null;
		}
		
		for(TipoRelatorio valor: TipoRelatorio.values()) {
			
			if(valor.getDescricao().equals(descricao)) {
				return valor;
			}
		}
		
		return null;
	}
	
	public static TipoRelatorio fromContains(String descricao) {
		if(descricao == null) {
			return null;
		}
		
		for(TipoRelatorio valor: TipoRelatorio.values()) {
			descricao = descricao.substring(0,1).toUpperCase().concat(descricao.substring(1));
			if(valor.getDescricao().contains(descricao)) {
				return valor;
			}
		}
		
		return null;
	}
}
