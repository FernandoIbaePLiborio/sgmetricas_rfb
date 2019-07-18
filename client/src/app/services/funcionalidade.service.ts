import { Injectable } from "@angular/core";
import { Response, Headers } from '@angular/http';
import { RestClientService } from "./restclient.service";
import { Observable } from "rxjs/Observable";
import { ConsultaResult } from "../util/consultaresult";
import { ServiceHelper } from "./service-helper";
import { Paginador } from "../model/paginador";
import { SelectItem } from "primeng/components/common/selectitem";
import { Situacao } from "../enumeration/situacao";
import { FuncionalidadeFiltro } from "../filtro/funcionalidade.filtro";
import { Funcionalidade } from "../model/funcionalidade";

@Injectable()
export class FuncionalidadeService {

    private static RESOURCE_PATH: string = "/funcionalidade";

    constructor(private restClient: RestClientService) { }

    obterPaginado = (filtro: FuncionalidadeFiltro): Observable<ConsultaResult<Funcionalidade>> => {
        let resourcePath = FuncionalidadeService.RESOURCE_PATH;
        let resourceArgs: string[] = [];
        if (filtro.contagem) {
            resourceArgs.push("contagem=" + filtro.contagem);
        }
        else {
            resourceArgs.push("contagem=" + 0);
        }
        if (filtro.nome && filtro.nome.trim()) {
            resourceArgs.push("nome=" + filtro.nome.trim());
        }
        if (filtro.classificacao) {
            resourceArgs.push("classificacao=" + filtro.classificacao);
        }
        if (filtro.complexidade) {
            resourceArgs.push("complexidade=" + filtro.complexidade);
        }
        if (filtro.tipo) {
            resourceArgs.push("tipo=" + filtro.tipo);
        }
        if (filtro.td) {
            resourceArgs.push("td=" + filtro.td);
        }
        if (filtro.lrAr) {
            resourceArgs.push("rlAr=" + filtro.lrAr);
        }
        if (filtro.pontoFuncao) {
            resourceArgs.push("pontoFuncao=" + filtro.pontoFuncao);
        }
        if (filtro.tipoRelatorio) {
            resourceArgs.push("tipoRelatorio=" + filtro.tipoRelatorio);
        }
        if (filtro.projeto && filtro.projeto.trim()) {
            resourceArgs.push("projeto=" + filtro.projeto.trim());
        }
        if (filtro.fronteira && filtro.fronteira.trim()) {
            resourceArgs.push("fronteira=" + filtro.fronteira.trim());
        }
        if (filtro.dataImportacaoDe) {
            resourceArgs.push("dataImportacaoDe=" + filtro.dataImportacaoDe);
        }
        if (filtro.dataImportacaoAte) {
            resourceArgs.push("dataImportacaoAte=" + filtro.dataImportacaoAte);
        }
        if (filtro.dataImportacao) {
            resourceArgs.push("dataImportacao=" + filtro.dataImportacao);
        }
        if (filtro.numeroDemanda && filtro.numeroDemanda.trim()) {
            resourceArgs.push("numeroDemanda=" + filtro.numeroDemanda.trim());
        }
        if (filtro.situacaoContagem) {
            resourceArgs.push("situacaoContagem=" + filtro.situacaoContagem);
        }
        resourceArgs = resourceArgs.concat(ServiceHelper.createCommonUrlSearchParams(filtro));
        resourcePath += ("?" + resourceArgs.join("&"));
        return this.restClient.getResource(resourcePath).map(response => this.mapToConsultaResult(response, filtro.limit));
    };

    obterPorFiltro = (filtro: FuncionalidadeFiltro): Observable<Funcionalidade[]> => {
        let resourcePath = FuncionalidadeService.RESOURCE_PATH;
        let resourceArgs: string[] = [];
        if (filtro.nome && filtro.nome.trim()) {
            resourceArgs.push("nome=" + filtro.nome.trim());
        }
        resourcePath += ("?" + resourceArgs.join("&"))
        return this.restClient.getResource(resourcePath).map(response => response.json());
    };

    obter = (id: number): Observable<Funcionalidade> => {
        let resourcePath = FuncionalidadeService.RESOURCE_PATH;
        resourcePath += "/" + id;
        return this.restClient.getResource(resourcePath).map(response => response.json());
    };

    incluir = (funcionalidade: Funcionalidade): Observable<Funcionalidade> => {
        let resourcePath = FuncionalidadeService.RESOURCE_PATH;
        return this.restClient.postResource(resourcePath, funcionalidade);

    };

    alterar = (funcionalidade: Funcionalidade): Observable<Funcionalidade> => {
        let resourcePath = FuncionalidadeService.RESOURCE_PATH;
        return this.restClient.putResource(resourcePath, funcionalidade);
    };

    excluir = (funcionalidade: Funcionalidade): Observable<void> => {
        let resourcePath = FuncionalidadeService.RESOURCE_PATH;
        resourcePath += ("/" + funcionalidade.id);
        return this.restClient.deleteResource(resourcePath);

    };

    relatorioBaselinePDF = (filtro: FuncionalidadeFiltro): Observable<string> => {
        let resourcePath = FuncionalidadeService.RESOURCE_PATH + "/relatorioBaselinePDF";
        filtro.contagem = 0;
        return this.restClient.postResource2(resourcePath, filtro);
    };

    relatorioBaselineXLS = (filtro: FuncionalidadeFiltro): Observable<string> => {
        let resourcePath = FuncionalidadeService.RESOURCE_PATH + "/relatorioBaselineXLS";
        filtro.contagem = 0;
        return this.restClient.postResource2(resourcePath, filtro);
    };

    exportacaoPDF = (filtro: FuncionalidadeFiltro): Observable<string> => {
        let resourcePath = FuncionalidadeService.RESOURCE_PATH + "/exportacaoPDF";
        return this.restClient.postResource2(resourcePath, filtro);
    };

    exportacaoXLS = (filtro: FuncionalidadeFiltro): Observable<string> => {
        let resourcePath = FuncionalidadeService.RESOURCE_PATH + "/exportacaoXLS";
        return this.restClient.postResource2(resourcePath, filtro);
    };

    mapToConsultaResult = (response: Response, tamanhoPagina: number): ConsultaResult<Funcionalidade> => {
        let itensPagina: Funcionalidade[];
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