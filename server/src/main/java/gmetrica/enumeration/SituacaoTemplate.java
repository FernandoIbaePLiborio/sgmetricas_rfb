package gmetrica.enumeration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum SituacaoTemplate {
	
	ATIVO ("Ativo"),
	INATIVO ("Inativo");
	
	private String descricao;
	
	private SituacaoTemplate(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static SituacaoTemplate fromDescricao(String descricao) {
		if(descricao == null) {
			return null;
		}
		
		for(SituacaoTemplate valor: SituacaoTemplate.values()) {
			
			if(valor.getDescricao().equals(descricao)) {
				return valor;
			}
		}
		
		return null;
	}
	
	@Override
    public String toString() {
        return this.descricao;
    }

    @SuppressWarnings("rawtypes")
	public static Map getMap() {
        Map<SituacaoTemplate, String> map = new ConcurrentHashMap<>();
        for (SituacaoTemplate userType : SituacaoTemplate.values()) {
            map.put(userType, userType.descricao);
        }
        return map;
    }
}
