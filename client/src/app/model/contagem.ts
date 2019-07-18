import { TipoProjeto } from "../enumeration/tipo-projeto";
import { TipoContagem } from "../enumeration/tipo-contagem";
import { Fornecedor } from "../enumeration/fornecedor";
import { Situacao } from "../enumeration/situacao";
import { Funcionalidade } from "./funcionalidade";
import { ResumoContagem } from "./resumo-contagem";
import { MetodoContagem } from "../enumeration/metodo-contagem";

export class Contagem {
    
    id: number;
    projeto: string;
    linguagem: string;
    tipoContagem: TipoContagem;
    tipoProjeto: TipoProjeto;
    plataforma: string;
    liderProjeto: string;
    fronteira: string;
    dataImportacao: Date;
    fornecedor: Fornecedor;
    numeroDemanda: string;
    situacao: Situacao;
    propositoEscopo: string;
    artefatosUsadosContagem: string;
    observacoes: string;
    responsavel: string;
    totalPfDemanda: number;
    pfFuncaoDados: number;
    pfFuncaoTransacional: number;
    pfRetrabalho: number;
    totalPfBruto: number;
    metodoContagem: MetodoContagem;
    funcionalidadeList: Array<Funcionalidade>;
    resumoContagem: ResumoContagem = new ResumoContagem();

    constructor() {

    }
}