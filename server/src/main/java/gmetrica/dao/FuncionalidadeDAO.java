package gmetrica.dao;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.demoiselle.jee.core.api.crud.Result;
import org.demoiselle.jee.crud.DemoiselleRequestContext;
import org.demoiselle.jee.crud.exception.DemoiselleCrudException;
import org.demoiselle.jee.crud.pagination.PaginationHelperConfig;
import org.demoiselle.jee.crud.pagination.ResultSet;
import org.demoiselle.jee.crud.sort.CrudSort;

import gmetrica.enumeration.Classificacao;
import gmetrica.enumeration.Complexidade;
import gmetrica.enumeration.Situacao;
import gmetrica.enumeration.TipoDemanda;
import gmetrica.enumeration.TipoFuncionalidade;
import gmetrica.enumeration.TipoRelatorio;
import gmetrica.model.Funcionalidade;
import gmetrica.util.TreatDate;

public class FuncionalidadeDAO extends GMetricasAbstractDAO<Funcionalidade, Long> {
	
	@Inject
    private PaginationHelperConfig paginationConfig;
	
	@Inject
    private DemoiselleRequestContext drc;
	
	private Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * Este método foi sobreescrito pois não foi possível realizar o filtro de funcionalidades pelo objeto Contagem.
	 * Acredito que por se tratar de objeto complexo.
	 * @since 25/11/2017
	 */
	@Override
    public Result find() {

		try {
            Result result = new ResultSet();
            CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Funcionalidade> criteriaQuery = criteriaBuilder.createQuery(Funcionalidade.class);

            configureCriteriaQuery(criteriaBuilder, criteriaQuery);

            TypedQuery<Funcionalidade> query = getEntityManager().createQuery(criteriaQuery);
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
            drc.setEntityClass(Funcionalidade.class);
            return result;
        }
        catch (Exception e) {
            logger.severe(e.getMessage());
            throw new DemoiselleCrudException("NÃ£o foi possÃ­vel consultar", e);
        }
    }
	
	/**
	 * Configura a query
	 * @param criteriaBuilder
	 * @param criteriaQuery
	 */
	private void configureCriteriaQuery(CriteriaBuilder criteriaBuilder, CriteriaQuery<Funcionalidade> criteriaQuery) {
        Root<Funcionalidade> from = criteriaQuery.from(Funcionalidade.class);
        if (drc.getFilters() != null) {
        	criteriaQuery.select(from).where(buildPredicates(criteriaBuilder, criteriaQuery, from));
        }
        configureOrder(criteriaBuilder, criteriaQuery, from);
    }
	
	/**
	 * Configura a ordenação
	 * @param criteriaBuilder
	 * @param criteriaQuery
	 * @param root
	 */
    private void configureOrder(CriteriaBuilder criteriaBuilder, CriteriaQuery<Funcionalidade> criteriaQuery, Root<Funcionalidade> root) {

        if (!drc.getSorts().isEmpty()) {
            List<Order> orders = new ArrayList<>();
            drc.getSorts().stream().forEachOrdered( sortModel -> {
            	if (sortModel.getField().equals("numeroDemanda") || sortModel.getField().equals("dataImportacao")){
            		if(sortModel.getType().equals(CrudSort.ASC)){
                        orders.add(criteriaBuilder.asc(root.get("contagem").get(sortModel.getField())));
                    }
                    else {
                        orders.add(criteriaBuilder.desc(root.get("contagem").get(sortModel.getField())));
                    }
            	} else {
            		if(sortModel.getType().equals(CrudSort.ASC)){
            			orders.add(criteriaBuilder.asc(root.get(sortModel.getField())));
            		}
            		else{
            			orders.add(criteriaBuilder.desc(root.get(sortModel.getField())));
            		}
            	}
            });
            criteriaQuery.orderBy(orders);
        }
    }
	
	/**
	 * Configura os filtros
	 * @param criteriaBuilder
	 * @param criteriaQuery
	 * @param root
	 * @return Predicate[]
	 */
	private Predicate[] buildPredicates(CriteriaBuilder criteriaBuilder, CriteriaQuery<?> criteriaQuery, Root<Funcionalidade> root) {
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
                        if ("null".equals(value) || value == null || "undefined".equals(value)){
                            predicateAndKeys.add(criteriaBuilder.isNull(root.get(child.getKey())));
                        } 
                        else{
                            if(child.getValue().isEmpty()) {
                                predicateAndKeys.add(criteriaBuilder.isEmpty(root.get(child.getKey())));
                            }
                            else {
                            	if(child.getKey().equals("contagem")){
                            		logger.info(value);
            						Long valor = Long.valueOf(value).longValue();
            						if(valor != 0L){
            							predicateAndKeys.add(criteriaBuilder.equal(root.get("contagem").get("id").as(Long.class), valor));
            						}
            					}
                            	else if(child.getKey().equals("complexidade")){
            						String sit = (String) value;
            						Complexidade valor = Complexidade.fromContains(sit);
            						predicateAndKeys.add(criteriaBuilder.equal(root.get("complexidade").as(Complexidade.class), valor));
            					}
                            	else if(child.getKey().equals("classificacao")){
            						String sit = (String) value;
            						Classificacao valor = Classificacao.fromContains(sit);
            						predicateAndKeys.add(criteriaBuilder.equal(root.get("classificacao").as(Classificacao.class), valor));
            					}
                            	else if(child.getKey().equals("tipo")){
            						String sit = (String) value;
            						TipoFuncionalidade valor = TipoFuncionalidade.fromContains(sit);
            						predicateAndKeys.add(criteriaBuilder.equal(root.get("tipo").as(TipoFuncionalidade.class), valor));
            					}
                            	else if(child.getKey().equals("pontoFuncao")){
                            		String valor = (String) value;
                            		BigDecimal pontoFuncao = new BigDecimal(valor);
                            		predicateAndKeys.add(criteriaBuilder.equal(root.get("pontoFuncao").as(BigDecimal.class), pontoFuncao));
                            	}
                            	else if(child.getKey().equals("td")){
                            		String td = (String) value;
                            		predicateAndKeys.add(criteriaBuilder.equal(root.get("td").as(Integer.class), td));
                            	}
                            	else if(child.getKey().equals("numeroDemanda")){
                            		String numeroDemanda = (String) value;
                            		predicateAndKeys.add(criteriaBuilder.like(root.get("contagem").get("numeroDemanda").as(String.class), "%" + numeroDemanda  + "%"));
                            	}
                            	else if(child.getKey().equals("rlAr")){
                            		String valor = (String) value;
                            		Integer rlAr = new Integer(valor);
                            		predicateAndKeys.add(criteriaBuilder.equal(root.get("rlAr").as(Integer.class), rlAr));
                            	}
                            	else if(child.getKey().equals("situacaoContagem")){
            						String sit = (String) value;
            						Situacao valor = Situacao.valueOf(sit);
            						predicateAndKeys.add(criteriaBuilder.equal(root.get("contagem").get("situacao").as(Situacao.class), valor));
            					}
                            	else if(child.getKey().equals("projeto")){
                            		String valor = (String) value;
                            		predicateAndKeys.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("contagem").get("projeto").as(String.class)), "%" + valor.toUpperCase() + "%"));
                            	}
                            	else if(child.getKey().equals("fronteira")){
                            		String valor = (String) value;
                            		predicateAndKeys.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("contagem").get("fronteira").as(String.class)), "%" + valor.toUpperCase() + "%"));
                            	}
                            	else if(child.getKey().equals("dataImportacaoDe")){
            						String valor = (String) value;
            						Date dataImportacaoDe = TreatDate.parseDate(valor);
            						if(dataImportacaoDe == null){
            							dataImportacaoDe = TreatDate.parseDate2(valor);
            						}
            						predicateAndKeys.add(criteriaBuilder.greaterThanOrEqualTo(root.get("contagem").get("dataImportacao").as(Date.class), dataImportacaoDe));
            					}
                            	else if(child.getKey().equals("dataImportacaoAte")){
            						String valor = (String) value;
            						Date dataImportacaoAte = TreatDate.parseDate(valor);
            						if(dataImportacaoAte == null){
            							dataImportacaoAte = TreatDate.parseDate2(valor);
            						}
            						predicateAndKeys.add(criteriaBuilder.lessThanOrEqualTo(root.get("contagem").get("dataImportacao").as(Date.class), dataImportacaoAte));
            					}
                            	else if(child.getKey().equals("dataImportacao")){
            						String valor = (String) value;
            						Date dataImportacao = TreatDate.parseDate(valor);
            						if(dataImportacao == null){
            							dataImportacao = TreatDate.parseDate2(valor);
            							if (dataImportacao == null){
            								SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                							try {
    											dataImportacao = formatter.parse(valor);
    										} catch (ParseException e) {
    											e.printStackTrace();
    										}
            							}
            						}
            						predicateAndKeys.add(criteriaBuilder.like(root.get("contagem").get("dataImportacao"), "%"+ dataImportacao +"%"));
            					}
                            	else if(child.getKey().equals("tipoRelatorio")){
                            		String sit = (String) value;
            						TipoRelatorio valor = TipoRelatorio.fromContains(sit);
            						if (valor == null){
            							valor = TipoRelatorio.valueOf(sit);
            						}
            						String classificacao = "Excluída";
            						Classificacao excluida = Classificacao.fromContains(classificacao);
            						predicateAndKeys.add(criteriaBuilder.notEqual(root.get("classificacao").as(Classificacao.class), excluida));
            						if (valor.equals(TipoRelatorio.BASELINE)){
            							CriteriaQuery<Funcionalidade> criteriaQ = criteriaBuilder.createQuery(Funcionalidade.class);
            							Subquery<Date> subQuery = criteriaQ.subquery(Date.class);
            							Root<Funcionalidade> rootSubQuery = subQuery.from(Funcionalidade.class);
            							subQuery.select(criteriaBuilder.greatest(rootSubQuery.<Date>get("contagem").get("dataImportacao").as(Date.class)))
            							.where(criteriaBuilder.equal(root.get("nome"), rootSubQuery.get("nome")), criteriaBuilder.equal(root.get("contagem").get("fronteira"), rootSubQuery.get("contagem").get("fronteira")));
            							predicateAndKeys.add(criteriaBuilder.equal(root.get("contagem").get("dataImportacao"), subQuery));
            							predicateAndKeys.add(root.get("tipoDemanda").as(TipoDemanda.class).in(TipoDemanda.values()));
            						}
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
        Root<Funcionalidade> entityRoot = countCriteria.from(Funcionalidade.class);
        countCriteria.select(criteriaBuilder.count(entityRoot));
        
        if(drc.getFilters() != null){
            countCriteria.where(buildPredicates(criteriaBuilder, countCriteria, entityRoot));
        }

        return getEntityManager().createQuery(countCriteria).getSingleResult();
    }
}
