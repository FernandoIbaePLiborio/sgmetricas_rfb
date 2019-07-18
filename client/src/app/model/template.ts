import { SituacaoTemplate } from "../enumeration/situacao-template";
import { TemplateContagem } from "./template-contagem";
import { TemplateFuncionalidade } from "./template-funcionalidade";

export class Template {
    
    id: number;
    nome: string;
    situacao: SituacaoTemplate;
    templateContagem: TemplateContagem;
    templateFuncionalidadeList: TemplateFuncionalidade[];

    constructor() {

    }
}