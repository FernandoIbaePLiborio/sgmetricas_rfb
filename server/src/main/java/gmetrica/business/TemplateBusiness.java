package gmetrica.business;

import javax.inject.Inject;

import org.apache.commons.lang3.SerializationUtils;
import org.demoiselle.jee.core.exception.DemoiselleException;
import org.demoiselle.jee.crud.AbstractBusiness;

import gmetrica.model.Template;
import gmetrica.model.TemplateFuncionalidade;
import gmetrica.util.AppMessage;

public class TemplateBusiness extends AbstractBusiness<Template, Long> {

	@Inject
    private AppMessage messages;
	
	/**
	 * Problema de recursividade, por isso foi realizado tratamento para retorno.
	 */
	@Override
    public Template persist(Template template) {
		validacao(template);
		for(TemplateFuncionalidade tf : template.getTemplateFuncionalidadeList()){
			tf.setTemplate(template);
		}
		template.getTemplateContagem().setTemplate(template);
		
		// Realiza a inclusão
		Template novo = SerializationUtils.clone(dao.persist(template));
		for(TemplateFuncionalidade tf : novo.getTemplateFuncionalidadeList()){
			tf.setTemplate(null);
		}
		novo.getTemplateContagem().setTemplate(null);
		
        return novo;
    }

	/**
	 * Problema de recursividade, por isso foi realizado tratamento para retorno.
	 */
	@Override
	public Template mergeFull(Template template) {
		validacao(template);
		for(TemplateFuncionalidade tf : template.getTemplateFuncionalidadeList()){
			tf.setTemplate(template);
		}
		template.getTemplateContagem().setTemplate(template);
		
		// Realiza a inclusão
		Template novo = SerializationUtils.clone(dao.mergeFull(template));
		for(TemplateFuncionalidade tf : novo.getTemplateFuncionalidadeList()){
			tf.setTemplate(null);
		}
		novo.getTemplateContagem().setTemplate(null);
		
        return novo;
	}
	
	/**
	 * Realiza a validação das regras de negócio
	 * @param template
	 */
	private void validacao(Template template) {
		if(template.getNome().equals("123")){
			throw new DemoiselleException(messages.mensagemDeTeste());
		}
	}
}
