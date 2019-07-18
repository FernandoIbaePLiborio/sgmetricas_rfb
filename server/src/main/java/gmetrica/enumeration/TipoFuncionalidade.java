package gmetrica.enumeration;

public enum TipoFuncionalidade {
	
	ALI ("ALI"),
	AIE ("AIE"),
	EE ("EE"),
	CE ("CE"),
	SE ("SE");
	
	private String descricao;
	
	private TipoFuncionalidade(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static TipoFuncionalidade fromDescricao(String descricao) {
		if(descricao == null) {
			return null;
		}
		
		for(TipoFuncionalidade valor: TipoFuncionalidade.values()) {
			
			if(valor.getDescricao().equals(descricao)) {
				return valor;
			}
		}
		
		return null;
	}
	
	public static TipoFuncionalidade fromContains(String descricao) {
		if(descricao == null) {
			return null;
		}
		
		for(TipoFuncionalidade valor: TipoFuncionalidade.values()) {
			descricao = descricao.toUpperCase().trim();
			if(valor.getDescricao().toUpperCase().contains(descricao)) {
				return valor;
			}
		}
		
		return null;
	}
}
