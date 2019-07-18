import { Injectable, EventEmitter } from "@angular/core";
import { Response, Headers } from '@angular/http';
import { RestClientService } from "./restclient.service";
import { Observable } from "rxjs/Observable";
import { ConsultaResult } from "../util/consultaresult";
import { Template } from "../model/template";
import { TemplateFiltro } from "../filtro/template.filtro";
import { ServiceHelper } from "./service-helper";
import { Paginador } from "../model/paginador";
import { SituacaoTemplate } from "../enumeration/situacao-template";
import { SelectItem } from "primeng/components/common/selectitem";

@Injectable()
export class TemplateService {

    private static RESOURCE_PATH: string = "/template";

    constructor(private restClient: RestClientService) { }

    obterPaginado = (filtro: TemplateFiltro) : Observable<ConsultaResult<Template>> => {
        let resourcePath = TemplateService.RESOURCE_PATH;
        let resourceArgs: string[] = [];
        if(filtro.nome && filtro.nome.trim()) {
            resourceArgs.push("nome=" + filtro.nome.trim());
        }
        if(filtro.situacao) {
            resourceArgs.push("situacao=" + filtro.situacao);
        }
        resourceArgs = resourceArgs.concat(ServiceHelper.createCommonUrlSearchParams(filtro));
        resourcePath += ("?" + resourceArgs.join("&"))
        return this.restClient.getResource(resourcePath).map(response => this.mapToConsultaResult(response, filtro.limit));
    };
    
    obterPorFiltro = (filtro: TemplateFiltro) : Observable<Template[]> => {
        let resourcePath = TemplateService.RESOURCE_PATH;
        let resourceArgs: string[] = [];
        if(filtro.nome && filtro.nome.trim()) {
            resourceArgs.push("nome=" + filtro.nome.trim());
        }
        if(filtro.situacao) {
            resourceArgs.push("situacao=" + filtro.situacao);
        }
        resourcePath += ("?" + resourceArgs.join("&"));
        
        return this.restClient.getResource(resourcePath).map(response => response.json());
    };

    obter = (idTemplate: number) : Observable<Template> => {
        let resourcePath = TemplateService.RESOURCE_PATH;
        resourcePath += "/" + idTemplate;
        return this.restClient.getResource(resourcePath).map(response => response.json());
    };

    incluir = (template: Template): Observable<Template> => {
        let resourcePath = TemplateService.RESOURCE_PATH;
        return this.restClient.postResource(resourcePath, template);
        
    };

    alterar = (template: Template): Observable<Template> => {
        let resourcePath = TemplateService.RESOURCE_PATH;
        return this.restClient.putResource(resourcePath, template);
    };

    excluir = (template: Template): Observable<void> => {
        let resourcePath = TemplateService.RESOURCE_PATH;
        resourcePath += ("/" + template.id);
        return this.restClient.deleteResource(resourcePath);
    
    };

    mapToConsultaResult = (response: Response, tamanhoPagina: number): ConsultaResult<Template> => {
        let itensPagina: Template[];
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

    getTemplateSelectItem = (nomeFiltro: string, situacaoFiltro: SituacaoTemplate) : SelectItem[] => {
        var selectItem: SelectItem[] = [];
        selectItem.push({label:'Selecione', value:null});

        var filtro: TemplateFiltro = new TemplateFiltro();
        filtro.nome = nomeFiltro;
        if(situacaoFiltro){
            filtro.situacao = SituacaoTemplate[situacaoFiltro];
        }

        this.obterPorFiltro(filtro).subscribe(
            result => {
                for (let template of result) {
                    selectItem.push({label:template.nome, value:template.id});
                }
            }
        );;
        
        return selectItem;
    }
}