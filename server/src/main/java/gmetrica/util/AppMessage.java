package gmetrica.util;

import org.apache.deltaspike.core.api.message.MessageBundle;
import org.apache.deltaspike.core.api.message.MessageTemplate;

@MessageBundle
public interface AppMessage {

	@MessageTemplate("{mensagem-de-teste}")
    String mensagemDeTeste();
	
	@MessageTemplate("{mensagem-de-teste-com-parametro}")
	String mensagemDeTesteComParametro(String parametro1, String parametro2);
}
