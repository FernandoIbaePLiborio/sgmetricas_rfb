package gmetrica.resource;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.demoiselle.jee.crud.AbstractREST;
import org.demoiselle.jee.rest.exception.DemoiselleRestException;
import org.demoiselle.jee.security.annotation.Authenticated;

import gmetrica.business.ImportacaoBusiness;
import gmetrica.model.Contagem;
import gmetrica.model.Importacao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@Api("importacao")
@ApiImplicitParams({
    @ApiImplicitParam(name = "Authorization", value = "JWT token",
            required = true, dataType = "string", paramType = "header")
})
@Authenticated
@Path("importacao")
public class ImportacaoResource extends AbstractREST<Contagem, Long> {
	
	@Inject
	private ImportacaoBusiness importacaoBusiness;

	/**
	 * Recebe o arquivo como bytearray
	 * @param arquivo
	 */
	@POST
	@Path("upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public void upload(byte[] arquivo){
    	
    }
	
	/**
	 * Realiza a importação da planilha
	 * @param importacao
	 */
	@POST
	@Path("uploadBase64")
	@Consumes(MediaType.APPLICATION_JSON)	
    public void upload(Importacao importacao) throws PersistenceException {
		try {
			importacaoBusiness.importacao(importacao);
		} catch (Exception e) {
			throw new DemoiselleRestException(e.getCause().getMessage(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
		} 
	}
}