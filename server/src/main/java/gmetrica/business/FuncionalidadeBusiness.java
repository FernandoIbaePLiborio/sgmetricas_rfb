package gmetrica.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.demoiselle.jee.core.api.crud.Result;
import org.demoiselle.jee.core.exception.DemoiselleException;
import org.demoiselle.jee.crud.AbstractBusiness;
import org.demoiselle.jee.crud.DemoiselleRequestContext;

import gmetrica.dto.FuncionalidadeDTO;
import gmetrica.dto.RelatorioBaselineDTO;
import gmetrica.enumeration.TipoExportacao;
import gmetrica.enumeration.TipoFuncionalidade;
import gmetrica.filter.FuncionalidadeFiltro;
import gmetrica.model.Funcionalidade;
import gmetrica.util.AppMessage;
import gmetrica.util.Constantes;
import gmetrica.util.RelatorioHelper;
import gmetrica.util.TreatReflect;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class FuncionalidadeBusiness extends AbstractBusiness<Funcionalidade, Long> {
	
	@Inject
    private AppMessage messages;
	
	@Inject
	private RelatorioHelper relatorioHelper;
	
	@Inject
    private DemoiselleRequestContext drc;
	
	/**
	 * Perisite uma funcionalidade
	 */
	@Override
    public Funcionalidade persist(Funcionalidade funcionalidade) {
		if(funcionalidade.getDivergencias().equals("123")){
			throw new DemoiselleException(messages.mensagemDeTeste());
		}
        return dao.persist(funcionalidade);
    }

	/**
	 * Atualiza uma funcionalidade
	 */
	@Override
	public Funcionalidade mergeFull(Funcionalidade funcionalidade) {

		return super.mergeFull(funcionalidade);
	}
	
	/**
	 * Gera o relatório de baseline
	 * @param context
	 * @param funcionalidadeFiltro
	 * @param tipoExportacao
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public String relatorioBaseline(ServletContext context, FuncionalidadeFiltro funcionalidadeFiltro, TipoExportacao tipoExportacao) throws IllegalArgumentException, IllegalAccessException{
		List<RelatorioBaselineDTO> dtoList = new ArrayList<>();
		drc.setPaginationEnabled(false);
		drc.setFilters(TreatReflect.filtroConsulta(Funcionalidade.class, funcionalidadeFiltro));
		Result resultado = dao.find();
		
		for(Object object : resultado.getContent()){
			Funcionalidade funcionalidade = (Funcionalidade) object;
			RelatorioBaselineDTO dto = new RelatorioBaselineDTO(
					funcionalidade.getNome(), // nome, 
					funcionalidade.getTipo() != null ? funcionalidade.getTipo().getDescricao() : null, // tipo, 
					funcionalidade.getComplexidade() != null ? funcionalidade.getComplexidade().getDescricao() : null, // complexidade, 
					funcionalidade.getContagem().getDataImportacao(), // dataContagem, 
					funcionalidade.getContagem().getNumeroDemanda(), // numeroDemanda, 
					null, // contagem, 
					funcionalidade.getPontoFuncao() // pontoFuncao
					);
			dtoList.add(dto);
		}
		
		// Parametros
		Map<String, Object> pMapParametros = new HashMap<>();
		JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(dtoList, false);
		String pdf = relatorioHelper.relatorioGenerico(
				Constantes.pathRelatorio(context.getRealPath("/")) + "relatorio-baseline", 
				pMapParametros, 
				datasource, 
				tipoExportacao);
		return pdf;
	}
	
	/**
	 * Gera o relatório da grid
	 * @param context
	 * @param funcionalidadeFiltro
	 * @param tipoExportacao
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public String exportacaoGrid(ServletContext context, FuncionalidadeFiltro funcionalidadeFiltro, TipoExportacao tipoExportacao) throws IllegalArgumentException, IllegalAccessException{
		List<FuncionalidadeDTO> dtoList = new ArrayList<>();
		drc.setPaginationEnabled(false);
		drc.setFilters(TreatReflect.filtroConsulta(Funcionalidade.class, funcionalidadeFiltro));
		Result resultado = dao.find();
		
		for(Object object : resultado.getContent()){
			Funcionalidade funcionalidade = (Funcionalidade) object;
			FuncionalidadeDTO dto = new FuncionalidadeDTO(
					funcionalidade.getTipo() != null ? funcionalidade.getTipo().getDescricao() : null, // tipo, 
					funcionalidade.getNome(), // nome, 
					funcionalidade.getClassificacao() != null ? funcionalidade.getClassificacao().getDescricao() : null, // classificacao, 
					funcionalidade.getComplexidade() != null ? funcionalidade.getComplexidade().getDescricao() : null, // complexidade, 
					funcionalidade.getPontoFuncao(), // pontoFuncao, 
					funcionalidade.getTd(), // td, 
					funcionalidade.getRlAr() // rlAr
					);
			dtoList.add(dto);
		}
		
		// Parametros
		Map<String, Object> pMapParametros = new HashMap<>();
		JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(dtoList, false);
		String rel64 = relatorioHelper.relatorioGenerico(
				Constantes.pathRelatorio(context.getRealPath("/")) + "funcionalidade-grid", 
				pMapParametros, 
				datasource, 
				tipoExportacao);
		return rel64;
	}
}
