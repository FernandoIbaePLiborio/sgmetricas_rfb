package gmetrica.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.demoiselle.jee.core.api.crud.Result;
import org.demoiselle.jee.crud.AbstractBusiness;
import org.demoiselle.jee.crud.DemoiselleRequestContext;

import gmetrica.dao.ContagemDAO;
import gmetrica.dto.ContagemDTO;
import gmetrica.enumeration.TipoExportacao;
import gmetrica.filter.ContagemFiltro;
import gmetrica.model.Contagem;
import gmetrica.util.Constantes;
import gmetrica.util.RelatorioHelper;
import gmetrica.util.TreatReflect;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Stateless
public class ContagemBusiness extends AbstractBusiness<Contagem, Long> {
	
	@Inject
	private RelatorioHelper relatorioHelper;
	
	@Inject
    private DemoiselleRequestContext drc;
	
	@Inject
	private ContagemDAO contagemDAO;

	/**
	 * Realiza a exportação da grid
	 * @param context
	 * @param contagemFiltro
	 * @param tipoExportacao
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public String exportacaoGrid(ServletContext context, ContagemFiltro contagemFiltro, TipoExportacao tipoExportacao) throws IllegalArgumentException, IllegalAccessException{
		List<ContagemDTO> dtoList = new ArrayList<>();
		drc.setPaginationEnabled(false);
		drc.setFilters(TreatReflect.filtroConsulta(Contagem.class, contagemFiltro));
		Result resultado = dao.find();
		
		for(Object object : resultado.getContent()){
			Contagem contagem = (Contagem) object;
			ContagemDTO dto = new ContagemDTO(
					contagem.getProjeto(), // projeto, 
					contagem.getTipoContagem() != null ? contagem.getTipoContagem().getDescricao() : null, // tipoContagem, 
					contagem.getDataImportacao(), // data, 
					contagem.getTipoProjeto() != null ? contagem.getTipoProjeto().getDescricao() : null, // tipoProjeto, 
					contagem.getResponsavel(), // responsavel, 
					contagem.getTotalPfBruto(), // pfBruto, 
					contagem.getTotalPfDemanda(), // pfDemanda, 
					contagem.getSituacao() != null ? contagem.getSituacao().getDescricao() : null, // situacao,
					contagem.getNumeroDemanda(), // demanda
					contagem.getMetodoContagem() != null ? contagem.getMetodoContagem().getDescricao() : null // metodoContagem
					);
			dtoList.add(dto);
		}
		
		// Parametros
		Map<String, Object> pMapParametros = new HashMap<>();
		JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(dtoList, false);
		String rel64 = relatorioHelper.relatorioGenerico(
				Constantes.pathRelatorio(context.getRealPath("/")) + "contagem-grid", 
				pMapParametros, 
				datasource, 
				tipoExportacao);
		return rel64;
	}
	
	/**
	 * Realiza a busca de fronteitas
	 * @param fronteira
	 * @return
	 */
	public List<String> buscaFronteiras(String fronteira){
		List<Contagem> lista = contagemDAO.listarPorFiltro(fronteira, null);
		Set<String> novaList = new LinkedHashSet<>();
		for(Contagem c : lista){
			novaList.add((c.getFronteira()));
		}
		List<String> fronteiras = new ArrayList<>(novaList);
		Collections.sort(fronteiras);
		return fronteiras;
	}
	
	/**
	 * Realiza a busca de projetos
	 * @param projeto
	 * @return
	 */
	public List<String> buscaProjetos(String projeto){
		List<Contagem> lista = contagemDAO.listarPorFiltro(null, projeto);
		Set<String> novaList = new LinkedHashSet<>();
		for(Contagem c : lista){
			novaList.add((c.getProjeto()));
		}
		List<String> projetos = new ArrayList<>(novaList);
		Collections.sort(projetos);
		return projetos;
	}
}
