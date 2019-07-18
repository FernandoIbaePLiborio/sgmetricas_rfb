package gmetrica.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

public class TreatNumber {

	public static double converte(String arg) throws ParseException{
	    //obtem um NumberFormat para o Locale default (BR)
	    NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
	    //converte um número com vírgulas ex: 2,56 para double
	    double number = nf.parse(arg).doubleValue();
	    return number;
	}
	
	/**
	 * Gera um identificador aleatório
	 * @return
	 */
	public static Long identificador(){
		int valor = Calendar.getInstance().get(Calendar.MILLISECOND);
		return Long.valueOf(valor * -1);
	}
}
