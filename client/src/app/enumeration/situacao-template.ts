import { SelectItem } from "primeng/components/common/selectitem";

export enum SituacaoTemplate {
    ATIVO = <any>"Ativo",
    INATIVO = <any>"Inativo"
}

export namespace SituacaoTemplate {
    export function getSelectItem(): any {
        var item: SelectItem[] = [];
        item.push({label:'Selecione', value:null});
        item.push({label:SituacaoTemplate.ATIVO.toString(), value:SituacaoTemplate[SituacaoTemplate.ATIVO]});
        item.push({label:SituacaoTemplate.INATIVO.toString(), value:SituacaoTemplate[SituacaoTemplate.INATIVO]});
        return item;
    }

    export function getDescricao(situacaoTemplate: SituacaoTemplate): string {
        return situacaoTemplate.toString();
    } 
}