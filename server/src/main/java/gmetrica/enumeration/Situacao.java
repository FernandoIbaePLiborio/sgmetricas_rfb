package gmetrica.enumeration;

public enum Situacao {
	
	PENDENTE ("Pendente"),
	CONCLUIDO ("Concluído");
	
	private String descricao;
	
	private Situacao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static Situacao fromDescricao(String descricao) {
		if(descricao == null) {
			return null;
		}
		
		for(Situacao valor: Situacao.values()) {
			
			if(valor.getDescricao().equals(descricao)) {
				return valor;
			}
		}
		
		return null;
	}
	
	public static Situacao fromContains(String descricao) {
		if(descricao == null) {
			return null;
		}
		
		for(Situacao valor: Situacao.values()) {
			descricao = descricao.substring(0,1).toUpperCase().concat(descricao.substring(1));
			if(valor.getDescricao().contains(descricao)) {
				return valor;
			}
		}
		
		return null;
	}
}
