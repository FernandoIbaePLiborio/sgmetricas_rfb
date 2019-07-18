package gmetrica.business;

import javax.inject.Inject;

import org.demoiselle.jee.core.exception.DemoiselleException;
import org.demoiselle.jee.crud.AbstractBusiness;

import gmetrica.model.Endereco;
import gmetrica.util.AppMessage;

public class EnderecoBusiness extends AbstractBusiness<Endereco, Long> {

	@Inject
    private AppMessage messages;
	
	/**
	 * 
	 */
	@Override
    public Endereco persist(Endereco endereco) {
		if(endereco.getCep().equals("123")){
			throw new DemoiselleException(messages.mensagemDeTeste());
		}
        return dao.persist(endereco);
    }
}
