import { Injectable } from "@angular/core";
import { Observable } from "rxjs/Observable";
import { Usuario } from "../model/usuario";
import { RestClientService } from "./restclient.service";
import { Credentials } from "../model/credentials";
import { Token } from "../model/token";

@Injectable()
export class AuthService {

    private static RESOURCE_PATH: string = "/auth";

    constructor(private restClient: RestClientService) {

    }

    login = (credentials: Credentials): Observable<Usuario> => {
        let resourcePath = AuthService.RESOURCE_PATH + "/login";
        return this.restClient.postResource2NoAuthorization(resourcePath, credentials);
    };

    token = (credentials: Credentials): Observable<Token> => {
        let resourcePath = AuthService.RESOURCE_PATH + "/token";
        return this.restClient.postResource2NoAuthorization(resourcePath, credentials);
    };

    loginCertificado = (credentials: Credentials): Observable<Usuario> => {
        let resourcePath = AuthService.RESOURCE_PATH + "/loginCertificado";
        return this.restClient.postResource2NoAuthorization(resourcePath, credentials);
    };

    tokenCertificado = (credentials: Credentials): Observable<Token> => {
        let resourcePath = AuthService.RESOURCE_PATH + "/tokenCertificado";
        return this.restClient.postResource2NoAuthorization(resourcePath, credentials);
    };
}