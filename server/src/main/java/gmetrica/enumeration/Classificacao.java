package gmetrica.enumeration;

import java.text.Normalizer;

public enum Classificacao {
	
	INCLUIDA ("Incluída"),
	ALTERADA ("Alterada"),
	EXCLUIDA ("Excluída");
	
	private String descricao;
	
	private Classificacao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static Classificacao fromDescricao(String descricao) {
		if(descricao == null) {
			return null;
		}
		
		for(Classificacao valor: Classificacao.values()) {
			
			if(valor.getDescricao().equals(descricao)) {
				return valor;
			}
		}
		
		return null;
	}
	
	public static Classificacao fromContains(String descricao) {
		if(descricao == null) {
			return null;
		}
		
		for(Classificacao valor : Classificacao.values()) {
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
