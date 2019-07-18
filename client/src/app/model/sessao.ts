import { Usuario } from "./usuario";
import { Token } from "./token";
import { Mensagem } from "./mensagem";

export class Sessao {
    
    logado: boolean;
    isShowMenu: boolean;
    usuario: Usuario;
    token: Token;

    constructor() {
        this.isShowMenu = true;
    }
}