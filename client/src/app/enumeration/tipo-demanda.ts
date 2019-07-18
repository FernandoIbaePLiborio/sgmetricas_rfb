import { SelectItem } from "primeng/components/common/selectitem";

export enum TipoDemanda {
    DESENVOLVIMENTO = <any>"Desenvolvimento",
    MELHORIA = <any>"Melhoria",
    RETRABALHO_ALTERACAO_REQUISITOS = <any>"Retrabalho Alteração de Requisitos",
    DW_FILTRO_SEGURANCA = <any>"DW - Filtro de Segurança",
    DW_FILTRO_RELATORIO = <any>"DW - Filtro de Relatório",
    DW_DIMENSAO_ESTATICA = <any>"DW - Dimensão Estática",
    DW_METADADOS = <any>"DW - Metadados",
    DW_METADADOS_CODE_DATA = <any>"DW - Metadados Code Data",
    DW_REORGANIZACAO_BANCADA = <any>"DW - Reorganização Bancada"
}

export namespace TipoDemanda {
    export function getSelectItem(): any {
        var item: SelectItem[] = [];
        item.push({label:'Selecione', value:null});
        item.push({label:TipoDemanda.DESENVOLVIMENTO.toString(), value:TipoDemanda[TipoDemanda.DESENVOLVIMENTO]});
        item.push({label:TipoDemanda.MELHORIA.toString(), value:TipoDemanda[TipoDemanda.MELHORIA]});
        item.push({label:TipoDemanda.RETRABALHO_ALTERACAO_REQUISITOS.toString(), value:TipoDemanda[TipoDemanda.RETRABALHO_ALTERACAO_REQUISITOS]});
        item.push({label:TipoDemanda.DW_FILTRO_SEGURANCA.toString(), value:TipoDemanda[TipoDemanda.DW_FILTRO_SEGURANCA]});
        item.push({label:TipoDemanda.DW_FILTRO_RELATORIO.toString(), value:TipoDemanda[TipoDemanda.DW_FILTRO_RELATORIO]});
        item.push({label:TipoDemanda.DW_DIMENSAO_ESTATICA.toString(), value:TipoDemanda[TipoDemanda.DW_DIMENSAO_ESTATICA]});
        item.push({label:TipoDemanda.DW_METADADOS.toString(), value:TipoDemanda[TipoDemanda.DW_METADADOS]});
        item.push({label:TipoDemanda.DW_METADADOS_CODE_DATA.toString(), value:TipoDemanda[TipoDemanda.DW_METADADOS_CODE_DATA]});
        item.push({label:TipoDemanda.DW_REORGANIZACAO_BANCADA.toString(), value:TipoDemanda[TipoDemanda.DW_REORGANIZACAO_BANCADA]});
        return item;
    } 
}