package gmetrica.security;

import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.demoiselle.jee.core.api.security.DemoiselleUser;
import org.demoiselle.jee.core.api.security.SecurityContext;
import org.demoiselle.jee.core.api.security.Token;
import org.demoiselle.jee.security.exception.DemoiselleSecurityException;

import br.gov.rfb.corporativo.exception.CorporativoException;
import br.gov.rfb.corporativo.modelo.Perfil;
import br.gov.rfb.corporativo.modelo.Usuario;
import br.gov.rfb.corporativo.service.AutorizacaoService;
import br.gov.rfb.corporativo.service.PessoaService;
import gmetrica.util.Propriedades;

@Stateless
public class LoginService {

	@Inject
    private Token token;
	
	@Inject
    private DemoiselleUser loggedUser;
	
	@Inject
    private SecurityContext securityContext;

	private static final Long ID_SISTEMA_GMETRICAS = Long.parseLong(Propriedades.getPropriedade("id_sistema"));

    @EJB(lookup = "java:global/rfb-corporativoEAR/rfb-corporativo-ejb-2.0.1/AutorizacaoServiceImpl!br.gov.rfb.corporativo.service.AutorizacaoService")
    public AutorizacaoService autorizacaoService;

    @EJB(lookup = "java:global/rfb-corporativoEAR/rfb-corporativo-ejb-2.0.1/PessoaServiceImpl!br.gov.rfb.corporativo.service.PessoaService")
    public PessoaService pessoaService;
    
	public Usuario login(Credentials credentials) throws CorporativoException {
		Usuario usu = autorizacaoService.autorizar(credentials.getLogin(), ID_SISTEMA_GMETRICAS);
		if (usu == null) {
			throw new DemoiselleSecurityException("Credenciais inválidas", Response.Status.UNAUTHORIZED.getStatusCode());
		}
        
        for(Perfil perfil : usu.getPerfis()){
			perfil.getSistema().setPerfis(null);
			perfil.setUsuarios(null);
		}
        
		return usu;
	}

	public Token token(Credentials credentials) throws CorporativoException {
		Usuario usu = autorizacaoService.autorizar(credentials.getLogin(), ID_SISTEMA_GMETRICAS);
		if (usu == null) {
			throw new DemoiselleSecurityException("Credenciais inválidas", Response.Status.UNAUTHORIZED.getStatusCode());
		}
		loggedUser.setName(usu.getNome());
        loggedUser.setIdentity(usu.getCodigo() + "");
        if(usu.getPerfis() != null && !usu.getPerfis().isEmpty()){
        	loggedUser.addRole(usu.getPerfis().stream().map(w->w.getDescricao()).collect(Collectors.joining(", ")));
        }
        
        loggedUser.addParam("CPF", usu.getCpf());
        securityContext.setUser(loggedUser);
        return token;
    }
	
	public Usuario login(String cpf) throws CorporativoException {
		Usuario usu = autorizacaoService.autorizar(cpf, ID_SISTEMA_GMETRICAS);
		if (usu == null) {
			throw new DemoiselleSecurityException("Credenciais inválidas", Response.Status.UNAUTHORIZED.getStatusCode());
		}
        
        for(Perfil perfil : usu.getPerfis()){
			perfil.getSistema().setPerfis(null);
			perfil.setUsuarios(null);
		}
        
		return usu;
	}
	
	public Token token(String cpf) throws CorporativoException {
		Usuario usu = autorizacaoService.autorizar(cpf, ID_SISTEMA_GMETRICAS);
		if (usu == null) {
			throw new DemoiselleSecurityException("Credenciais inválidas", Response.Status.UNAUTHORIZED.getStatusCode());
		}
		loggedUser.setName(usu.getNome());
        loggedUser.setIdentity(usu.getCodigo() + "");
        if(usu.getPerfis() != null && !usu.getPerfis().isEmpty()){
        	loggedUser.addRole(usu.getPerfis().stream().map(w->w.getDescricao()).collect(Collectors.joining(", ")));
        }
        
        loggedUser.addParam("CPF", usu.getCpf());
        securityContext.setUser(loggedUser);
        return token;
    }
}
