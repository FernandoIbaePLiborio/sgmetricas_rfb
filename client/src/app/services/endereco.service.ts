import { Injectable } from "@angular/core";
import {Response, Headers} from '@angular/http';

import { RestClientService } from "./restclient.service";
import { Observable } from "rxjs/Observable";
import { EnderecoFiltro } from "../filtro/endereco.filtro";
import { ConsultaResult } from "../util/consultaresult";
import { Endereco } from "../model/endereco";
import { Paginador } from "../model/paginador";
import { ServiceHelper } from "./service-helper";

@Injectable()
export class EnderecoService {

    private static RESOURCE_PATH: string = "/endereco";

    constructor(private restClient: RestClientService) {

    }

    findEnderecos = (filtro: EnderecoFiltro) : Observable<ConsultaResult<Endereco>> => {
        
        let resourcePath = EnderecoService.RESOURCE_PATH;
        let resourceArgs: string[] = [];
        if(filtro.numeroProcesso && filtro.numeroProcesso.trim()) {
            resourceArgs.push("numeroProcesso=" + filtro.numeroProcesso.trim());
        }
        if(filtro.cep && filtro.cep.trim()) {
            resourceArgs.push("cep=" + filtro.cep.trim());
        }
        if(filtro.rua && filtro.rua.trim()) {
            resourceArgs.push("rua=" + filtro.rua.trim());
        }
        if(filtro.complemento && filtro.complemento.trim()) {
            resourceArgs.push("complemento=" + filtro.complemento.trim());
        }
        resourceArgs = resourceArgs.concat(ServiceHelper.createCommonUrlSearchParams(filtro));
        resourcePath += ("?" + resourceArgs.join("&"))
        return this.restClient.getResource(resourcePath).map(response => this.mapToConsultaResult(response, filtro.limit));
    };

    findEnderecoList = () : Observable<Endereco[]> => {
        let resourcePath = EnderecoService.RESOURCE_PATH;
        return this.restClient.getResource(resourcePath).map(response => response.json());
    };

    findEndereco = (idEndereco: number) : Observable<Endereco> => {
        let resourcePath = EnderecoService.RESOURCE_PATH;
        resourcePath += "/" + idEndereco;
        return this.restClient.getResource(resourcePath).map(response => response.json());
    };

    createEndereco = (endereco: Endereco): Observable<Endereco> => {
        let resourcePath = EnderecoService.RESOURCE_PATH;
        return this.restClient.postResource(resourcePath, endereco);
        
    };

    updateEndereco = (endereco: Endereco): Observable<Endereco> => {
        let resourcePath = EnderecoService.RESOURCE_PATH;
        return this.restClient.putResource(resourcePath, endereco);
    };

    deleteEndereco = (endereco: Endereco): Observable<void> => {
        let resourcePath = EnderecoService.RESOURCE_PATH;
        resourcePath += ("/" + endereco.id);
        return this.restClient.deleteResource(resourcePath);
    
    };

    mapToConsultaResult = (response: Response, tamanhoPagina: number): ConsultaResult<Endereco> => {
        let itensPagina: Endereco[];
        let paginador: Paginador = new Paginador(tamanhoPagina);
        if(response) {
            itensPagina = response.json();
            if(response.headers) {
                let contentRangeHeader: string = response.headers.get("Content-Range");
                paginador = new Paginador(tamanhoPagina, contentRangeHeader);
            }
        }
        return new ConsultaResult(itensPagina, paginador)
    };

    getEnderecoList = (): Endereco[] => {
        var lista: Endereco[] = [];
        this.findEnderecoList().subscribe(
            result => {
                return result;
            }
        );
        return null;
    }
}