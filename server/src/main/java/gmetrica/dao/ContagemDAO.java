package gmetrica.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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
import org.apache.commons.lang3.StringUtils;
import org.demoiselle.jee.core.api.crud.Result;
import org.demoiselle.jee.crud.DemoiselleRequestContext;
import org.demoiselle.jee.crud.exception.DemoiselleCrudException;
import org.demoiselle.jee.crud.pagination.PaginationHelperConfig;
import org.demoiselle.jee.crud.pagination.ResultSet;
import org.demoiselle.jee.crud.sort.CrudSort;

import gmetrica.dto.ResumoContagemDTO;
import gmetrica.enumeration.Complexidade;
import gmetrica.enumeration.Fornecedor;
import gmetrica.enumeration.MetodoContagem;
import gmetrica.enumeration.Situacao;
import gmetrica.enumeration.TipoContagem;
import gmetrica.enumeration.TipoFuncionalidade;
import gmetrica.enumeration.TipoProjeto;
import gmetrica.model.Contagem;
import gmetrica.model.Funcionalidade;
import gmetrica.util.TreatDate;

public class ContagemDAO extends GMetricasAbstractDAO<Contagem, Long> {

	private static final String RETRABALHO = "RETRABALHO";

	@Inject
    private PaginationHelperConfig paginationConfig;
	
	@Inject
    private DemoiselleRequestContext drc;
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	/**
	 * Fui obrigado a sobreescrever o método pois estava dando problema de abertura de transação
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public Contagem persist(Contagem contagem) {
		this.getEntityManager().persist(contagem);
		return null;
	}
	
	/**
	 * Realiza a busca de contagem por determinados filtros
	 * @param fronteira
	 * @param projeto
	 * @return
	 */
	public List<Contagem> listarPorFiltro(String fronteira, String projeto){
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Contagem> criteria = builder.createQuery(Contagem.class);
		Root<Contagem> root = criteria.from(Contagem.class);
		
		criteria.select(root);

		Collection<Predicate> condicoes = new ArrayList<>();
			
		// fronteira
		if (StringUtils.isNotBlank(fronteira)) {
			condicoes.add(builder.like(builder.upper(root.get("fronteira").as(String.class)), "%" + fronteira.replace("\"", "").trim().toUpperCase() + "%"));
		}
		
		// projeto
		if (StringUtils.isNotBlank(projeto)) {
			condicoes.add(builder.like(builder.upper(root.get("projeto").as(String.class)), "%" + projeto.replace("\"", "").trim().toUpperCase() + "%"));
		}
		
		criteria.where(condicoes.toArray(new Predicate[condicoes.size()]));
		criteria.orderBy(builder.asc(root.get("numeroDemanda")));

		return getEntityManager().createQuery(criteria).getResultList();
	}

	/**
	 * Método sobreescrito para poder efetuar o fetch no relacionamento
	 */
	@Override
	public Contagem find(Long id) {
		try {
			CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<Contagem> criteria = builder.createQuery(Contagem.class);
			Root<Contagem> root = criteria.from(Contagem.class);
			criteria.select(root);

			Collection<Predicate> condicoes = new ArrayList<>();

			// id
			condicoes.add(builder.equal(root.get("id").as(Long.class), id));

			criteria.where(condicoes.toArray(new Predicate[condicoes.size()]));
			root.fetch("funcionalidadeList", JoinType.LEFT);

			List<Contagem> lista = getEntityManager().createQuery(criteria).getResultList();
			if(lista.isEmpty()){
				return null;
			}
			Set<Contagem> contagemSet = new HashSet<Contagem>();
			contagemSet.addAll(lista);
			Contagem contagem = SerializationUtils.clone(contagemSet.iterator().next());
			for (Funcionalidade funcionalidade : contagem.getFuncionalidadeList()) {
				funcionalidade.setContagem(null);
			}
			
			// Gera o resumo da contagem
			contagem.setResumoContagem(geraResumoContagem(contagem));
			
			return contagem;

		} catch (Exception e) {
			logger.severe(e.getMessage());
			throw new DemoiselleCrudException("Não foi possível consultar", e);
		}
	}
	
	/**
	 * Monta a visão do resumo da contagem
	 * @param contagem
	 * @return
	 */
	private ResumoContagemDTO geraResumoContagem(Contagem contagem){
		ResumoContagemDTO dto = new ResumoContagemDTO();
		dto.setAliFuncaoQuantidadeBaixa(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.ALI.equals(w.getTipo()) && w.getComplexidade() != null && w.getTipoDemanda() != null && Complexidade.BAIXA.equals(w.getComplexidade()) && !RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToLong(w->w.getId()).count());
		dto.setAliFuncaoQuantidadeMedia(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.ALI.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.MEDIA.equals(w.getComplexidade()) && !RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToLong(w->w.getId()).count());
		dto.setAliFuncaoQuantidadeAlta(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.ALI.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.ALTA.equals(w.getComplexidade()) && !RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToLong(w->w.getId()).count());
		dto.setAliFuncaoPFBaixa(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.ALI.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.BAIXA.equals(w.getComplexidade()) && !RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToDouble(w->w.getPontoFuncao().doubleValue()).sum());
		dto.setAliFuncaoPFMedia(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.ALI.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.MEDIA.equals(w.getComplexidade()) && !RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToDouble(w->w.getPontoFuncao().doubleValue()).sum());
		dto.setAliFuncaoPFAlta(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.ALI.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.ALTA.equals(w.getComplexidade()) && !RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToDouble(w->w.getPontoFuncao().doubleValue()).sum());
		dto.setAliRetrabalhoQuantidadeBaixa(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.ALI.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.BAIXA.equals(w.getComplexidade()) && RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToLong(w->w.getId()).count());
		dto.setAliRetrabalhoQuantidadeMedia(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.ALI.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.MEDIA.equals(w.getComplexidade()) && RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToLong(w->w.getId()).count());
		dto.setAliRetrabalhoQuantidadeAlta(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.ALI.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.ALTA.equals(w.getComplexidade()) && RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToLong(w->w.getId()).count());
		dto.setAliRetrabalhoPFBaixa(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.ALI.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.BAIXA.equals(w.getComplexidade()) && RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToDouble(w->w.getPontoFuncao().doubleValue()).sum());
		dto.setAliRetrabalhoPFMedia(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.ALI.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.MEDIA.equals(w.getComplexidade()) && RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToDouble(w->w.getPontoFuncao().doubleValue()).sum());
		dto.setAliRetrabalhoPFAlta(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.ALI.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.ALTA.equals(w.getComplexidade()) && RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToDouble(w->w.getPontoFuncao().doubleValue()).sum());
		dto.setAieFuncaoQuantidadeBaixa(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.AIE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.BAIXA.equals(w.getComplexidade()) && !RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToLong(w->w.getId()).count());
		dto.setAieFuncaoQuantidadeMedia(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.AIE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.MEDIA.equals(w.getComplexidade()) && !RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToLong(w->w.getId()).count());
		dto.setAieFuncaoQuantidadeAlta(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.AIE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.ALTA.equals(w.getComplexidade()) && !RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToLong(w->w.getId()).count());
		dto.setAieFuncaoPFBaixa(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.AIE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.BAIXA.equals(w.getComplexidade()) && !RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToDouble(w->w.getPontoFuncao().doubleValue()).sum());
		dto.setAieFuncaoPFMedia(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.AIE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.MEDIA.equals(w.getComplexidade()) && !RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToDouble(w->w.getPontoFuncao().doubleValue()).sum());
		dto.setAieFuncaoPFAlta(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.AIE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.ALTA.equals(w.getComplexidade()) && !RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToDouble(w->w.getPontoFuncao().doubleValue()).sum());
		dto.setAieRetrabalhoQuantidadeBaixa(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.AIE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.BAIXA.equals(w.getComplexidade()) && RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToLong(w->w.getId()).count());
		dto.setAieRetrabalhoQuantidadeMedia(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.AIE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.MEDIA.equals(w.getComplexidade()) && RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToLong(w->w.getId()).count());
		dto.setAieRetrabalhoQuantidadeAlta(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.AIE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.ALTA.equals(w.getComplexidade()) && RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToLong(w->w.getId()).count());
		dto.setAieRetrabalhoPFBaixa(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.AIE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.BAIXA.equals(w.getComplexidade()) && RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToDouble(w->w.getPontoFuncao().doubleValue()).sum());
		dto.setAieRetrabalhoPFMedia(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.AIE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.MEDIA.equals(w.getComplexidade()) && RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToDouble(w->w.getPontoFuncao().doubleValue()).sum());
		dto.setAieRetrabalhoPFAlta(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.AIE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.ALTA.equals(w.getComplexidade()) && RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToDouble(w->w.getPontoFuncao().doubleValue()).sum());
		dto.setEeFuncaoQuantidadeBaixa(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.EE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.BAIXA.equals(w.getComplexidade()) && !RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToLong(w->w.getId()).count());
		dto.setEeFuncaoQuantidadeMedia(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.EE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.MEDIA.equals(w.getComplexidade()) && !RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToLong(w->w.getId()).count());
		dto.setEeFuncaoQuantidadeAlta(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.EE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.ALTA.equals(w.getComplexidade()) && !RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToLong(w->w.getId()).count());
		dto.setEeFuncaoPFBaixa(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.EE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.BAIXA.equals(w.getComplexidade()) && !RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToDouble(w->w.getPontoFuncao().doubleValue()).sum());
		dto.setEeFuncaoPFMedia(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.EE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.MEDIA.equals(w.getComplexidade()) && !RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToDouble(w->w.getPontoFuncao().doubleValue()).sum());
		dto.setEeFuncaoPFAlta(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.EE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.ALTA.equals(w.getComplexidade()) && !RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToDouble(w->w.getPontoFuncao().doubleValue()).sum());
		dto.setEeRetrabalhoQuantidadeBaixa(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.EE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.BAIXA.equals(w.getComplexidade()) && RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToLong(w->w.getId()).count());
		dto.setEeRetrabalhoQuantidadeMedia(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.EE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.MEDIA.equals(w.getComplexidade()) && RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToLong(w->w.getId()).count());
		dto.setEeRetrabalhoQuantidadeAlta(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.EE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.ALTA.equals(w.getComplexidade()) && RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToLong(w->w.getId()).count());
		dto.setEeRetrabalhoPFBaixa(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.EE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.BAIXA.equals(w.getComplexidade()) && RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToDouble(w->w.getPontoFuncao().doubleValue()).sum());
		dto.setEeRetrabalhoPFMedia(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.EE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.MEDIA.equals(w.getComplexidade()) && RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToDouble(w->w.getPontoFuncao().doubleValue()).sum());
		dto.setEeRetrabalhoPFAlta(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.EE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.ALTA.equals(w.getComplexidade()) && RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToDouble(w->w.getPontoFuncao().doubleValue()).sum());
		dto.setCeFuncaoQuantidadeBaixa(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.CE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.BAIXA.equals(w.getComplexidade()) && !RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToLong(w->w.getId()).count());
		dto.setCeFuncaoQuantidadeMedia(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.CE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.MEDIA.equals(w.getComplexidade()) && !RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToLong(w->w.getId()).count());
		dto.setCeFuncaoQuantidadeAlta(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.CE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.ALTA.equals(w.getComplexidade()) && !RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToLong(w->w.getId()).count());
		dto.setCeFuncaoPFBaixa(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.CE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.BAIXA.equals(w.getComplexidade()) && !RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToDouble(w->w.getPontoFuncao().doubleValue()).sum());
		dto.setCeFuncaoPFMedia(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.CE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.MEDIA.equals(w.getComplexidade()) && !RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToDouble(w->w.getPontoFuncao().doubleValue()).sum());
		dto.setCeFuncaoPFAlta(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.CE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.ALTA.equals(w.getComplexidade()) && !RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToDouble(w->w.getPontoFuncao().doubleValue()).sum());
		dto.setCeRetrabalhoQuantidadeBaixa(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.CE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.BAIXA.equals(w.getComplexidade()) && RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToLong(w->w.getId()).count());
		dto.setCeRetrabalhoQuantidadeMedia(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.CE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.MEDIA.equals(w.getComplexidade()) && RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToLong(w->w.getId()).count());
		dto.setCeRetrabalhoQuantidadeAlta(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.CE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.ALTA.equals(w.getComplexidade()) && RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToLong(w->w.getId()).count());
		dto.setCeRetrabalhoPFBaixa(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.CE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.BAIXA.equals(w.getComplexidade()) && RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToDouble(w->w.getPontoFuncao().doubleValue()).sum());
		dto.setCeRetrabalhoPFMedia(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.CE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.MEDIA.equals(w.getComplexidade()) && RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToDouble(w->w.getPontoFuncao().doubleValue()).sum());
		dto.setCeRetrabalhoPFAlta(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.CE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.ALTA.equals(w.getComplexidade()) && RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToDouble(w->w.getPontoFuncao().doubleValue()).sum());
		dto.setSeFuncaoQuantidadeBaixa(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.SE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.BAIXA.equals(w.getComplexidade()) && !RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToLong(w->w.getId()).count());
		dto.setSeFuncaoQuantidadeMedia(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.SE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.MEDIA.equals(w.getComplexidade()) && !RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToLong(w->w.getId()).count());
		dto.setSeFuncaoQuantidadeAlta(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.SE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.ALTA.equals(w.getComplexidade()) && !RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToLong(w->w.getId()).count());
		dto.setSeFuncaoPFBaixa(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.SE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.BAIXA.equals(w.getComplexidade()) && !RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToDouble(w->w.getPontoFuncao().doubleValue()).sum());
		dto.setSeFuncaoPFMedia(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.SE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.MEDIA.equals(w.getComplexidade()) && !RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToDouble(w->w.getPontoFuncao().doubleValue()).sum());
		dto.setSeFuncaoPFAlta(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.SE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.ALTA.equals(w.getComplexidade()) && !RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToDouble(w->w.getPontoFuncao().doubleValue()).sum());
		dto.setSeRetrabalhoQuantidadeBaixa(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.SE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.BAIXA.equals(w.getComplexidade()) && RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToLong(w->w.getId()).count());
		dto.setSeRetrabalhoQuantidadeMedia(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.SE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.MEDIA.equals(w.getComplexidade()) && RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToLong(w->w.getId()).count());
		dto.setSeRetrabalhoQuantidadeAlta(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.SE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.ALTA.equals(w.getComplexidade()) && RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToLong(w->w.getId()).count());
		dto.setSeRetrabalhoPFBaixa(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.SE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.BAIXA.equals(w.getComplexidade()) && RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToDouble(w->w.getPontoFuncao().doubleValue()).sum());
		dto.setSeRetrabalhoPFMedia(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.SE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.MEDIA.equals(w.getComplexidade()) && RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToDouble(w->w.getPontoFuncao().doubleValue()).sum());
		dto.setSeRetrabalhoPFAlta(contagem.getFuncionalidadeList().stream().filter(w->TipoFuncionalidade.SE.equals(w.getTipo()) && w.getComplexidade()!= null && w.getTipoDemanda() != null && Complexidade.ALTA.equals(w.getComplexidade()) && RETRABALHO.contains(w.getTipoDemanda().getDescricao().toUpperCase())).mapToDouble(w->w.getPontoFuncao().doubleValue()).sum());
		
		return dto;
	}
	
	/**
	 * Este método foi sobreescrito pois o Demoiselle não faz filtro para campos que não pertençam à entidade.
	 * @since 25/11/2017
	 */
	@Override
    public Result find() {

        try {
            Result result = new ResultSet();

            CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Contagem> criteriaQuery = criteriaBuilder.createQuery(Contagem.class);

            configureCriteriaQuery(criteriaBuilder, criteriaQuery);

            TypedQuery<Contagem> query = getEntityManager().createQuery(criteriaQuery);

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
            
            drc.setEntityClass(Contagem.class);

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
	private void configureCriteriaQuery(CriteriaBuilder criteriaBuilder, CriteriaQuery<Contagem> criteriaQuery) {
        Root<Contagem> from = criteriaQuery.from(Contagem.class);
        if (drc.getFilters() != null) {
            criteriaQuery.select(from).where(buildPredicates(criteriaBuilder, criteriaQuery, from));
        }

        configureOrder(criteriaBuilder, criteriaQuery, from);
    }
	
	/**
	 * Configura os filtros
	 * @param criteriaBuilder
	 * @param criteriaQuery
	 * @param root
	 * @return
	 */
	private Predicate[] buildPredicates(CriteriaBuilder criteriaBuilder, CriteriaQuery<?> criteriaQuery, Root<Contagem> root) {
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
                            	if(child.getKey().equals("tipoProjeto")){
            						String sit = (String) value;
            						TipoProjeto valor = TipoProjeto.fromContains(sit);
            						predicateAndKeys.add(criteriaBuilder.equal(root.get("tipoProjeto").as(TipoProjeto.class), valor));
            					}
                            	else if(child.getKey().equals("tipoContagem")){
            						String sit = (String) value;
            						TipoContagem valor = TipoContagem.fromContains(sit);
            						predicateAndKeys.add(criteriaBuilder.equal(root.get("tipoContagem").as(TipoContagem.class), valor));
            					}
                            	else if(child.getKey().equals("fornecedor")){
            						String sit = (String) value;
            						Fornecedor valor = Fornecedor.fromContains(sit);
            						predicateAndKeys.add(criteriaBuilder.equal(root.get("fornecedor").as(Fornecedor.class), valor));
            					}
                            	else if(child.getKey().equals("situacao")){
            						String sit = (String) value;
            						Situacao valor = Situacao.fromContains(sit);
            						predicateAndKeys.add(criteriaBuilder.equal(root.get("situacao").as(Situacao.class), valor));
            					}
                            	else if(child.getKey().equals("dataImportacaoDe")){
            						String valor = (String) value;
            						Date dataImportacaoDe = TreatDate.parseDate(valor);
            						predicateAndKeys.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dataImportacao").as(Date.class), dataImportacaoDe));
            					}
                            	else if(child.getKey().equals("dataImportacaoAte")){
            						String valor = (String) value;
            						Date dataImportacaoAte = TreatDate.parseDate(valor);
            						predicateAndKeys.add(criteriaBuilder.lessThanOrEqualTo(root.get("dataImportacao").as(Date.class), dataImportacaoAte));
            					}
                            	else if(child.getKey().equals("totalPfBrutoDe")){
                            		String valor = (String) value;
                            		BigDecimal totalPfBrutoDe = new BigDecimal(valor);
                            		predicateAndKeys.add(criteriaBuilder.greaterThanOrEqualTo(root.get("totalPfBruto").as(BigDecimal.class), totalPfBrutoDe));
                            	}
                            	else if(child.getKey().equals("totalPfBrutoAte")){
                            		String valor = (String) value;
                            		BigDecimal totalPfBrutoAte = new BigDecimal(valor);
                            		predicateAndKeys.add(criteriaBuilder.lessThanOrEqualTo(root.get("totalPfBruto").as(BigDecimal.class), totalPfBrutoAte));
                            	}
                            	else if(child.getKey().equals("totalPfDemandaDe")){
                            		String valor = (String) value;
                            		BigDecimal totalPfDemandaDe = new BigDecimal(valor);
                            		predicateAndKeys.add(criteriaBuilder.greaterThanOrEqualTo(root.get("totalPfDemanda").as(BigDecimal.class), totalPfDemandaDe));
                            	}
                            	else if(child.getKey().equals("totalPfDemandaAte")){
                            		String valor = (String) value;
                            		BigDecimal totalPfDemandaAte = new BigDecimal(valor);
                            		predicateAndKeys.add(criteriaBuilder.lessThanOrEqualTo(root.get("totalPfDemanda").as(BigDecimal.class), totalPfDemandaAte));
                            	}
                            	else if(child.getKey().equals("metodoContagem")){
            						String sit = (String) value;
            						MetodoContagem valor = MetodoContagem.fromContains(sit);
            						predicateAndKeys.add(criteriaBuilder.equal(root.get("metodoContagem").as(MetodoContagem.class), valor));
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
    private void configureOrder(CriteriaBuilder criteriaBuilder, CriteriaQuery<Contagem> criteriaQuery, Root<Contagem> root) {

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
        Root<Contagem> entityRoot = countCriteria.from(Contagem.class);
        countCriteria.select(criteriaBuilder.count(entityRoot));
        
        if(drc.getFilters() != null){
            countCriteria.where(buildPredicates(criteriaBuilder, countCriteria, entityRoot));
        }

        return getEntityManager().createQuery(countCriteria).getSingleResult();
    }
	
	/**
	 * Remove uma contagem e seus relacionamentos
	 */
	 @Override
	    public void remove(Long id) {
	        try {
	        	Contagem contagem = this.getEntityManager().find(Contagem.class, id);
	        	for (Funcionalidade funcionalidade : contagem.getFuncionalidadeList()) {
	        		 this.getEntityManager().remove(funcionalidade);
				}
	        	 this.getEntityManager().remove(contagem);
	        } catch (Exception e) {
	            throw new DemoiselleCrudException("Não foi possível excluir", e);
	        }
	    }
}
