import { Perfil } from "./perfil";

export class Usuario {

    codigo: number;
    cpf: string;
    nome: string;
    setorExercicio: number;
    unidadeAdministrativa: number;
    unidadeExercicio: number;
    perfis: Perfil[];

    constructor() {}
}