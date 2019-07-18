package gmetrica.enumeration;

public enum TipoDemanda {
	
	DESENVOLVIMENTO ("Desenvolvimento"),
	MELHORIA ("Melhoria"),
	COMPONENTE_INTERNO_REUSAVEL ("Componente Interno Reusável"),
	RETRABALHO_ALTERACAO_REQUISITOS ("Retrabalho Alteração Requisitos"),
	DW_FILTRO_SEGURANCA ("DW Filtro Segurança"),
	DW_FILTRO_RELATORIO ("DW Filtro Relatório"),
	DW_DIMENSAO_ESTATICA ("DW Dimensão Estática"),
	DW_METADADOS ("DW Metadados"),
	DW_METADADOS_CODE_DATA ("DW Metadados Code Data"),
	DW_REORGANIZACAO_BANCADA ("DW Reorganização Bancada");
	
	private String descricao;
	
	private TipoDemanda(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static TipoDemanda fromDescricao(String descricao) {
		if(descricao == null) {
			return null;
		}
		
		for(TipoDemanda valor: TipoDemanda.values()) {
			
			if(valor.getDescricao().equals(descricao)) {
				return valor;
			}
		}
		
		return null;
	}
}
