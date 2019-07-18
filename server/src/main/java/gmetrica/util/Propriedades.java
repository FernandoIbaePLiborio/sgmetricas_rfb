package gmetrica.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Propriedades {

	private static ResourceBundle propertiesResources;
	private static final Logger log = LoggerFactory.getLogger(Propriedades.class);

	static {
		try {
			propertiesResources = ResourceBundle.getBundle("gmetrica.util.Application");
		} catch (Exception e) {
			log.error("Não foi possível acessar arquivo de properties com as propriedades do sistema.", e);
			propertiesResources = null;
		}
	}

	public static String getPropriedade(String key) {

		String msg = null;

		try {
			msg = propertiesResources.getString(key);
		} catch (MissingResourceException mre) {
			log.debug("Codigo da propriedade não encontrado", mre);
		} catch (Exception e) {
			log.error("Não foi possível acessar arquivo de properties.", e);
			propertiesResources = null;
		}

		// Retornando mensagem
		return (msg == null ? "" : msg);
	}
}
