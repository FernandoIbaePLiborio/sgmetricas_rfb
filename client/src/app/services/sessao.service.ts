import {Injectable} from '@angular/core';
import { Sessao } from '../model/sessao';

@Injectable()
export class SessaoService {

    sessao: Sessao;

    constructor() {
        if(this.sessao == null){
            this.sessao = new Sessao();
        }

        if(sessionStorage.getItem("sessaoGMETRICA")){
            this.sessao = JSON.parse(sessionStorage.getItem("sessaoGMETRICA"));
        }
    }
}
