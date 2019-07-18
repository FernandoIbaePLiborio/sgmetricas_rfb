package gmetrica.enumeration;

public enum ScopeCreep {
	
	SIM ("Sim"),
	NAO ("Não");
	
	private String descricao;
	
	private ScopeCreep(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static ScopeCreep fromDescricao(String descricao) {
		if(descricao == null) {
			return null;
		}
		
		for(ScopeCreep valor: ScopeCreep.values()) {
			
			if(valor.getDescricao().equals(descricao)) {
				return valor;
			}
		}
		
		return null;
	}
	
	public static ScopeCreep fromContains(String descricao) {
		if(descricao == null) {
			return null;
		}
		
		for(ScopeCreep valor: ScopeCreep.values()) {
			descricao = descricao.substring(0,1).toUpperCase().concat(descricao.substring(1));
			if(valor.getDescricao().contains(descricao)) {
				return valor;
			}
		}
		
		return null;
	}

}
