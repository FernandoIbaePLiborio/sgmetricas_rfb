import { MetodoContagem } from './../../enumeration/metodo-contagem';
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Validators, FormControl, FormGroup, FormBuilder } from '@angular/forms';
import { ContagemService } from '../../services/contagem.service';
import { RestClientService } from '../../services/restclient.service';
import { MensagemService } from '../../services/mensagem.service';
import { SessaoService } from '../../services/sessao.service';
import { MessageService } from 'primeng/components/common/messageservice';
import { Contagem } from '../../model/contagem';
import { Acao } from '../../enumeration/acao';
import { EnderecoService } from '../../services/endereco.service';
import { AbstractComponent } from '../../util/abstract.component';
import { Observable } from 'rxjs/Observable';
import { ErrorMessage } from '../../util/error-message';
import { SelectItem } from 'primeng/components/common/selectitem';
import { Situacao } from '../../enumeration/situacao';
import { TipoContagem } from '../../enumeration/tipo-contagem';
import { TipoProjeto } from '../../enumeration/tipo-projeto';
import { Fornecedor } from '../../enumeration/fornecedor';
import { ConsultaResult } from '../../util/consultaresult';
import { Funcionalidade } from '../../model/funcionalidade';
import { Config } from '../../model/config';
import { FuncionalidadeFiltro } from '../../filtro/funcionalidade.filtro';
import { FuncionalidadeService } from '../../services/funcionalidade.service';
import { LazyLoadEvent } from 'primeng/components/common/lazyloadevent';
import { BsModalService } from 'ngx-bootstrap/modal/bs-modal.service';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { TemplateRef } from '@angular/core/src/linker/template_ref';
import { TipoFuncionalidade } from '../../enumeration/tipo-funcionalidade';
import { TipoDemanda } from '../../enumeration/tipo-demanda';
import { Classificacao } from '../../enumeration/classificacao';
import { Complexidade } from '../../enumeration/complexidade';
import { Paginador } from '../../model/paginador';
import { ItensPorPagina } from '../../model/itens-pagina';
import { DataTable, ConfirmationService } from 'primeng/primeng';
import { LoaderService } from '../../services/loader.service';

declare var $: any;

@Component({
  selector: 'app-contagem-edicao',
  templateUrl: './contagem-edicao.component.html',
  styleUrls: ['./contagem-edicao.component.css'],
  providers: [
    ContagemService,
    FuncionalidadeService,
    RestClientService,
    MensagemService,
    SessaoService,
    MessageService,
    LoaderService,
    ConfirmationService]
})
export class ContagemEdicaoComponent extends AbstractComponent implements OnInit {
  @ViewChild(DataTable) dataTable: DataTable;
  result: ConsultaResult<Funcionalidade>;
  tituloPagina: string;
  tituloBotaoAcao: string;
  acao: Acao;
  readOnly: boolean;
  contagem: Contagem;
  funcionalidade: Funcionalidade;
  funcionalidadeFiltro: FuncionalidadeFiltro;
  modalRef: BsModalRef;
  itensPorPagina: ItensPorPagina[] = [
    { rows: 10 },
    { rows: 50 },
    { rows: 100 }
  ];

  situacaoList: SelectItem[];
  tipoContagemList: SelectItem[];
  tipoProjetoList: SelectItem[];
  metodoContagemList: SelectItem[];
  tipoFuncionalidadeList: SelectItem[];
  tipoDemandaList: SelectItem[];
  fornecedorList: SelectItem[];
  classificacaoList: SelectItem[];
  complexidadeList: SelectItem[];
  funcionalidadeList: ConsultaResult<Funcionalidade>;

  /**
   * Construtor padrão 
   */
  constructor(
    private contagemService: ContagemService,
    private funcionalidadeService: FuncionalidadeService,
    private router: Router,
    private route: ActivatedRoute,
    private mensagemService: MensagemService,
    private sessaoService: SessaoService,
    private modalService: BsModalService,
    private loaderService: LoaderService,
    private confirmationService: ConfirmationService) {
    super(sessaoService);
    this.situacaoList = Situacao.getSelectItem();
    this.tipoContagemList = TipoContagem.getSelectItem();
    this.tipoProjetoList = TipoProjeto.getSelectItem();
    this.fornecedorList = Fornecedor.getSelectItem();
    this.tipoFuncionalidadeList = TipoFuncionalidade.getSelectItem();
    this.tipoDemandaList = TipoDemanda.getSelectItem();
    this.classificacaoList = Classificacao.getSelectItem();
    this.complexidadeList = Complexidade.getSelectItem();
    this.metodoContagemList = MetodoContagem.getSelectItem();
  }

  /**
   * Método chamado na inicialização do componente
   */
  ngOnInit() {
    this.contagem = new Contagem();
    this.funcionalidade = new Funcionalidade();
    this.funcionalidadeFiltro = new FuncionalidadeFiltro();
    this.funcionalidadeList = new ConsultaResult<Funcionalidade>();

    let combinedObservable = Observable.combineLatest(this.route.params, this.route.queryParams,
      (params, queryParams) => ({ params, queryParams }));

    combinedObservable.subscribe(tupla => {
      let id: number = +tupla.params['id'];
      this.acao = Acao[(<string>tupla.params['acao']).toUpperCase()];
      this.readOnly = (this.acao == Acao.DETALHES);

      if (this.acao === Acao.ALTERACAO) {
        this.initAlteracao(id);
      } else if (this.acao === Acao.DETALHES) {
        this.initDetalhes(id);
      } else {
        alert("Ação '" + this.acao + "' inválida.");
      }
    });
  }

  /**
   * Inicia a tela em modo alteração
   */
  initAlteracao = (id: number): void => {
    this.tituloPagina = "Alteração de Contagem";
    this.tituloBotaoAcao = "Concluir";
    this.contagemService.obter(id).subscribe(
      result => {
        this.contagem = result;
        this.contagem.dataImportacao = new Date(result.dataImportacao);
        this.funcionalidadeFiltro.contagem = result.id;
        this.contagem.tipoProjeto;
        this.carregarPrimeiraPaginaComTamanhoPaginaPadrao();
      },
      error => ""
    );
  };

  /**
   * Inicia a tela em modo detalhes
   */
  initDetalhes = (id: number): void => {
    this.tituloPagina = "Detalhes da Contagem";
    this.tituloBotaoAcao = "Voltar";
    this.contagemService.obter(id).subscribe(
      result => {
        this.contagem = result;
        this.contagem.dataImportacao = new Date(result.dataImportacao);
        this.funcionalidadeFiltro.contagem = result.id;
        this.carregarPrimeiraPaginaComTamanhoPaginaPadrao();
      },
      error => ""
    );
  };

  /**
   * Método chamado ao clicar no botão voltar
   */
  onClickVoltar = (): void => {
    this.router.navigate(['/pages', 'contagem-consulta']);
  };

  /**
   * Método chamado ao clicar no botão de ação
   */
  onClickAcao = (): void => {
    if (this.acao === Acao.ALTERACAO) {
      this.alterar();
    } else {
      alert("Ação '" + this.acao + "' inválida.");
    }
  };

  /**
   * Realiza a alteração do endereço
   */
  alterar = (): void => {
    this.contagemService.alterar(this.contagem).subscribe(
      result => {
        this.contagem = result;
        this.router.navigate(['/pages', 'contagem-consulta', { message_desc: 'CONTAGEM_INFO_2' }]);
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
  validacaoBasica = (contagem: Contagem): boolean => {
    var isValid = true;
    var descricao: string[] = [];
    this.removerValidacaoBasica();
    if (contagem.linguagem == null || contagem.linguagem.trim() == "") {
      descricao.push("A linguagem é obrigatória.");
      $('#div_numeroProcesso').addClass('has-error');
      isValid = false;
    }
    if (contagem.fornecedor == null) {
      descricao.push("O fornecedor é obrigatório.");
      $('#div_cep').addClass('has-error');
      isValid = false;
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
    $('#div_numeroProcesso').removeClass('has-error');
    $('#div_cep').removeClass('has-error');
  }

  /**
   * Carrega a primeira página no datatable
   */
  carregarPrimeiraPaginaComTamanhoPaginaPadrao = (): void => {
    this.irParaPagina(1, Config.DEFAULT_PAGE_SIZE);
  };

  /**
   * Efetua a paginação da tabela
   */
  irParaPagina = (numeroPagina: number, tamanhoPagina: number): void => {
    this.loaderService.showLoader();
    this.funcionalidadeFiltro.offset = (numeroPagina - 1) * tamanhoPagina;
    this.funcionalidadeFiltro.limit = tamanhoPagina;
    this.funcionalidadeService
      .obterPaginado(this.funcionalidadeFiltro)
      .subscribe(
        result => {
          this.funcionalidadeList = result;
          setTimeout(() => {
            this.loaderService.hideLoader()
          }, 1000);
        },
        error => { 
          this.loaderService.hideLoader() 
        }
      );
  }

  /**
   * Carrega os dados para o datatable
   * @param event
   */
  loadData(event: LazyLoadEvent) {
    this.funcionalidadeList.paginador = new Paginador(this.dataTable.rows);
    this.irParaPagina(event.first / event.rows + 1, event.rows);
  }

  /**
   * Carrega a página inserida no campo input.
   * @param event 
   */
  setPage(event: number) {
    this.dataTable.reset();
    let paging = {
      first: ((event - 1) * this.dataTable.rows),
      rows: this.dataTable.rows
    };
    this.dataTable.first = paging.first;
    this.dataTable.paginate();
  }

  /**
   * Carrega a quantidade de linhas em dataTable.
   * @param event 
   */
  setRows(event: number) {
    this.dataTable.reset();
    this.dataTable.rows = event;
    this.dataTable.paginate()
  }

  /**
   * Carrega os filtros indicados para ordenação.
   * @param event 
   */
  setSortOrder(event: any) {
    this.funcionalidadeFiltro.desc = event.order == -1;
    this.funcionalidadeFiltro.sortField = event.field;
    this.dataTable.paginate();
  }

  /**
   * Configura a busca de acordo com os filtros.
   * @param event 
   */
  setFilter(event: any) {
    this.funcionalidadeFiltro = new FuncionalidadeFiltro()
    this.funcionalidadeFiltro.contagem = this.contagem.id;
    let json = JSON.stringify(event.filters);
    let objetos = JSON.parse(json);
    let valor, obj;
    for (let n in objetos) {
      JSON.parse(json, (key, value) => {
        if (typeof value === 'string' && n !== obj && value !== valor) {
          obj = n;
          valor = value;
          this.funcionalidadeFiltro[n] = value;
        }
      });
    }
    this.dataTable.paginate();
  }

  /**
   * Método chamado ao clicar no botão buscar
   */
  onClickBuscar(): void {
    this.loading = true;
    this.carregarPrimeiraPaginaComTamanhoPaginaPadrao();
  }

  /**
   * Método chamado ao clicar no botão apagar
   */
  onClickDelete = (funcionalidade: Funcionalidade) => {
    this.confirmationService.confirm({
      message: 'Confirma a exclusão do registro?',
      accept: () => {
        this.loaderService.showLoader();
        this.funcionalidadeService
          .excluir(funcionalidade)
          .subscribe(
            voidResult => this.carregarPrimeiraPaginaComTamanhoPaginaPadrao(),
            error => { }
          );
      }
    });
  }

  /** 
   * Limpa os dados de filtro 
   */
  onClickLimpar(): void {
    this.contagem = new Contagem();
  }

  /**
   * @param template 
   * @param funcionalidade 
   */
  openModal(template: TemplateRef<any>, funcionalidade: Funcionalidade) {
    this.modalRef = this.modalService.show(template);
    this.funcionalidade = JSON.parse(JSON.stringify(funcionalidade));
    this.funcionalidade = funcionalidade;
  }

  /**
   * 
   */
  salvarFuncionalidade = (): void => {
    this.dataTableVisible = false;
    if (!this.funcionalidade.pontoFuncao) {
      this.funcionalidade.pontoFuncao == 0;
    }
    this.funcionalidade.contagem = this.contagem;
    this.funcionalidadeService.alterar(this.funcionalidade).subscribe(
      result => {
        this.mensagemService.sucessoTitulo('Sucesso', 'Funcionalidade alterada com sucesso');
        setTimeout(() => this.dataTableVisible = true, 0);
      },
      error => {
        let errorMessages: ErrorMessage[] = ((error.json() as Object) as ErrorMessage[]);
        this.mensagemService.erroTitulo(ErrorMessage.descricao(errorMessages[0].error), errorMessages[0].error_description);
      }
    );
    this.modalRef.hide()
    this.dataTable.changeDetector.detectChanges()
  };

  /**
   * Realiza a exportação da grid de funcionalidades para XLS
   */
  exportXLS = () => {
    this.funcionalidadeService.exportacaoXLS(this.funcionalidadeFiltro).subscribe(
      result => {
        var link = document.createElement("a");
        link.download = "funcionalidade.xls";
        link.href = "data:application/vnd.ms-excel;base64," + result;
        document.body.appendChild(link);
        link.click();
      },
      error => { }
    );
  }

  /**
   * Realiza a exportação da grid de funcionalidades para PDF
   */
  exportPDF = () => {
    this.funcionalidadeService.exportacaoPDF(this.funcionalidadeFiltro).subscribe(
      result => {
        var link = document.createElement("a");
        link.download = "funcionlidade.pdf";
        link.href = "data:application/pdf;base64," + result;
        document.body.appendChild(link);
        link.click();
      },
      error => { }
    );
  }
}
