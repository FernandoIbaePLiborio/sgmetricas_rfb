package gmetrica.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.demoiselle.jee.core.api.crud.Result;
import org.demoiselle.jee.crud.AbstractREST;
import org.demoiselle.jee.crud.Search;
import org.demoiselle.jee.security.annotation.Authenticated;

import gmetrica.model.Endereco;
import gmetrica.util.Constantes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@Api("endereco")
@ApiImplicitParams({
    @ApiImplicitParam(name = "Authorization", value = "JWT token",
            required = true, dataType = "string", paramType = "header")
})
@Authenticated
@Path("endereco")
public class EnderecoResource extends AbstractREST<Endereco, Long> {

	/**
	 * Retorna a lista de endereços cadastrados
	 */
    @GET
    @Search(fields = {"id", "numeroProcesso", "rua", "cep", "complemento"}, withPagination=true, quantityPerPage=Constantes.PAGINACAO)
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
	@Override
    public Result find() {
        return bc.find();
    }
    
    
}