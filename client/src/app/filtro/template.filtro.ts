import { ConsultaFiltro } from "./consulta.filtro";
import { SituacaoTemplate } from "../enumeration/situacao-template";

export class TemplateFiltro extends ConsultaFiltro {
    nome: string; 
    situacao: string;
}