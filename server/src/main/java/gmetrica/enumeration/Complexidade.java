package gmetrica.enumeration;

import java.text.Normalizer;

public enum Complexidade {
	
	BAIXA ("Baixa"),
	MEDIA ("Média"),
	ALTA ("Alta");
	
	private String descricao;
	
	private Complexidade(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static Complexidade fromDescricao(String descricao) {
		if(descricao == null) {
			return null;
		}
		
		for(Complexidade valor: Complexidade.values()) {
			
			if(valor.getDescricao().equals(descricao)) {
				return valor;
			}
		}
		
		return null;
	}
	
	public static Complexidade fromContains(String descricao) {
		if(descricao == null) {
			return null;
		}
		
		for(Complexidade valor: Complexidade.values()) {
			descricao = removerAcentos(descricao);
			
			if(removerAcentos(valor.getDescricao()).contains(descricao)) {
				return valor;
			}
		}
		return null;
	}
	
	public static String removerAcentos(String str) {
	    return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toUpperCase();
	}
	
}
