package gmetrica.enumeration;

public enum TipoContagem {
	
	CONTAGEM_REFERENCIA ("Contagem de Referência"),
	ESTIMATIVA_INICIAL ("Estimativa Inicial"),
	CONTAGEM_ENCERRAMENTO ("Contagem de Encerramento");
	
	private String descricao;
	
	private TipoContagem(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static TipoContagem fromDescricao(String descricao) {
		if(descricao == null) {
			return null;
		}
		
		for(TipoContagem valor: TipoContagem.values()) {
			
			if(valor.getDescricao().equals(descricao)) {
				return valor;
			}
		}
		
		return null;
	}
	
	public static TipoContagem fromContains(String descricao) {
		if(descricao == null) {
			return null;
		}
		
		for(TipoContagem valor: TipoContagem.values()) {
			descricao = descricao.substring(0,1).toUpperCase().concat(descricao.substring(1));
			if(valor.getDescricao().contains(descricao)) {
				return valor;
			}
		}
		
		return null;
	}
}
