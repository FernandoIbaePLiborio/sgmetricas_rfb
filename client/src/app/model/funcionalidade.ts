import { Contagem } from "./contagem";
import { TipoFuncionalidade } from "../enumeration/tipo-funcionalidade";
import { TipoDemanda } from "../enumeration/tipo-demanda";
import { Classificacao } from "../enumeration/classificacao";
import { Complexidade } from "../enumeration/complexidade";

export class Funcionalidade {
    
    id: number;
    contagem: Contagem;
    nome: string;
    tipo: TipoFuncionalidade;
    tipoDemanda: TipoDemanda;
    rlAr: string;
    memoriaRlAr: string;
    td: string;
    numeroDemanda: string;
    memoriaTd: string;
    classificacao: Classificacao;
    complexidade: Complexidade;
    pontoFuncao: number;
    rastreabilidadeJustificativa: string;
    divergencias: string;
    dataImportacao: Date;

    constructor() {

    }
}