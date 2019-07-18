import { Injectable } from '@angular/core';
import { Observable } from "rxjs/Observable";
import { Response, Headers } from '@angular/http';
import { RestClientService } from './restclient.service';
import { Importacao } from '../model/importacao';

@Injectable()
export class ImportacaoService {

    private static RESOURCE_PATH: string = "/importacao";

    constructor(private restClient: RestClientService) {

    }

    importarPlanilha = (importacao: Importacao): Observable<Importacao> => {
        let resourcePath = ImportacaoService.RESOURCE_PATH + "/uploadBase64";
        return this.restClient.postResource(resourcePath, importacao);
    };
}