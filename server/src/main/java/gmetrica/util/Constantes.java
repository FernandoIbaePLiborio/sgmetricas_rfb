package gmetrica.util;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Constantes {

	public static final int PAGINACAO = 100;
	
	public static <T> String campos(Class<T> klazz) {
		List<String> campos = new ArrayList<>();
        Field[] fields = klazz.getDeclaredFields();
        System.out.printf("%d fields:%n", fields.length);
        for (Field field : fields) {
        	campos.add(field.getName());
        }
        return String.join(",", campos);
    }
	
	public static String pathRelatorio(String path){
		return path + File.separator + "reports" + File.separator;
	}
}
