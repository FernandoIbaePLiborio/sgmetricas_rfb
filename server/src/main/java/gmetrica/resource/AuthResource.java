package gmetrica.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.inject.Inject;
import javax.security.cert.X509Certificate;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.demoiselle.jee.security.exception.DemoiselleSecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.rfb.corporativo.exception.CorporativoException;
import br.gov.rfb.corporativo.modelo.Usuario;
import gmetrica.security.Credentials;
import gmetrica.security.LoginService;
import io.swagger.annotations.Api;

@Api("Auth")
@Path("auth")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class AuthResource {
	
	/** log. */
    private static final Logger logger = LoggerFactory.getLogger(AuthResource.class);
	
    @Inject
    private LoginService loginService;
    
    @Inject
    private HttpServletRequest request;

    /**
     * Efetua o login no sistema
     * @param credentials
     * @return
     */
    @POST
    @Path("login")
    public Usuario login(Credentials credentials) {
    	Usuario usuario = new Usuario();
		try {
			usuario = loginService.login(credentials);
		} catch (CorporativoException e) {
			e.printStackTrace();
			throw new DemoiselleSecurityException(e.getMessage(), Response.Status.UNAUTHORIZED.getStatusCode());
		}
		
		return usuario;
    }

    @POST
    @Path("token")
    public Response token(Credentials credentials) {
    	try {
    		return Response.ok().entity(loginService.token(credentials).toString()).build();
		} catch (CorporativoException e) {
			e.printStackTrace();
			throw new DemoiselleSecurityException(e.getMessage(), Response.Status.UNAUTHORIZED.getStatusCode());
		}
    }
    
    @POST
    @Path("loginCertificado")
    public Usuario loginCertificado() {
    	Usuario usuario = new Usuario();
		try {
			String cpf = recuperarInformacoesX509Certificate();
			usuario = loginService.login(cpf);
		} catch (CorporativoException e) {
			e.printStackTrace();
			throw new DemoiselleSecurityException(e.getMessage(), Response.Status.UNAUTHORIZED.getStatusCode());
		}
		
		return usuario;
    }
    
    @POST
    @Path("tokenCertificado")
    public Response tokenCertificado() {
    	try {
    		String cpf = recuperarInformacoesX509Certificate();
    		return Response.ok().entity(loginService.token(cpf).toString()).build();
		} catch (CorporativoException e) {
			e.printStackTrace();
			throw new DemoiselleSecurityException(e.getMessage(), Response.Status.UNAUTHORIZED.getStatusCode());
		}
    }
    
    public String recuperarInformacoesX509Certificate() throws CorporativoException {
        final X509Certificate[] certificate = recuperarX509Certificate();
        if ((certificate == null) || (certificate.length == 0)) {
            logger.info("X509Certificate não encontrado");
            throw new DemoiselleSecurityException("X509Certificate não encontrado", Response.Status.UNAUTHORIZED.getStatusCode());
        } else {
            final X509Certificate x509Certificate = certificate[0];
            logger.info("Certificado encontrado:" + x509Certificate);
            logger.info("Certificado encontrado:" + x509Certificate.getSubjectDN());
            logger.info("Certificado encontrado:" + x509Certificate.getSubjectDN().getName());
            
            final String cpf = extrairCpfX509Certificate(x509Certificate);
            
            if (StringUtils.isBlank(cpf)) {
                logger.error("Não foi possível encontrar o cpf do usuário no X509Certificado.");
                throw new DemoiselleSecurityException("Não foi possível encontrar o cpf do usuário no X509Certificado.", Response.Status.UNAUTHORIZED.getStatusCode());
            } 
            
            return cpf;
        }
    }
    
    /**
     * Recuperar x509 certificate.
     * 
     * @return the x509 certificate[]
     * @throws CorporativoException 
     */
    private X509Certificate[] recuperarX509Certificate() throws CorporativoException {
    	final String X509_CERTIFICATE = "javax.servlet.request.X509Certificate";
    	if(request == null){
    		logger.info("O HttpServletRequest é nulo.");
    		throw new DemoiselleSecurityException("O HttpServletRequest é nulo.", Response.Status.UNAUTHORIZED.getStatusCode());
    	}
        return (X509Certificate[]) request.getAttribute(X509_CERTIFICATE);
    }
    
    /**
     * Extrair cpf x509 certificate.
     * 
     * @param x509Certificate
     *            the x509 certificate
     * @return the string
     */
    private String extrairCpfX509Certificate(final X509Certificate x509Certificate) {
        final String name = x509Certificate.getSubjectDN().getName();
        final int indiceDoisPontos = name.indexOf(':') + 1;
        return name.substring(indiceDoisPontos, indiceDoisPontos + Integer.valueOf("11"));
    }
}
