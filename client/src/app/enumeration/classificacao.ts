import { SelectItem } from "primeng/components/common/selectitem";

export enum Classificacao {
    INCLUIDA = <any>"Incluída",
    ALTERADA = <any>"Alterada",
    EXCLUIDA = <any>"Excluída"
}

export namespace Classificacao {
    export function getSelectItem(): any {
        var item: SelectItem[] = [];
        item.push({label:'Selecione', value:null});
        item.push({label:Classificacao.INCLUIDA.toString(), value:Classificacao[Classificacao.INCLUIDA]});
        item.push({label:Classificacao.ALTERADA.toString(), value:Classificacao[Classificacao.ALTERADA]});
        item.push({label:Classificacao.EXCLUIDA.toString(), value:Classificacao[Classificacao.EXCLUIDA]});
        return item;
    } 
}