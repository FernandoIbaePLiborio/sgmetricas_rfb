import { SelectItem } from "primeng/components/common/selectitem";

export enum Complexidade {
    BAIXA = <any>"Baixa",
    MEDIA = <any>"Média",
    ALTA = <any>"Alta"
}

export namespace Complexidade {
    export function getSelectItem(): any {
        var item: SelectItem[] = [];
        item.push({label:'Selecione', value:null});
        item.push({label:Complexidade.BAIXA.toString(), value:Complexidade[Complexidade.BAIXA]});
        item.push({label:Complexidade.MEDIA.toString(), value:Complexidade[Complexidade.MEDIA]});
        item.push({label:Complexidade.ALTA.toString(), value:Complexidade[Complexidade.ALTA]});
        return item;
    } 
}