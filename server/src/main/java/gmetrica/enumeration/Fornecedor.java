package gmetrica.enumeration;

public enum Fornecedor {
	
	DATAPREV ("Dataprev"),
	SERPRO ("Serpro"),
	FABRICA_SOFTWARE ("Fábrica de Software");
	
	private String descricao;

	private Fornecedor(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static Fornecedor fromDescricao(String descricao) {
		if(descricao == null) {
			return null;
		}
		
		for(Fornecedor valor: Fornecedor.values()) {
			
			if(valor.getDescricao().equals(descricao)) {
				return valor;
			}
		}
		
		return null;
	}
	
	public static Fornecedor fromContains(String descricao) {
		if(descricao == null) {
			return null;
		}
		
		for(Fornecedor valor: Fornecedor.values()) {
			descricao = descricao.substring(0,1).toUpperCase().concat(descricao.substring(1));
			if(valor.getDescricao().contains(descricao)) {
				return valor;
			}
		}
		
		return null;
	}
}
