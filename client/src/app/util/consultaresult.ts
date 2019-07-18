import { Paginador } from "../model/paginador";
import { Config } from "../model/config";

export class ConsultaResult<T> {
    
    constructor(
        public itensPagina: T[] = [], 
        public paginador: Paginador = new Paginador(Config.DEFAULT_PAGE_SIZE)) {
    }

}