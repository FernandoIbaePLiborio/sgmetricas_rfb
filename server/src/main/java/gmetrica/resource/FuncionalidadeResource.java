package gmetrica.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

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

import gmetrica.business.FuncionalidadeBusiness;
import gmetrica.enumeration.TipoExportacao;
import gmetrica.filter.FuncionalidadeFiltro;
import gmetrica.model.Funcionalidade;
import gmetrica.util.Constantes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@Api("funcionalidade")
@ApiImplicitParams({
    @ApiImplicitParam(name = "Authorization", value = "JWT token",
            required = true, dataType = "string", paramType = "header")
})
@Authenticated
@Path("funcionalidade")
public class FuncionalidadeResource extends AbstractREST<Funcionalidade, Long> {

	@Context
	private ServletContext context;
	
	@Inject
	private FuncionalidadeBusiness funcionalidadeBusiness;
	
	/**
	 * Retorna a lista de funcionalidades cadastradas
	 */
    @GET
    @Search(fields = {"id", "nome", "tipo", "tipoDemanda", "rlAr", "memoriaRlAr", "td", "memoriaTd", "classificacao", "complexidade", "pontoFuncao", "rastreabilidadeJustificativa", "divergencias", "contagem", "tipoRelatorio", "fronteira", "projeto", "dataImportacaoDe", "dataImportacaoAte", "dataImportacao", "situacaoContagem", "numeroDemanda"}, withPagination=true, quantityPerPage=Constantes.PAGINACAO)
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
	@Override
    public Result find() {
        return bc.find();
    }
    
    /**
     * Efetua a geração do relatório baseline em formato PDF
     * @param funcionalidadeFiltro
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @POST
	@Path("relatorioBaselinePDF")
	@Consumes(MediaType.APPLICATION_JSON)
    public String relatorioBaselinePDF(FuncionalidadeFiltro funcionalidadeFiltro) throws IllegalArgumentException, IllegalAccessException{
    	Gson gson = new GsonBuilder().create();
		return gson.toJson(funcionalidadeBusiness.relatorioBaseline(context, funcionalidadeFiltro, TipoExportacao.PDF));
    }
    
    /**
     * Efetua a geração do relatório baseline em formato CLS
     * @param funcionalidadeFiltro
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @POST
	@Path("relatorioBaselineXLS")
	@Consumes(MediaType.APPLICATION_JSON)
    public String relatorioBaselineXLS(FuncionalidadeFiltro funcionalidadeFiltro) throws IllegalArgumentException, IllegalAccessException{
    	Gson gson = new GsonBuilder().create();
		return gson.toJson(funcionalidadeBusiness.relatorioBaseline(context, funcionalidadeFiltro, TipoExportacao.EXCEL));
    }
    
    /**
     * Realiza a exportação da grid de funcionalidade em formato PDF
     * @param funcionalidadeFiltro
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @POST
	@Path("exportacaoPDF")
	@Consumes(MediaType.APPLICATION_JSON)
    public String exportacaoPDF(FuncionalidadeFiltro funcionalidadeFiltro) throws IllegalArgumentException, IllegalAccessException{
    	Gson gson = new GsonBuilder().create();
		return gson.toJson(funcionalidadeBusiness.exportacaoGrid(context, funcionalidadeFiltro, TipoExportacao.PDF));
    }
    
    /**
     * Realiza a exportação da grid de funcionalidade em formato XLS
     * @param funcionalidadeFiltro
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @POST
	@Path("exportacaoXLS")
	@Consumes(MediaType.APPLICATION_JSON)
    public String exportacaoXLS(FuncionalidadeFiltro funcionalidadeFiltro) throws IllegalArgumentException, IllegalAccessException{
    	Gson gson = new GsonBuilder().create();
		return gson.toJson(funcionalidadeBusiness.exportacaoGrid(context, funcionalidadeFiltro, TipoExportacao.EXCEL));
    }
}