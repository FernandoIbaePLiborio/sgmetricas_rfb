import { Template } from "./template";
import { Fornecedor } from "../enumeration/fornecedor";

export class Importacao {
    
    arquivoBase64: String;
    idTemplate: number;
    fornecedor: Fornecedor;
    numeroDemanda: String;
    data: Date;

    constructor() {

    }
}