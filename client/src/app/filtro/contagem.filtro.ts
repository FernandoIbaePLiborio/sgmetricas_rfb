import { ConsultaFiltro } from "./consulta.filtro";
import { TipoContagem } from "../enumeration/tipo-contagem";
import { TipoProjeto } from "../enumeration/tipo-projeto";
import { Fornecedor } from "../enumeration/fornecedor";
import { Situacao } from "../enumeration/situacao";

export class ContagemFiltro extends ConsultaFiltro {
    projeto: string; 
    linguagem: string;
    tipoContagem: TipoContagem;
    tipoProjeto: TipoProjeto;
    propositoEscopo: string;
    fornecedor: Fornecedor;
    fronteira: string;
    dataImportacaoDe: Date;
    dataImportacaoAte: Date;
    pfBrutoDe: number;
    pfBrutoAte: number;
    pfDemandaDe: number;
    pfDemandaAte: number;
    responsavel: string;
    numeroDemanda: string;
    situacao: Situacao;
}