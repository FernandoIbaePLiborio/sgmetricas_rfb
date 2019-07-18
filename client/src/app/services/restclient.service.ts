import * as configResource from '../../assets/resources/config.json';
import { Injectable } from "@angular/core";
import { Http, Response, Headers, RequestOptionsArgs } from '@angular/http';
import { Observable } from "rxjs/Observable";
import { Config } from "../model/config";
import 'rxjs/add/operator/map';
import { SessaoService } from "./sessao.service";
import { Configuracao } from '../model/configuracao';
import { Template } from '../model/template';
import { EventEmitter } from 'events';

@Injectable()
export class RestClientService {

  getConfiguracao = (chave: string): string => {
    var configuracoes: Configuracao[] = <any>configResource;
    return configuracoes.find(w => w.chave == chave).valor;
  }

  restApiPath: string = this.getConfiguracao('REST_API_PATH');

  constructor(private http: Http, private sessaoService: SessaoService) { }

  getResource<T>(resourcePath: string): Observable<Response> {
    let headers = new Headers();
    headers.append("Accept", "application/json");
    headers.append("Authorization", this.sessaoService.sessao.token.type + " " + this.sessaoService.sessao.token.key);
    let requestArgs: RequestOptionsArgs = {
      headers: headers
    };

    return this.http.get(this.restApiPath + resourcePath, requestArgs);
  }

  postResource<T>(resourcePath: string, obj: T): Observable<T> {

    var json = JSON.stringify(obj);
    let headers = new Headers();
    headers.append("Content-Type", "application/json");
    headers.append("Authorization", this.sessaoService.sessao.token.type + " " + this.sessaoService.sessao.token.key);
    let requestArgs: RequestOptionsArgs = {
      headers: headers
    };

    return this.http.post(this.restApiPath + resourcePath, json, requestArgs).map(response => response.json());
  }

  postResource2<T, U>(resourcePath: string, obj: T): Observable<U> {

    var json = JSON.stringify(obj);
    let headers = new Headers();
    headers.append("Content-Type", "application/json");
    headers.append("Authorization", this.sessaoService.sessao.token.type + " " + this.sessaoService.sessao.token.key);
    let requestArgs: RequestOptionsArgs = {
      headers: headers
    };

    return this.http.post(this.restApiPath + resourcePath, json, requestArgs).map(response => response.json());
  }

  postResource2NoAuthorization<T, U>(resourcePath: string, obj: T): Observable<U> {

    var json = JSON.stringify(obj);
    let headers = new Headers();
    headers.append("Content-Type", "application/json");
    let requestArgs: RequestOptionsArgs = {
      headers: headers
    };

    return this.http.post(this.restApiPath + resourcePath, json, requestArgs).map(response => response.json());
  }

  postMultiPart<T, U>(resourcePath: string, obj: T): Observable<U> {
    let headers = new Headers();
    headers.append("Content-Type", "multipart/form-data");
    headers.append("Authorization", this.sessaoService.sessao.token.type + " " + this.sessaoService.sessao.token.key);
    let requestArgs: RequestOptionsArgs = {
      headers: headers
    };

    return this.http.post(this.restApiPath + resourcePath, obj, requestArgs).map(response => response.json());

  }

  putResource<T>(resourcePath: string, obj: T): Observable<T> {

    var json = JSON.stringify(obj);
    let headers = new Headers();
    headers.append("Content-Type", "application/json");
    headers.append("Authorization", this.sessaoService.sessao.token.type + " " + this.sessaoService.sessao.token.key);
    let requestArgs: RequestOptionsArgs = {
      headers: headers
    };

    return this.http.put(this.restApiPath + resourcePath, json, requestArgs).map(response => response.json());

  }

  deleteResource(resourcePath: string): Observable<void> {

    let headers = new Headers();
    headers.append("Content-Type", "application/json");
    headers.append("Authorization", this.sessaoService.sessao.token.type + " " + this.sessaoService.sessao.token.key);
    let requestArgs: RequestOptionsArgs = {
      headers: headers
    };

    return this.http.delete(this.restApiPath + resourcePath, requestArgs).map(response => null);
  }
}