package gmetrica.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.demoiselle.jee.core.api.crud.Result;
import org.demoiselle.jee.crud.AbstractREST;
import org.demoiselle.jee.crud.Search;
import org.demoiselle.jee.security.annotation.Authenticated;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import gmetrica.business.ContagemBusiness;
import gmetrica.enumeration.TipoExportacao;
import gmetrica.filter.ContagemFiltro;
import gmetrica.model.Contagem;
import gmetrica.util.Constantes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@Api("contagem")
@ApiImplicitParams({
    @ApiImplicitParam(name = "Authorization", value = "JWT token",
            required = true, dataType = "string", paramType = "header")
})
@Authenticated
@Path("contagem")
public class ContagemResource extends AbstractREST<Contagem, Long> {
	
	@Context
	private ServletContext context;
	
	@Inject
	private ContagemBusiness contagemBusiness;

	/**
	 * Retorna a lista de contagens cadastradas
	 */
    @GET
    @Search(fields = {"id", "projeto", "linguagem", "tipoProjeto", "tipoContagem", "plataforma", "liderProjeto", "fronteira", "dataImportacao", "dataImportacaoDe", "dataImportacaoAte", "fornecedor", "numeroDemanda", "situacao", "propositoEscopo", "artefatosUsadosContagem", "observacoes", "responsavel", "ScopeCreep", "totalPfDemanda", "pfFuncaoDados", "pfFuncaoTransacional", "pfRetrabalho", "totalPfBruto", "totalPfBrutoDe", "totalPfBrutoAte", "totalPfDemandaDe", "totalPfDemandaAte"}, 
    	withPagination=true, quantityPerPage=Constantes.PAGINACAO)
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
	@Override
    public Result find() {
        return bc.find();
    }
    
    /**
     * Realiza a exportação da grid de contagem em formato PDF
     * @param contagemFiltro
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @POST
	@Path("exportacaoPDF")
	@Consumes(MediaType.APPLICATION_JSON)
    public String relatorioBaselinePDF(ContagemFiltro contagemFiltro) throws IllegalArgumentException, IllegalAccessException{
    	Gson gson = new GsonBuilder().create();
		return gson.toJson(contagemBusiness.exportacaoGrid(context, contagemFiltro, TipoExportacao.PDF));
    }
    
    /**
     * Realiza a exportação da grid de contagem em formato XLS
     * @param contagemFiltro
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @POST
	@Path("exportacaoXLS")
	@Consumes(MediaType.APPLICATION_JSON)
    public String relatorioBaselineXLS(ContagemFiltro contagemFiltro) throws IllegalArgumentException, IllegalAccessException{
    	Gson gson = new GsonBuilder().create();
		return gson.toJson(contagemBusiness.exportacaoGrid(context, contagemFiltro, TipoExportacao.EXCEL));
    }
    
    /**
     * Retorna a lista de fronteiras existentes
     * @param fronteira
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @POST
	@Path("fronteira")
	@Consumes(MediaType.APPLICATION_JSON)
    public List<String> fronteira(String fronteira) throws IllegalArgumentException, IllegalAccessException{
    	return contagemBusiness.buscaFronteiras(fronteira);
    }
    
    /**
     * Retorna a lista de projetos existentes
     * @param projeto
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @POST
	@Path("projeto")
	@Consumes(MediaType.APPLICATION_JSON)
    public List<String> projeto(String projeto) throws IllegalArgumentException, IllegalAccessException{
    	return contagemBusiness.buscaProjetos(projeto);
    }
}