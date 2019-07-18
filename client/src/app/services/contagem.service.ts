import { Injectable } from "@angular/core";
import { Response, Headers } from '@angular/http';
import { RestClientService } from "./restclient.service";
import { Observable } from "rxjs/Observable";
import { ConsultaResult } from "../util/consultaresult";
import { ServiceHelper } from "./service-helper";
import { Paginador } from "../model/paginador";
import { SelectItem } from "primeng/components/common/selectitem";
import { ContagemFiltro } from "../filtro/contagem.filtro";
import { Contagem } from "../model/contagem";
import { Situacao } from "../enumeration/situacao";
import { TreatDate } from "../util/treatDate";
import { TreatNumber } from "../util/treatNumber";
import { HttpParams, HttpClient } from "@angular/common/http";

@Injectable()
export class ContagemService {

    private static RESOURCE_PATH: string = "/contagem";

    constructor(private restClient: RestClientService) { }

    obterPaginado = (filtro: ContagemFiltro): Observable<ConsultaResult<Contagem>> => {
        let resourcePath = ContagemService.RESOURCE_PATH;
        let resourceArgs: string[] = [];
        if (filtro.projeto && filtro.projeto.trim()) {
            resourceArgs.push("projeto=" + filtro.projeto.trim());
        }
        if (filtro.linguagem && filtro.linguagem.trim()) {
            resourceArgs.push("linguagem=" + filtro.linguagem.trim());
        }
        if (filtro.tipoContagem) {
            resourceArgs.push("tipoContagem=" + filtro.tipoContagem);
        }
        if (filtro.tipoProjeto) {
            resourceArgs.push("tipoProjeto=" + filtro.tipoProjeto);
        }
        if (filtro.propositoEscopo && filtro.propositoEscopo.trim()) {
            resourceArgs.push("propositoEscopo=" + filtro.propositoEscopo.trim());
        }
        if (filtro.fornecedor) {
            resourceArgs.push("fornecedor=" + filtro.fornecedor);
        }
        if (filtro.dataImportacaoDe) {
            resourceArgs.push("dataImportacaoDe=" + TreatDate.getDateAsString(filtro.dataImportacaoDe));
        }
        if (filtro.dataImportacaoAte) {
            resourceArgs.push("dataImportacaoAte=" + TreatDate.getDateAsString(filtro.dataImportacaoAte));
        }
        if (filtro.pfBrutoDe) {
            resourceArgs.push("totalPfBrutoDe=" + filtro.pfBrutoDe);
        }
        if (filtro.pfBrutoAte) {
            resourceArgs.push("totalPfBrutoAte=" + filtro.pfBrutoAte);
        }
        if (filtro.pfDemandaDe) {
            resourceArgs.push("totalPfDemandaDe=" + filtro.pfDemandaDe);
        }
        if (filtro.pfDemandaAte) {
            resourceArgs.push("totalPfDemandaAte=" + filtro.pfDemandaAte);
        }
        if (filtro.fronteira && filtro.fronteira.trim()) {
            resourceArgs.push("fronteira=" + filtro.fronteira.trim());
        }
        if (filtro.responsavel && filtro.responsavel.trim()) {
            resourceArgs.push("responsavel=" + filtro.responsavel.trim());
        }
        if (filtro.numeroDemanda && filtro.numeroDemanda.trim()) {
            resourceArgs.push("numeroDemanda=" + filtro.numeroDemanda.trim());
        }
        if (filtro.situacao) {
            resourceArgs.push("situacao=" + filtro.situacao);
        }
        resourceArgs = resourceArgs.concat(ServiceHelper.createCommonUrlSearchParams(filtro));
        resourcePath += ("?" + resourceArgs.join("&"));
        return this.restClient.getResource(resourcePath).map(response => this.mapToConsultaResult(response, filtro.limit));
    };

    obter = (id: number): Observable<Contagem> => {
        let resourcePath = ContagemService.RESOURCE_PATH;
        resourcePath += "/" + id;
        return this.restClient.getResource(resourcePath).map(response => response.json());
    };

    incluir = (contagem: Contagem): Observable<Contagem> => {
        let resourcePath = ContagemService.RESOURCE_PATH;
        return this.restClient.postResource(resourcePath, contagem);

    };

    alterar = (contagem: Contagem): Observable<Contagem> => {
        let resourcePath = ContagemService.RESOURCE_PATH;
        return this.restClient.putResource(resourcePath, contagem);
    };

    excluir = (contagem: Contagem): Observable<void> => {
        let resourcePath = ContagemService.RESOURCE_PATH;
        resourcePath += ("/" + contagem.id);
        return this.restClient.deleteResource(resourcePath);

    };

    exportacaoPDF = (filtro: ContagemFiltro): Observable<string> => {
        let resourcePath = ContagemService.RESOURCE_PATH + "/exportacaoPDF";
        return this.restClient.postResource2(resourcePath, filtro);
    };

    exportacaoXLS = (filtro: ContagemFiltro): Observable<string> => {
        let resourcePath = ContagemService.RESOURCE_PATH + "/exportacaoXLS";
        return this.restClient.postResource2(resourcePath, filtro);
    };

    fronteira = (fronteira: string): Observable<string[]> => {
        let resourcePath = ContagemService.RESOURCE_PATH + "/fronteira";
        return this.restClient.postResource2(resourcePath, fronteira);
    };

    projeto = (projeto: string): Observable<string[]> => {
        let resourcePath = ContagemService.RESOURCE_PATH + "/projeto";
        return this.restClient.postResource2(resourcePath, projeto);
    };

    mapToConsultaResult = (response: Response, tamanhoPagina: number): ConsultaResult<Contagem> => {
        let itensPagina: Contagem[];
        let paginador: Paginador = new Paginador(tamanhoPagina);
        if (response) {
            itensPagina = response.json();
            if (response.headers) {
                let contentRangeHeader: string = response.headers.get("Content-Range");
                paginador = new Paginador(tamanhoPagina, contentRangeHeader);
            }
        }
        return new ConsultaResult(itensPagina, paginador)
    };
}