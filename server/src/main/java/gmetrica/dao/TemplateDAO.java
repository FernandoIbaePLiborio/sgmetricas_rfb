package gmetrica.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.SerializationUtils;
import org.demoiselle.jee.core.api.crud.Result;
import org.demoiselle.jee.crud.DemoiselleRequestContext;
import org.demoiselle.jee.crud.exception.DemoiselleCrudException;
import org.demoiselle.jee.crud.pagination.PaginationHelperConfig;
import org.demoiselle.jee.crud.pagination.ResultSet;
import org.demoiselle.jee.crud.sort.CrudSort;

import gmetrica.enumeration.SituacaoTemplate;
import gmetrica.model.Template;
import gmetrica.model.TemplateContagem;
import gmetrica.model.TemplateFuncionalidade;

public class TemplateDAO extends GMetricasAbstractDAO<Template, Long> {

	@Inject
    private PaginationHelperConfig paginationConfig;
	
	@Inject
    private DemoiselleRequestContext drc;
	
	private Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * Método sobreescrito para poder realizar o fetch, pois por padrão ele não é feito.
	 */
	@Override
	public Template find(Long id) {
		try {
			CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<Template> criteria = builder.createQuery(Template.class);
			Root<Template> root = criteria.from(Template.class);
			criteria.select(root);

			Collection<Predicate> condicoes = new ArrayList<>();

			// id
			condicoes.add(builder.equal(root.get("id").as(Long.class), id));

			criteria.where(condicoes.toArray(new Predicate[condicoes.size()]));
			root.fetch("templateContagem", JoinType.LEFT);
			root.fetch("templateFuncionalidadeList", JoinType.LEFT);

			List<Template> lista = getEntityManager().createQuery(criteria).getResultList();
			if(lista.isEmpty()){
				return null;
			}
			Template template = SerializationUtils.clone(lista.get(0));
			for (TemplateFuncionalidade tf : template.getTemplateFuncionalidadeList()) {
				tf.setTemplate(null);
			}
			if(template.getTemplateContagem() != null){
				template.getTemplateContagem().setTemplate(null);
			}
			else {
				template.setTemplateContagem(new TemplateContagem());
			}
			
			return template;

		} catch (Exception e) {
			logger.severe(e.getMessage());
			throw new DemoiselleCrudException("Não foi possível consultar", e);
		}
	}

	/**
	 * Este método foi sobreescrito pois o Demoiselle não faz filtro quando o campo é enum.
	 * Não efetua a conversão de string para enum.
	 * E também não efetua o filtro por parte do nome, apenas pela string exata.
	 * @since 25/11/2017
	 */
	@Override
    public Result find() {

        try {
            Result result = new ResultSet();

            CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Template> criteriaQuery = criteriaBuilder.createQuery(Template.class);

            configureCriteriaQuery(criteriaBuilder, criteriaQuery);

            TypedQuery<Template> query = getEntityManager().createQuery(criteriaQuery);

            if(drc.isPaginationEnabled()){
                Integer firstResult = drc.getOffset() == null ? 0 : drc.getOffset();
                Integer maxResults = getMaxResult();
                Long count = count();
    
                if (firstResult < count) {
                    query.setFirstResult(firstResult);
                    query.setMaxResults(maxResults);
                }
    
                drc.setCount(count);
            }

            result.setContent(query.getResultList());
            if(result.getContent() != null && !result.getContent().isEmpty() 
                    && drc.isPaginationEnabled()
                    && result.getContent().size() <= drc.getCount() && drc.getCount() < getMaxResult()){
                    drc.setLimit(drc.getCount().intValue());
            }
            
            drc.setEntityClass(Template.class);

            return result;

        } 
        catch (Exception e) {
            logger.severe(e.getMessage());
            throw new DemoiselleCrudException("Não foi possível consultar", e);
        }
    }
	
	/**
	 * Configura a query
	 * @param criteriaBuilder
	 * @param criteriaQuery
	 */
	private void configureCriteriaQuery(CriteriaBuilder criteriaBuilder, CriteriaQuery<Template> criteriaQuery) {
        Root<Template> from = criteriaQuery.from(Template.class);
        if (drc.getFilters() != null) {
            criteriaQuery.select(from).where(buildPredicates(criteriaBuilder, criteriaQuery, from));
        }

        configureOrder(criteriaBuilder, criteriaQuery, from);
    }
	
	/**
	 * Realiza os filtros
	 * @param criteriaBuilder
	 * @param criteriaQuery
	 * @param root
	 * @return
	 */
	private Predicate[] buildPredicates(CriteriaBuilder criteriaBuilder, CriteriaQuery<?> criteriaQuery, Root<Template> root) {
        List<Predicate> predicates = new LinkedList<>();

        if(drc.getFilters() != null){
            drc.getFilters().getChildren().stream().forEach( child -> {
                
                List<Predicate> predicateAndKeys = new LinkedList<>();
                List<Predicate> predicateSameKey = new LinkedList<>();
    
                // Many parameters for the same key, generate OR clause
                if (!child.getChildren().isEmpty()) {
                    
                    Join<?, ?> join = root.join(child.getKey());
                    child.getChildren().stream().forEach( values -> {
                        
                        predicateSameKey.clear();
                        
                        if(!child.getChildren().isEmpty()){
                            
                            values.getValue().stream().forEach( value ->{
                                if ("null".equals(value) || value == null){
                                    predicateSameKey.add(criteriaBuilder.isNull(join.get(values.getKey())));
                                }
                                else{
                                    if (values.getValue().isEmpty()) {
                                        predicateSameKey.add(criteriaBuilder.isEmpty(join.get(values.getKey())));
                                    } 
                                    else {
                                        predicateSameKey.add(criteriaBuilder.equal(join.get(values.getKey()), value));
                                    }
                                }
                            });
                            
                            predicates.add(criteriaBuilder.or(predicateSameKey.toArray(new Predicate[]{})));
                        }
                    });
                } 
                else {
                    child.getValue().stream().forEach( value -> {      
                        if ("null".equals(value) || value == null){
                            predicateAndKeys.add(criteriaBuilder.isNull(root.get(child.getKey())));
                        } 
                        else{
                            if(child.getValue().isEmpty()) {
                                predicateAndKeys.add(criteriaBuilder.isEmpty(root.get(child.getKey())));
                            }
                            else {
                            	if(child.getKey().equals("situacao")){
            						String sit = (String) value;
            						SituacaoTemplate valor = SituacaoTemplate.valueOf(sit);
            						predicateAndKeys.add(criteriaBuilder.equal(root.get("situacao").as(SituacaoTemplate.class), valor));
            					}
                            	else {
                            		Object objeto = value;
                            		if(objeto.getClass().equals(String.class)){
                            			String texto = (String) objeto;
                            			predicateAndKeys.add(criteriaBuilder.like(criteriaBuilder.upper(root.get(child.getKey())), "%" + texto.toUpperCase() + "%"));
                            		}
                            		else {
                            			predicateAndKeys.add(criteriaBuilder.equal(root.get(child.getKey()), value));
                            		}
                            	}
                            }
                        }
                    });
                    
                    predicates.add(criteriaBuilder.and(predicateAndKeys.toArray(new Predicate[]{})));
                }
            });
        }

        return predicates.toArray(new Predicate[]{});
    }

	/**
	 * Configura a ordenação
	 * @param criteriaBuilder
	 * @param criteriaQuery
	 * @param root
	 */
    private void configureOrder(CriteriaBuilder criteriaBuilder, CriteriaQuery<Template> criteriaQuery, Root<Template> root) {

        if (!drc.getSorts().isEmpty()) {
            List<Order> orders = new ArrayList<>();

            drc.getSorts().stream().forEachOrdered( sortModel -> {
                
                if(sortModel.getType().equals(CrudSort.ASC)){
                    orders.add(criteriaBuilder.asc(root.get(sortModel.getField())));
                }
                else{
                    orders.add(criteriaBuilder.desc(root.get(sortModel.getField())));
                }
            });

            criteriaQuery.orderBy(orders);
        }

    }
	
    /**
     * Retorna o total de resultados
     * @return
     */
	private Integer getMaxResult() {
        if (drc.getLimit() == null && drc.getOffset() == null) {
            return paginationConfig.getDefaultPagination();
        }

        return (drc.getLimit() - drc.getOffset()) + 1;
    }
	
	/**
	 * Retorna o total de resultados
	 */
	@Override
	public Long count() {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> countCriteria = criteriaBuilder.createQuery(Long.class);
        Root<Template> entityRoot = countCriteria.from(Template.class);
        countCriteria.select(criteriaBuilder.count(entityRoot));
        
        if(drc.getFilters() != null){
            countCriteria.where(buildPredicates(criteriaBuilder, countCriteria, entityRoot));
        }

        return getEntityManager().createQuery(countCriteria).getSingleResult();
    }
}
