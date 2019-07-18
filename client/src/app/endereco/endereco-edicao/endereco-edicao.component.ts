import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {Validators,FormControl,FormGroup,FormBuilder} from '@angular/forms';
import { Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/combineLatest';

import { RestClientService } from '../../services/restclient.service';
import { EnderecoService } from '../../services/endereco.service';
import { Endereco } from '../../model/endereco';
import { Acao } from '../../enumeration/acao';
import { MessageService } from 'primeng/components/common/messageservice';
import { ErrorMessage } from '../../util/error-message';
import { AbstractComponent } from '../../util/abstract.component';
import { SessaoService } from '../../services/sessao.service';
import { MensagemService } from '../../services/mensagem.service';

declare var $ :any;

@Component({
  selector: 'app-endereco-edicao',
  templateUrl: './endereco-edicao.component.html',
  styleUrls: ['./endereco-edicao.component.css'],
  providers: [
    EnderecoService, 
    RestClientService, 
    MensagemService, 
    SessaoService, 
    MessageService]
})
export class EnderecoEdicaoComponent extends AbstractComponent implements OnInit {

  tituloPagina: string;
  tituloBotaoAcao: string;
  endereco: Endereco;
  acao: Acao;
  readOnly: boolean;

  /**
   * Construtor padrão 
   */
  constructor(
    private enderecoService: EnderecoService, 
    private router: Router, 
    private route: ActivatedRoute,
    private mensagemService: MensagemService,
    private sessaoService: SessaoService) {
      super(sessaoService);
  }

  /**
   * Método chamado na inicialização do componente
   */
  ngOnInit() {
    this.endereco = new Endereco();

    let combinedObservable = Observable.combineLatest(this.route.params, this.route.queryParams, 
      (params, queryParams) => ({ params, queryParams }));
    
    combinedObservable.subscribe( tupla => {
      let idEndereco: number = +tupla.params['id']; 
      this.acao = Acao[(<string>tupla.params['acao']).toUpperCase()]; 
      this.readOnly = (this.acao == Acao.DETALHES);
      
      if(this.acao === Acao.INCLUSAO) {
        this.initInclusao();
      } else if(this.acao === Acao.ALTERACAO) {
        this.initAlteracao(idEndereco);
      } else if(this.acao === Acao.DETALHES) {
        this.initDetalhes(idEndereco);
      } else {
        alert("Ação '" + this.acao + "' inválida.");
      }

    });
  }

  /**
   * Inicia a tela em modo inclusão
   */
  initInclusao = (): void => {
    this.tituloPagina = "Inclusão de Endereço";
    this.tituloBotaoAcao = "Incluir";
    this.endereco = new Endereco();
  };

  /**
   * Inicia a tela em modo alteração
   */
  initAlteracao = (idEndereco: number): void => {
    this.tituloPagina = "Alteração de Endereço";
    this.tituloBotaoAcao = "Alterar";
    this.enderecoService.findEndereco(idEndereco).subscribe(
      result => this.endereco = result,
      error => ""
    );
  };

  /**
   * Inicia a tela em modo detalhes
   */
  initDetalhes = (idEndereco: number): void => {
    this.tituloPagina = "Detalhes de Endereço";
    this.tituloBotaoAcao = "Voltar";
    this.enderecoService.findEndereco(idEndereco).subscribe(
      result => this.endereco = result,
      error => ""
    );
  };

  /**
   * Método chamado ao clicar no botão voltar
   */
  onClickVoltar = (): void => {
    this.router.navigate(['/pages', 'endereco-consulta']);
  };

  /**
   * Método chamado ao clicar no botão de ação
   */
  onClickAcao = (): void => {
    if(this.acao === Acao.INCLUSAO) {
      this.incluirEndereco();
    } else if(this.acao === Acao.ALTERACAO) {
      this.alterarEndereco();
    } else {
      alert("Ação '" + this.acao + "' inválida.");
    }    
  };

  /**
   * Realiza a inclusão do endereço
   */
  incluirEndereco = (): void => {
    if(!this.validacaoBasica(this.endereco)){
      return;
    }
    this.enderecoService.createEndereco(this.endereco).subscribe(
      (result: Endereco) => {
        this.endereco = new Endereco();
        this.router.navigate(['/pages','endereco-consulta',{ message_desc: 'ENDERECO_INFO_1'}]);
      },
      (error: Response) => {
        let errorMessages: ErrorMessage[] = ((error.json() as Object) as ErrorMessage[]);
        this.mensagemService.erroTitulo(ErrorMessage.descricao(errorMessages[0].error), errorMessages[0].error_description);
      }
    );
  };

  /**
   * Realiza a alteração do endereço
   */
  alterarEndereco = (): void => {
    this.enderecoService.updateEndereco(this.endereco).subscribe(
      result => {
        this.endereco = result;
        this.router.navigate(['/pages','endereco-consulta',{ message_desc: 'ENDERECO_INFO_2'}]);
      },
      error => {
        let errorMessages: ErrorMessage[] = ((error.json() as Object) as ErrorMessage[]);
        this.mensagemService.erroTitulo(ErrorMessage.descricao(errorMessages[0].error), errorMessages[0].error_description);
      }
    );
  };

  /**
   * Realiza a validação básica do formulário
   */
  validacaoBasica = (endereco: Endereco): boolean => {
    var isValid = true;
    var descricao: string[] = [];
    this.removerValidacaoBasica();
    if(endereco.numeroProcesso == null || endereco.numeroProcesso.trim() == ""){
      descricao.push("O número do processo é obrigatório.");
      $('#div_numeroProcesso').addClass('has-error');
      isValid = false;
    }
    if(endereco.cep == null){
      descricao.push("O cep é obrigatório.");
      $('#div_cep').addClass('has-error');
      isValid = false;
    }

    if(!isValid){
      this.mensagemService.erroAll(descricao);
    }

    return isValid;
  }

  /**
   * Remove a validação do formulário
   */
  removerValidacaoBasica = (): void => {
    $('#div_numeroProcesso').removeClass('has-error');
    $('#div_cep').removeClass('has-error');
  }
}
