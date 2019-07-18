import { SelectItem } from "primeng/components/common/selectitem";

export enum TipoRelatorio {
    TODAS = <any>"Todas as funcionalidades",
    BASELINE = <any>"Baseline"
}

export namespace TipoRelatorio {
    export function getSelectItem(): any {
        var item: SelectItem[] = [];
        item.push({label:'Selecione', value:null});
        item.push({label:TipoRelatorio.TODAS.toString(), value:TipoRelatorio[TipoRelatorio.TODAS]});
        item.push({label:TipoRelatorio.BASELINE.toString(), value:TipoRelatorio[TipoRelatorio.BASELINE]});
        return item;
    } 
}