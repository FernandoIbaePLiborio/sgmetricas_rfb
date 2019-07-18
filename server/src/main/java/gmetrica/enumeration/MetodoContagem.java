package gmetrica.enumeration;

public enum MetodoContagem {
	
	CONTAGEM_DETALHADA ("Contagem Detalhada"),
	CONTAGEM_ESTIMADA_NESMA ("Contagem Estimada NESMA"),
	CONTAGEM_ESTIMATIVA ("Contagem Estimativa");
	
	private String descricao;
	
	private MetodoContagem(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static MetodoContagem fromDescricao(String descricao) {
		if(descricao == null) {
			return null;
		}
		
		for(MetodoContagem valor: MetodoContagem.values()) {
			
			if(valor.getDescricao().equals(descricao)) {
				return valor;
			}
		}
		
		return null;
	}
	
	public static MetodoContagem fromContains(String descricao) {
		if(descricao == null) {
			return null;
		}
		
		for(MetodoContagem valor: MetodoContagem.values()) {
			descricao = descricao.substring(0,1).toUpperCase().concat(descricao.substring(1));
			if(valor.getDescricao().contains(descricao)) {
				return valor;
			}
		}
		
		return null;
	}

}
