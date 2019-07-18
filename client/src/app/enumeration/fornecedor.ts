import { SelectItem } from "primeng/components/common/selectitem";

export enum Fornecedor {
    DATAPREV = <any>"Dataprev",
    SERPRO = <any>"Serpro",
    FABRICA_SOFTWARE = <any>"Fábrica de Software",
    DESENVOLVIMENTO_INTERNO = <any>"Desenvolvimento Interno"
}

export namespace Fornecedor {
    export function getSelectItem(): any {
        var item: SelectItem[] = [];
        item.push({label:'Selecione', value:null});
        item.push({label:Fornecedor.DATAPREV.toString(), value:Fornecedor[Fornecedor.DATAPREV]});
        item.push({label:Fornecedor.SERPRO.toString(), value:Fornecedor[Fornecedor.SERPRO]});
        item.push({label:Fornecedor.FABRICA_SOFTWARE.toString(), value:Fornecedor[Fornecedor.FABRICA_SOFTWARE]});
        item.push({label:Fornecedor.DESENVOLVIMENTO_INTERNO.toString(), value:Fornecedor[Fornecedor.DESENVOLVIMENTO_INTERNO]});
        return item;
    } 
}