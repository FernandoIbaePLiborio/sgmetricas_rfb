import { MetodoContagem } from './../../enumeration/metodo-contagem';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Validators, FormControl, FormGroup, FormBuilder } from '@angular/forms';
import { Response } from '@angular/http';
import { TemplateService } from '../../services/template.service';
import { RestClientService } from '../../services/restclient.service';
import { MensagemService } from '../../services/mensagem.service';
import { SessaoService } from '../../services/sessao.service';
import { MessageService } from 'primeng/components/common/messageservice';
import { AbstractComponent } from '../../util/abstract.component';
import { Acao } from '../../enumeration/acao';
import { Template } from '../../model/template';
import { Observable } from 'rxjs/Observable';
import { ErrorMessage } from '../../util/error-message';
import { TemplateContagem } from '../../model/template-contagem';
import { TemplateFuncionalidade } from '../../model/template-funcionalidade';
import { SelectItem } from 'primeng/components/common/selectitem';
import { SituacaoTemplate } from '../../enumeration/situacao-template';
import { TipoFuncionalidade } from '../../enumeration/tipo-funcionalidade';
import { ConsultaResult } from '../../util/consultaresult';
import { TemplateFiltro } from '../../filtro/template.filtro';

declare var $: any;

@Component({
  selector: 'app-template-edicao',
  templateUrl: './template-edicao.component.html',
  styleUrls: ['./template-edicao.component.css'],
  providers: [
    TemplateService,
    RestClientService,
    MensagemService,
    SessaoService,
    MessageService
  ]
})
export class TemplateEdicaoComponent extends AbstractComponent implements OnInit {

  tituloPagina: string;
  tituloBotaoAcao: string;
  acao: Acao;
  readOnly: boolean;
  template: Template;
  templateList: Template[];
  templateContagem: TemplateContagem;
  templateFuncionalidadeAli: TemplateFuncionalidade;
  templateFuncionalidadeAie: TemplateFuncionalidade;
  templateFuncionalidadeEE: TemplateFuncionalidade;
  templateFuncionalidadeCE: TemplateFuncionalidade;
  templateFuncionalidadeSE: TemplateFuncionalidade;
  situacaoList: SelectItem[];
  filtroTemplate: TemplateFiltro;

  /**
   * Construtor padrão
   */
  constructor(
    private templateService: TemplateService,
    private router: Router,
    private route: ActivatedRoute,
    private mensagemService: MensagemService,
    private sessaoService: SessaoService) {
    super(sessaoService);
    this.situacaoList = SituacaoTemplate.getSelectItem();
  }

  /**
   * Método chamado na inicialização do componente
   */
  ngOnInit() {
    this.template = new Template();
    this.templateList = [];
    this.filtroTemplate = new TemplateFiltro();
    this.templateContagem = new TemplateContagem();
    this.templateFuncionalidadeAli = new TemplateFuncionalidade();
    this.templateFuncionalidadeAli.tipoFuncionalidade = TipoFuncionalidade.ALI;
    this.templateFuncionalidadeAie = new TemplateFuncionalidade();
    this.templateFuncionalidadeAie.tipoFuncionalidade = TipoFuncionalidade.AIE;
    this.templateFuncionalidadeEE = new TemplateFuncionalidade();
    this.templateFuncionalidadeEE.tipoFuncionalidade = TipoFuncionalidade.EE;
    this.templateFuncionalidadeCE = new TemplateFuncionalidade();
    this.templateFuncionalidadeCE.tipoFuncionalidade = TipoFuncionalidade.CE;
    this.templateFuncionalidadeSE = new TemplateFuncionalidade();
    this.templateFuncionalidadeSE.tipoFuncionalidade = TipoFuncionalidade.SE;
    this.templateService.obterPorFiltro(this.filtroTemplate).subscribe(templateList => this.templateList = templateList)
    let combinedObservable = Observable.combineLatest(this.route.params, this.route.queryParams,
      (params, queryParams) => ({ params, queryParams }));

    combinedObservable.subscribe(tupla => {
      let idEndereco: number = +tupla.params['id'];
      this.acao = Acao[(<string>tupla.params['acao']).toUpperCase()];
      this.readOnly = (this.acao == Acao.DETALHES);

      if (this.acao === Acao.INCLUSAO) {
        this.initInclusao();
      } else if (this.acao === Acao.ALTERACAO) {
        this.initAlteracao(idEndereco);
      } else if (this.acao === Acao.DETALHES) {
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
    this.tituloPagina = "Inclusão de Template";
    this.tituloBotaoAcao = "Incluir";
    this.template = new Template();
  };

  /**
   * Inicia a tela em modo alteração
   */
  initAlteracao = (id: number): void => {
    this.tituloPagina = "Alteração de Template";
    this.tituloBotaoAcao = "Concluir";
    this.templateService.obter(id).subscribe(
      result => {
        this.populaDados(result);
      },
      error => ""
    );
  };

  /**
   * Inicia a tela em modo detalhes
   */
  initDetalhes = (id: number): void => {
    this.tituloPagina = "Detalhes do Template";
    this.tituloBotaoAcao = "Voltar";
    this.templateService.obter(id).subscribe(
      result => this.populaDados(result),
      error => ""
    );
  };

  /**
   * Método chamado ao clicar no botão voltar
   */
  onClickVoltar = (): void => {
    this.router.navigate(['/pages', 'template-consulta']);
  };

  /**
   * Método chamado ao clicar no botão de ação
   */
  onClickAcao = (): void => {
    if (this.acao === Acao.INCLUSAO) {
      this.incluir();
    } else if (this.acao === Acao.ALTERACAO) {
      this.alterar();
    } else {
      alert("Ação '" + this.acao + "' inválida.");
    }
  };

  /**
   * Realiza a inclusão do template
   */
  incluir = (): void => {
    this.template.templateContagem = this.templateContagem;
    var templateFuncionalidadeList: TemplateFuncionalidade[] = [];
    templateFuncionalidadeList.push(this.templateFuncionalidadeAli);
    templateFuncionalidadeList.push(this.templateFuncionalidadeAie);
    templateFuncionalidadeList.push(this.templateFuncionalidadeEE);
    templateFuncionalidadeList.push(this.templateFuncionalidadeCE);
    templateFuncionalidadeList.push(this.templateFuncionalidadeSE);
    this.template.templateFuncionalidadeList = templateFuncionalidadeList;

    if (!this.validacaoBasica(this.template)) {
      return;
    }
    this.templateService.incluir(this.template).subscribe(
      (result: Template) => {
        this.template = new Template();
        this.router.navigate(['/pages', 'template-consulta', { message_desc: 'TEMPLATE_INFO_1' }]);
      },
      (error: Response) => {
        let errorMessages: ErrorMessage[] = ((error.json() as Object) as ErrorMessage[]);
        this.mensagemService.erroTitulo(ErrorMessage.descricao(errorMessages[0].error), errorMessages[0].error_description);
      }
    );
  };

  /**
   * Realiza a alteração do template
   */
  alterar = (): void => {
    this.template.templateContagem = this.templateContagem;
    var templateFuncionalidadeList: TemplateFuncionalidade[] = [];
    templateFuncionalidadeList.push(this.templateFuncionalidadeAli);
    templateFuncionalidadeList.push(this.templateFuncionalidadeAie);
    templateFuncionalidadeList.push(this.templateFuncionalidadeEE);
    templateFuncionalidadeList.push(this.templateFuncionalidadeCE);
    templateFuncionalidadeList.push(this.templateFuncionalidadeSE);
    this.template.templateFuncionalidadeList = templateFuncionalidadeList;
    if (!this.validacaoBasica(this.template)) {
      return;
    }
    this.templateService.alterar(this.template).subscribe(
      result => {
        this.template = result;
        this.router.navigate(['/pages', 'template-consulta', { message_desc: 'TEMPLATE_INFO_2' }]);
      },
      error => {
        let errorMessages: ErrorMessage[] = ((error.json() as Object) as ErrorMessage[]);
        this.mensagemService.erroTitulo(ErrorMessage.descricao(errorMessages[0].error), errorMessages[0].error_description);
      }
    );
  };

  /**
   * Na alteração ou detalhes popula as variáveis de acordo com o retorno
   */
  populaDados = (template: Template) => {
    this.template = template;
    this.templateContagem = template.templateContagem;
    for (let f of template.templateFuncionalidadeList) {
      if (f.tipoFuncionalidade == TipoFuncionalidade.ALI) {
        this.templateFuncionalidadeAli = f;
        this.templateFuncionalidadeAli.template = null;
        this.templateFuncionalidadeAli.tipoFuncionalidade = f.tipoFuncionalidade;
      }
      else if (f.tipoFuncionalidade == TipoFuncionalidade.AIE) {
        this.templateFuncionalidadeAie = f;
        this.templateFuncionalidadeAie.template = null;
        this.templateFuncionalidadeAie.tipoFuncionalidade = f.tipoFuncionalidade;
      }
      else if (f.tipoFuncionalidade == TipoFuncionalidade.EE) {
        this.templateFuncionalidadeEE = f;
        this.templateFuncionalidadeEE.template = null;
        this.templateFuncionalidadeEE.tipoFuncionalidade = f.tipoFuncionalidade;
      }
      else if (f.tipoFuncionalidade == TipoFuncionalidade.CE) {
        this.templateFuncionalidadeCE = f;
        this.templateFuncionalidadeCE.template = null;
        this.templateFuncionalidadeCE.tipoFuncionalidade = f.tipoFuncionalidade;
      }
      else if (f.tipoFuncionalidade == TipoFuncionalidade.SE) {
        this.templateFuncionalidadeSE = f;
        this.templateFuncionalidadeSE.template = null;
        this.templateFuncionalidadeSE.tipoFuncionalidade = f.tipoFuncionalidade;
      }
    }
  }

  /** 
   * Limpa os dados de filtro 
   */
  onClickLimpar(): void {
    this.template = new Template();
  }

  /**
   * Realiza a validação básica do formulário
   */
  validacaoBasica = (template: Template): boolean => {
    var isValid = true;
    var descricao: string[] = [];
    this.removerValidacaoBasica();
    if (template.nome == null || template.nome.trim() == "") {
      descricao.push("O nome é obrigatório.");
      $('#templateNome').parent().addClass('has-error');
      isValid = false;
    }
    if (template.situacao == null) {
      descricao.push("A situação é obrigatória.");
      $('#templateSituacao').parent().addClass('has-error');
      isValid = false;
    }
    if (template.id !== undefined) {
      let templateRecebido = this.templateList.find(t => t.id == template.id)
      if (templateRecebido.nome !== template.nome) {
        if (this.templateList.find(t => t.nome == template.nome)) {
          descricao.push("O nome do template já está cadastrado.");
          $('#templateSituacao').parent().addClass('has-error');
          isValid = false;
        }
      }
    } else {
      if (this.templateList.find(t => t.nome == template.nome)) {
        descricao.push("O nome do template já está cadastrado.");
        $('#templateSituacao').parent().addClass('has-error');
        isValid = false;
      }
    }
    if (template.templateContagem.metodoContagemAba == null) {
      descricao.push("O campo aba método de contagem é obrigatório.");
      $('#templateContagemMetodoContagemAba').parent().addClass('has-error');
      isValid = false;
    }
    if (template.templateContagem.metodoContagemReferencia == null) {
      descricao.push("O campo método de contagem é obrigatório.");
      $('#templateContagemMetodoContagemReferencia').parent().addClass('has-error');
      isValid = false;
    }

    /*if(template.templateContagem.projetoAba == null){
      descricao.push("O campo aba projeto da contagem é obrigatório.");
      $('#templateContagemProjetoAba').parent().addClass('has-error');
      isValid = false;
    }
    if(template.templateContagem.projetoReferencia == null){
      descricao.push("O campo referência projeto da contagem é obrigatório.");
      $('#templateContagemProjetoReferencia').parent().addClass('has-error');
      isValid = false;
    }
    if(template.templateContagem.linguagemAba == null){
      descricao.push("O campo aba liguagem da contagem é obrigatório.");
      $('#templateContagemLinguagemAba').parent().addClass('has-error');
      isValid = false;
    }
    if(template.templateContagem.linguagemReferencia == null){
      descricao.push("O campo referência linguagem da contagem é obrigatório.");
      $('#templateContagemLinguagemReferencia').parent().addClass('has-error');
      isValid = false;
    }
    if(template.templateContagem.tipoProjetoAba == null){
      descricao.push("O campo aba tipo de projeto da contagem é obrigatório.");
      $('#templateContagemTipoProjetoAba').parent().addClass('has-error');
      isValid = false;
    }
    if(template.templateContagem.tipoProjetoReferencia == null){
      descricao.push("O campo referência tipo de projeto da contagem é obrigatório.");
      $('#templateContagemTipoProjetoReferencia').parent().addClass('has-error');
      isValid = false;
    }
    if(template.templateContagem.tipoContagemAba == null){
      descricao.push("O campo aba tipo da contagem é obrigatório.");
      $('#templateContagemTipoContagemAba').parent().addClass('has-error');
      isValid = false;
    }
    if(template.templateContagem.tipoContagemReferencia == null){
      descricao.push("O campo referência tipo da contagem é obrigatório.");
      $('#templateContagemTipoContagemReferencia').parent().addClass('has-error');
      isValid = false;
    }
    if(template.templateContagem.plataformaAba == null){
      descricao.push("O campo aba plataforma da contagem é obrigatório.");
      $('#templateContagemPlataformaAba').parent().addClass('has-error');
      isValid = false;
    }
    if(template.templateContagem.plataformaReferencia == null){
      descricao.push("O campo referência plataforma da contagem é obrigatório.");
      $('#templateContagemPlataformaReferencia').parent().addClass('has-error');
      isValid = false;
    }
    if(template.templateContagem.liderProjetoAba == null){
      descricao.push("O campo aba líder projeto é obrigatório.");
      $('#templateContagemLiderProjetoAba').parent().addClass('has-error');
      isValid = false;
    }
    if(template.templateContagem.liderProjetoReferencia == null){
      descricao.push("O campo referência líder projeto é obrigatório.");
      $('#templateContagemLiderProjetoReferencia').parent().addClass('has-error');
      isValid = false;
    }
    if(template.templateContagem.observacoesAba == null){
      descricao.push("O campo aba observações da contagem é obrigatório.");
      $('#templateContagemObservacoesAba').parent().addClass('has-error');
      isValid = false;
    }
    if(template.templateContagem.observacoesReferencia == null){
      descricao.push("O campo referência observações da contagem é obrigatório.");
      $('#templateContagemObservacoesReferencia').parent().addClass('has-error');
      isValid = false;
    }
    if(template.templateContagem.artefatosUsadosAba == null){
      descricao.push("O campo aba artefatos usados da contagem é obrigatório.");
      $('#templateContagemArtefatosUsadosAba').parent().addClass('has-error');
      isValid = false;
    }
    if(template.templateContagem.artefatorUsadosReferencia == null){
      descricao.push("O campo referência artefatos usados da contagem é obrigatório.");
      $('#templateContagemArtefatorUsadosReferencia').parent().addClass('has-error');
      isValid = false;
    }
    if(template.templateContagem.propositoEscopoAba == null){
      descricao.push("O campo aba propósito escopo da contagem é obrigatório.");
      $('#templateContagemPropositoEscopoAba').parent().addClass('has-error');
      isValid = false;
    }
    if(template.templateContagem.propositoEscopoReferencia == null){
      descricao.push("O campo referência propósito escopo da contagem é obrigatório.");
      $('#templateContagemPropositoEscopoReferencia').parent().addClass('has-error');
      isValid = false;
    }
    if(template.templateContagem.fronteiraAba == null){
      descricao.push("O campo aba fronteira da contagem é obrigatório.");
      $('#templateContagemFronteiraAba').parent().addClass('has-error');
      isValid = false;
    }
    if(template.templateContagem.fronteiraReferencia == null){
      descricao.push("O campo referência fronteira da contagem é obrigatório.");
      $('#templateContagemFronteiraReferencia').parent().addClass('has-error');
      isValid = false;
    }
    */
    if (template.templateContagem.totalPfDemandaAba == null) {
      descricao.push("O campo aba total pf demanda é obrigatório.");
      $('#templateContagemTotalPfDemandaAba').parent().addClass('has-error');
      isValid = false;
    }
    if (template.templateContagem.totalPfDemandaReferencia == null) {
      descricao.push("O campo referência total pf demanda é obrigatório.");
      $('#templateContagemTotalPfDemandaReferencia').parent().addClass('has-error');
      isValid = false;
    }
    if (template.templateContagem.projetoAba == null) {
      descricao.push("O campo aba projeto é obrigatório.");
      $('#templateContagemProjetoAba').parent().addClass('has-error');
      isValid = false;
    }
    if (template.templateContagem.projetoReferencia == null) {
      descricao.push("O campo referência projeto é obrigatório.");
      $('#templateContagemProjetoReferencia').parent().addClass('has-error');
      isValid = false;
    }
    /*
    if(template.templateContagem.pfFuncaoDadosAba == null){
      descricao.push("O campo aba pf função dados é obrigatório.");
      $('#templateContagemPfFuncaoDadosAba').parent().addClass('has-error');
      isValid = false;
    }
    if(template.templateContagem.pfFuncaoDadosReferencia == null){
      descricao.push("O campo referência pf função dados é obrigatório.");
      $('#templateContagemPfFuncaoDadosReferencia').parent().addClass('has-error');
      isValid = false;
    }
    if(template.templateContagem.pfRetrabalhoAba == null){
      descricao.push("O campo aba pf retrabalho é obrigatório.");
      $('#templateContagemPfRetrabalhoAba').parent().addClass('has-error');
      isValid = false;
    }
    if(template.templateContagem.pfRetrabalhoReferencia == null){
      descricao.push("O campo referência pf retrabalho é obrigatório.");
      $('#templateContagemPfRetrabalhoReferencia').parent().addClass('has-error');
      isValid = false;
    }
    if(template.templateContagem.pfFuncaoTransacionalAba == null){
      descricao.push("O campo aba pf função transacional é obrigatório.");
      $('#templateContagemPfFuncaoTransacionalAba').parent().addClass('has-error');
      isValid = false;
    }
    if(template.templateContagem.pfFuncaoTransacionalReferencia == null){
      descricao.push("O campo referência pf função transacional é obrigatório.");
      $('#templateContagemPfFuncaoTransacionalReferencia').parent().addClass('has-error');
      isValid = false;
    }
    if(template.templateContagem.totalPfBrutoAba == null){
      descricao.push("O campo aba total pf bruto é obrigatório.");
      $('#templateContagemTotalPfBrutoAba').parent().addClass('has-error');
      isValid = false;
    }
    if(template.templateContagem.totalPfBrutoReferencia == null){
      descricao.push("O campo referência total pf bruto é obrigatório.");
      $('#templateContagemTotalPfBrutoReferencia').parent().addClass('has-error');
      isValid = false;
    }
    if(template.templateContagem.scopeCreepAba == null){
      descricao.push("O campo aba scope creep é obrigatório.");
      $('#templateContagemScopeCreepAba').parent().addClass('has-error');
      isValid = false;
    }
    if(template.templateContagem.scopeCreepReferencia == null){
      descricao.push("O campo referência scope creep é obrigatório.");
      $('#templateContagemScopeCreepReferencia').parent().addClass('has-error');
      isValid = false;
    }*/
    for (let f of template.templateFuncionalidadeList) {
      /*
      if(f.nomeFuncionalidadeAba == null){
        descricao.push("O campo aba nome " + f.tipoFuncionalidade + " é obrigatório.");
        $('#templateFuncionalidade' + f.tipoFuncionalidade + 'nomeFuncionalidadeAba').parent().addClass('has-error');
        isValid = false;
      }*/
      if (f.nomeFuncionalidadeReferencia == null) {
        descricao.push("O campo referência nome " + f.tipoFuncionalidade + " é obrigatório.");
        $('#templateFuncionalidade' + f.tipoFuncionalidade + 'nomeFuncionalidadeReferencia').parent().addClass('has-error');
        isValid = false;
      }
      /*
      if(f.tipoAba == null){
        descricao.push("O campo aba tipo " + f.tipoFuncionalidade + " é obrigatório.");
        $('#templateFuncionalidade' + f.tipoFuncionalidade + 'tipoAba').parent().addClass('has-error');
        isValid = false;
      }*/
      if (f.tipoReferencia == null) {
        descricao.push("O campo referência tipo " + f.tipoFuncionalidade + " é obrigatório.");
        $('#templateFuncionalidade' + f.tipoFuncionalidade + 'tipoReferencia').parent().addClass('has-error');
        isValid = false;
      }
      /*
      if(f.tipoDemandaAba == null){
        descricao.push("O campo aba tipo de demanda " + f.tipoFuncionalidade + " é obrigatório.");
        $('#templateFuncionalidade' + f.tipoFuncionalidade + 'tipoDemandaAba').parent().addClass('has-error');
        isValid = false;
      }*/
      if (f.tipoDemandaReferencia == null) {
        descricao.push("O campo referência tipo de demanda " + f.tipoFuncionalidade + " é obrigatório.");
        $('#templateFuncionalidade' + f.tipoFuncionalidade + 'tipoDemandaReferencia').parent().addClass('has-error');
        isValid = false;
      }
      if (f.rlArAba == null) {
        descricao.push("O campo aba RL/AR " + f.tipoFuncionalidade + " é obrigatório.");
        $('#templateFuncionalidade' + f.tipoFuncionalidade + 'rlArAba').parent().addClass('has-error');
        isValid = false;
      }
      if (f.rlArReferencia == null) {
        descricao.push("O campo referência RL/AR " + f.tipoFuncionalidade + " é obrigatório.");
        $('#templateFuncionalidade' + f.tipoFuncionalidade + 'rlArReferencia').parent().addClass('has-error');
        isValid = false;
      }
      if (f.memoriaRlArAba == null) {
        descricao.push("O campo aba memória RL/AR " + f.tipoFuncionalidade + " é obrigatório.");
        $('#templateFuncionalidade' + f.tipoFuncionalidade + 'memoriaRlArAba').parent().addClass('has-error');
        isValid = false;
      }
      if (f.memoriaRlArReferencia == null) {
        descricao.push("O campo referência memória RL/AR " + f.tipoFuncionalidade + " é obrigatório.");
        $('#templateFuncionalidade' + f.tipoFuncionalidade + 'memoriaRlArReferencia').parent().addClass('has-error');
        isValid = false;
      }
      if (f.rastreabilidadeAba == null) {
        descricao.push("O campo aba rastreabilidade " + f.tipoFuncionalidade + " é obrigatório.");
        $('#templateFuncionalidade' + f.tipoFuncionalidade + 'rastreabilidadeAba').parent().addClass('has-error');
        isValid = false;
      }
      if (f.rastreabilidadeReferencia == null) {
        descricao.push("O campo referência rastreabilidade " + f.tipoFuncionalidade + " é obrigatório.");
        $('#templateFuncionalidade' + f.tipoFuncionalidade + 'rastreabilidadeReferencia').parent().addClass('has-error');
        isValid = false;
      }
      if (f.tdAba == null) {
        descricao.push("O campo aba td " + f.tipoFuncionalidade + " é obrigatório.");
        $('#templateFuncionalidade' + f.tipoFuncionalidade + 'tdAba').parent().addClass('has-error');
        isValid = false;
      }
      if (f.tdReferencia == null) {
        descricao.push("O campo referência td " + f.tipoFuncionalidade + " é obrigatório.");
        $('#templateFuncionalidade' + f.tipoFuncionalidade + 'tdReferencia').parent().addClass('has-error');
        isValid = false;
      }
      if (f.memoriaTdAba == null) {
        descricao.push("O campo aba memória td " + f.tipoFuncionalidade + " é obrigatório.");
        $('#templateFuncionalidade' + f.tipoFuncionalidade + 'memoriaTdAba').parent().addClass('has-error');
        isValid = false;
      }
      if (f.memoriaTdReferencia == null) {
        descricao.push("O campo referência memória td " + f.tipoFuncionalidade + " é obrigatório.");
        $('#templateFuncionalidade' + f.tipoFuncionalidade + 'memoriaTdReferencia').parent().addClass('has-error');
        isValid = false;
      }
      if (f.classificacaoAba == null) {
        descricao.push("O campo aba classificação " + f.tipoFuncionalidade + " é obrigatório.");
        $('#templateFuncionalidade' + f.tipoFuncionalidade + 'classificacaoAba').parent().addClass('has-error');
        isValid = false;
      }
      if (f.classificacaoReferencia == null) {
        descricao.push("O campo referência classificação " + f.tipoFuncionalidade + " é obrigatório.");
        $('#templateFuncionalidade' + f.tipoFuncionalidade + 'classificacaoReferencia').parent().addClass('has-error');
        isValid = false;
      }
      if (f.complexidadeAba == null) {
        descricao.push("O campo aba complexidade " + f.tipoFuncionalidade + " é obrigatório.");
        $('#templateFuncionalidade' + f.tipoFuncionalidade + 'complexidadeAba').parent().addClass('has-error');
        isValid = false;
      }
      if (f.complexidadeReferencia == null) {
        descricao.push("O campo referência complexidade " + f.tipoFuncionalidade + " é obrigatório.");
        $('#templateFuncionalidade' + f.tipoFuncionalidade + 'complexidadeReferencia').parent().addClass('has-error');
        isValid = false;
      }
      if (f.pontoFuncaoAba == null) {
        descricao.push("O campo aba ponto função " + f.tipoFuncionalidade + " é obrigatório.");
        $('#templateFuncionalidade' + f.tipoFuncionalidade + 'pontoFuncaoAba').parent().addClass('has-error');
        isValid = false;
      }
      if (f.pontoFuncaoReferencia == null) {
        descricao.push("O campo referência ponto função " + f.tipoFuncionalidade + " é obrigatório.");
        $('#templateFuncionalidade' + f.tipoFuncionalidade + 'pontoFuncaoReferencia').parent().addClass('has-error');
        isValid = false;
      }
      if (f.divergenciasAba == null) {
        descricao.push("O campo aba divergências " + f.tipoFuncionalidade + " é obrigatório.");
        $('#templateFuncionalidade' + f.tipoFuncionalidade + 'divergenciasAba').parent().addClass('has-error');
        isValid = false;
      }
      if (f.divergenciasReferencia == null) {
        descricao.push("O campo referência divergências " + f.tipoFuncionalidade + " é obrigatório.");
        $('#templateFuncionalidade' + f.tipoFuncionalidade + 'divergenciasReferencia').parent().addClass('has-error');
        isValid = false;
      }
    }

    if (!isValid) {
      this.mensagemService.erroAll(descricao);
    }

    return isValid;
  }

  /**
   * Remove a validação do formulário
   */
  removerValidacaoBasica = (): void => {
    $('div').removeClass('has-error');
  }
}
