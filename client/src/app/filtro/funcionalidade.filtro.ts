import { TipoDemanda } from './../enumeration/tipo-demanda';
import { ConsultaFiltro } from "./consulta.filtro";
import { TipoFuncionalidade } from "../enumeration/tipo-funcionalidade";
import { Classificacao } from "../enumeration/classificacao";
import { Complexidade } from "../enumeration/complexidade";
import { Contagem } from "../model/contagem";
import { TipoRelatorio } from "../enumeration/tipo-relatorio";
import { Situacao } from "../enumeration/situacao";

export class FuncionalidadeFiltro extends ConsultaFiltro {
    contagem: number;
    tipo: TipoFuncionalidade; 
    nome: string;
    classificacao: Classificacao;
    complexidade: Complexidade;
    pontoFuncao: number;
    td: number;
    lrAr: number;
    numeroDemanda: string;
    tipoRelatorio: TipoRelatorio;
    dataImportacaoDe: Date;
    dataImportacaoAte: Date;
    dataImportacao: Date;
    fronteira: string;
    projeto: string;
    situacaoContagem: Situacao;
}