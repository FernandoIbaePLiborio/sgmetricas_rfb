import { SelectItem } from "primeng/components/common/selectitem";

export enum Situacao {
    PENDENTE = <any>"Pendente",
    CONCLUIDO = <any>"Concluído"
}

export namespace Situacao {
    export function getSelectItem(): any {
        var item: SelectItem[] = [];
        item.push({label:'Selecione', value:null});
        item.push({label:Situacao.PENDENTE.toString(), value:Situacao[Situacao.PENDENTE]});
        item.push({label:Situacao.CONCLUIDO.toString(), value:Situacao[Situacao.CONCLUIDO]});
        return item;
    } 
}