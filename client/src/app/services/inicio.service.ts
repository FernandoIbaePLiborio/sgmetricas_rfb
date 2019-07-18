import { Injectable } from "@angular/core";
import { RestClientService } from "./restclient.service";

@Injectable()
export class InicioService {

    private static RESOURCE_PATH: string = "/inicio";

    constructor(private restClient: RestClientService) {

    }
}