package gmetrica.util;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.demoiselle.jee.crud.TreeNodeField;

public class TreatReflect {

	public static <K, J> TreeNodeField<String, Set<String>> filtroConsulta(Class<K> saida, J entrada) throws IllegalArgumentException, IllegalAccessException{
		TreeNodeField<String, Set<String>> root = new TreeNodeField<String, Set<String>>(saida.toString(), new HashSet<>());
		for (Field f : entrada.getClass().getDeclaredFields()){
			f.setAccessible(true);
			if(f.getName().equals("offset") || f.getName().equals("limit") || f.getName().equals("desc")){
				continue;
			}
			if (f.get(entrada) != null){
				Set<String> campos = new HashSet<>();
				campos.add(f.get(entrada).toString());
				root.addChild(f.getName(), campos);
			}
		}
		
		return root;
	}
}
