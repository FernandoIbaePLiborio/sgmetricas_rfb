import { SelectItem } from "primeng/components/common/selectitem";

export enum TipoFuncionalidade {
    ALI = <any>"ALI",
    AIE = <any>"AIE",
    EE = <any>"EE",
    CE = <any>"CE",
    SE = <any>"SE"
}

export namespace TipoFuncionalidade {
    export function getSelectItem(): any {
        var item: SelectItem[] = [];
        item.push({label:'Selecione', value:null});
        item.push({label:TipoFuncionalidade.ALI.toString(), value:TipoFuncionalidade[TipoFuncionalidade.ALI]});
        item.push({label:TipoFuncionalidade.AIE.toString(), value:TipoFuncionalidade[TipoFuncionalidade.AIE]});
        item.push({label:TipoFuncionalidade.EE.toString(), value:TipoFuncionalidade[TipoFuncionalidade.EE]});
        item.push({label:TipoFuncionalidade.CE.toString(), value:TipoFuncionalidade[TipoFuncionalidade.CE]});
        item.push({label:TipoFuncionalidade.SE.toString(), value:TipoFuncionalidade[TipoFuncionalidade.SE]});
        return item;
    }
}