import { Template } from "./template";
import { TipoFuncionalidade } from "../enumeration/tipo-funcionalidade";

export class TemplateFuncionalidade {
    
    id: number;
    template: Template;
    tipoFuncionalidade: TipoFuncionalidade;
    nomeFuncionalidadeAba: string;
    nomeFuncionalidadeReferencia: string;
    tipoAba: string;
    tipoReferencia: string;
    tipoDemandaAba: string;
    tipoDemandaReferencia: string;
    rlArAba: string;
    rlArReferencia: string;
    memoriaRlArAba: string;
    memoriaRlArReferencia: string;
    rastreabilidadeAba: string;
    rastreabilidadeReferencia: string;
    tdAba: string;
    tdReferencia: string;
    memoriaTdAba: string;
    memoriaTdReferencia: string;
    classificacaoAba: string;
    classificacaoReferencia: string;
    complexidadeAba: string;
    complexidadeReferencia: string;
    pontoFuncaoAba: string;
    pontoFuncaoReferencia: string;
    divergenciasAba: string;
    divergenciasReferencia: string;

    constructor() {

    }
}