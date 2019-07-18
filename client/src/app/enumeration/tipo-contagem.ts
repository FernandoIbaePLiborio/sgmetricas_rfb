import { SelectItem } from "primeng/components/common/selectitem";

export enum TipoContagem {
    CONTAGEM_REFERENCIA = <any>"Contagem de Referência",
    ESTIMATIVA_INICIAL = <any>"Estimativa Inicial",
    CONTAGEM_ENCERRAMENTO = <any>"Contagem de Encerramento"
}

export namespace TipoContagem {
    export function getSelectItem(): any {
        var item: SelectItem[] = [];
        item.push({label:'Selecione', value:null});
        item.push({label:TipoContagem.CONTAGEM_REFERENCIA.toString(), value:TipoContagem[TipoContagem.CONTAGEM_REFERENCIA]});
        item.push({label:TipoContagem.ESTIMATIVA_INICIAL.toString(), value:TipoContagem[TipoContagem.ESTIMATIVA_INICIAL]});
        item.push({label:TipoContagem.CONTAGEM_ENCERRAMENTO.toString(), value:TipoContagem[TipoContagem.CONTAGEM_ENCERRAMENTO]});
        return item;
    } 
}