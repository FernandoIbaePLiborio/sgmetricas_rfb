package gmetrica.enumeration;

public enum TipoProjeto {
	
	APURACAO_ESPECIAL ("Apuração Especial"),
	DESENVOLVIMENTO ("Desenvolvimento"),
	MANUTENCAO_ADAPTATIVA ("Manutenção Adaptativa"),
	MANUTENCAO_CORRETIVA ("Manutenção Corretiva"),
	MANUTENCAO_EVOLUTIVA ("Manutenção Evolutiva");
	
	private String descricao;
	
	private TipoProjeto(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static TipoProjeto fromDescricao(String descricao) {
		if(descricao == null) {
			return null;
		}
		
		for(TipoProjeto valor: TipoProjeto.values()) {
			
			if(valor.getDescricao().equals(descricao)) {
				return valor;
			}
		}
		
		return null;
	}
	
	public static TipoProjeto fromContains(String descricao) {
		if(descricao == null) {
			return null;
		}
		
		for(TipoProjeto valor: TipoProjeto.values()) {
			descricao = descricao.substring(0,1).toUpperCase().concat(descricao.substring(1));
			if(valor.getDescricao().contains(descricao)) {
				return valor;
			}
		}
		
		return null;
	}
}
