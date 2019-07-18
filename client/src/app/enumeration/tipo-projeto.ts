import { SelectItem } from "primeng/components/common/selectitem";

export enum TipoProjeto {
    APURACAO_ESPECIAL = <any>"Apuração Especial",
    DESENVOLVIMENTO = <any>"Desenvolvimento",
    MANUTENCAO_ADAPTATIVA = <any>"Manutenção Adaptativa",
    MANUTENCAO_CORRETIVA = <any>"Manutenção Corretiva",
    MANUTENCAO_EVOLUTIVA = <any>"Manutenção Evolutiva"
}

export namespace TipoProjeto {
    export function getSelectItem(): any {
        var item: SelectItem[] = [];
        item.push({label:'Selecione', value:null});
        item.push({label:TipoProjeto.APURACAO_ESPECIAL.toString(), value:TipoProjeto[TipoProjeto.APURACAO_ESPECIAL]});
        item.push({label:TipoProjeto.DESENVOLVIMENTO.toString(), value:TipoProjeto[TipoProjeto.DESENVOLVIMENTO]});
        item.push({label:TipoProjeto.MANUTENCAO_ADAPTATIVA.toString(), value:TipoProjeto[TipoProjeto.MANUTENCAO_ADAPTATIVA]});
        item.push({label:TipoProjeto.MANUTENCAO_CORRETIVA.toString(), value:TipoProjeto[TipoProjeto.MANUTENCAO_CORRETIVA]});
        item.push({label:TipoProjeto.MANUTENCAO_EVOLUTIVA.toString(), value:TipoProjeto[TipoProjeto.MANUTENCAO_EVOLUTIVA]});
        return item;
    } 
}

