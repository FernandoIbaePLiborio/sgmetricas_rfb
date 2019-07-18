import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { RestClientService } from '../../services/restclient.service';
import { MensagemService } from '../../services/mensagem.service';
import { MessageService } from 'primeng/components/common/messageservice';
import { SessaoService } from '../../services/sessao.service';
import { AbstractComponent } from '../../util/abstract.component';
import { Template } from '../../model/template';
import { ConsultaResult } from '../../util/consultaresult';
import { TemplateService } from '../../services/template.service';
import { TemplateFiltro } from '../../filtro/template.filtro';
import { LazyLoadEvent } from 'primeng/components/common/lazyloadevent';
import { Config } from '../../model/config';
import { SelectItem } from 'primeng/components/common/selectitem';
import { SituacaoTemplate } from '../../enumeration/situacao-template';
import { LoaderService } from '../../services/loader.service';
import { ItensPorPagina } from '../../model/itens-pagina';
import { DataTable, ConfirmationService } from 'primeng/primeng';
import { Paginador } from '../../model/paginador';

declare var $: any;

@Component({
  selector: 'app-template-consulta',
  templateUrl: './template-consulta.component.html',
  styleUrls: ['./template-consulta.component.css'],
  providers: [
    TemplateService,
    RestClientService,
    MensagemService,
    MessageService,
    SessaoService,
    ConfirmationService,
    LoaderService
  ]
})
export class TemplateConsultaComponent extends AbstractComponent implements OnInit {
  @ViewChild(DataTable) dataTable: DataTable;
  result: ConsultaResult<Template>;
  filtro: TemplateFiltro;
  situacaoList: SelectItem[];
  itensPorPagina: ItensPorPagina[] = [
    { rows: 10 },
    { rows: 50 },
    { rows: 100 }
  ];

  /**
   * Construtor padrão
   */
  constructor(
    private templateService: TemplateService,
    private sessaoService: SessaoService,
    private activatedRoute: ActivatedRoute,
    private mensagemService: MensagemService,
    private loaderService: LoaderService,
    private confirmationService: ConfirmationService
  ) {
    super(sessaoService);
    this.situacaoList = SituacaoTemplate.getSelectItem();
  }

  /**
   * Método chamado quando o componente é inicializado
   */
  ngOnInit() {
    if (this.activatedRoute.snapshot.params["message_desc"]) {
      var mensagem = this.getMensagem(this.activatedRoute.snapshot.params["message_desc"]);
      this.mensagemService.sucessoTitulo('Sucesso', mensagem);
    }
    this.result = new ConsultaResult<Template>();
    this.filtro = new TemplateFiltro();
    this.carregarPrimeiraPaginaComTamanhoPaginaPadrao();
  }

  /**
   * Método chamado ao clicar no botão buscar
   */
  onClickBuscar(): void {
    this.carregarPrimeiraPaginaComTamanhoPaginaPadrao();
  }

  /**
   * Carrega os dados para o datatable
   * @param event
   */
  loadData(event: LazyLoadEvent) {
    this.result.paginador = new Paginador(this.dataTable.rows);
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
    this.dataTable.paginate();
  }

  /**
   * Carrega os filtros indicados para ordenação.
   * @param event 
   */
  setSortOrder(event: any) {
    this.filtro.desc = event.order == -1;
    this.filtro.sortField = event.field;
    this.dataTable.paginate();
  }

  /**
   * Configura a busca de acordo com os filtros.
   * @param event 
   */
  setFilter(event: any) {
    this.filtro = new TemplateFiltro()
    let json = JSON.stringify(event.filters);
    let objetos = JSON.parse(json);
    let valor, obj;
    for (let n in objetos) {
      JSON.parse(json, (key, value) => {
        if (typeof value === 'string' && n !== obj && value !== valor) {
          obj = n;
          valor = value;
          this.filtro[n] = value;
        }
      });
    }
    this.dataTable.paginate();
  }


  /**
   * Método chamado ao clicar no botão limpar
   */
  onClickLimpar(): void {
    this.filtro.nome = null
    this.filtro.situacao = null;
  }

  /**
   * Método chamado ao clicar no botão apagar
   */
  onClickDelete = (template: Template) => {
    this.confirmationService.confirm({
      message: 'Confirma a exclusão do registro?',
      accept: () => {
        this.loaderService.showLoader();
        this.templateService
          .excluir(template)
          .subscribe(
            voidResult => {
              this.mensagemService.sucessoTitulo('Sucesso', this.getMensagem("TEMPLATE_INFO_3"));
              this.recarregarPaginaAtual();
            },
            error => { }
          );
      }
    });
  }

  /**
   * Efetua a paginação da tabela
   */
  irParaPagina = (numeroPagina: number, tamanhoPagina: number): void => {
    this.loaderService.showLoader();
    this.filtro.offset = (numeroPagina - 1) * tamanhoPagina;
    this.filtro.limit = tamanhoPagina;
    this.templateService
      .obterPaginado(this.filtro)
      .subscribe(
        result => {
          this.result = result;
          setTimeout(() => {
            this.loaderService.hideLoader();
          }, 1000);
        },
        error => {
          this.loaderService.hideLoader();
        }
      );
  }

  /**
   * Carrega a primeira página no datatable
   */
  carregarPrimeiraPaginaComTamanhoPaginaPadrao = (): void => {
    this.irParaPagina(1, Config.DEFAULT_PAGE_SIZE);
  };

  /**
   * Recarrega os dados do datatable
   */
  recarregarPaginaAtual = (): void => {

    this.irParaPagina(this.result.paginador.numeroPaginaAtual, this.result.paginador.numeroItensPorPagina);
  }
}
