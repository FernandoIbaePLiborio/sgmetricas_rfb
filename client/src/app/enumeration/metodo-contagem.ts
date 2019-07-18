import { SelectItem } from "primeng/components/common/selectitem";

export enum MetodoContagem {
    CONTAGEM_DETALHADA = <any>"Contagem Detalhada",
    CONTAGEM_ESTIMADA_NESMA = <any>"Contagem Estimada NESMA",
    CONTAGEM_ESTIMATIVA = <any>"Contagem Estimativa"
}

export namespace MetodoContagem {
    export function getSelectItem(): any {
        var item: SelectItem[] = [];
        item.push({label:'Selecione', value:null});
        item.push({label:MetodoContagem.CONTAGEM_DETALHADA.toString(), value:MetodoContagem[MetodoContagem.CONTAGEM_DETALHADA]});
        item.push({label:MetodoContagem.CONTAGEM_ESTIMADA_NESMA.toString(), value:MetodoContagem[MetodoContagem.CONTAGEM_ESTIMADA_NESMA]});
        item.push({label:MetodoContagem.CONTAGEM_ESTIMATIVA.toString(), value:MetodoContagem[MetodoContagem.CONTAGEM_ESTIMATIVA]});
        return item;
    } 
}