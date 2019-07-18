
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';
import { DataTable, FilterMetadata, ConfirmationService } from 'primeng/primeng';
import { Component, OnInit, Input, Output, ViewChild } from '@angular/core';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { ContagemService } from '../../services/contagem.service';
import { RestClientService } from '../../services/restclient.service';
import { MensagemService } from '../../services/mensagem.service';
import { MessageService } from 'primeng/components/common/messageservice';
import { SessaoService } from '../../services/sessao.service';
import { AbstractComponent } from '../../util/abstract.component';
import { ConsultaResult } from '../../util/consultaresult';
import { Contagem } from '../../model/contagem';
import { ContagemFiltro } from '../../filtro/contagem.filtro';
import { SelectItem } from 'primeng/components/common/selectitem';
import { Situacao } from '../../enumeration/situacao';
import { LazyLoadEvent } from 'primeng/components/common/lazyloadevent';
import { Config } from '../../model/config';
import { TipoContagem } from '../../enumeration/tipo-contagem';
import { TipoProjeto } from '../../enumeration/tipo-projeto';
import { Fornecedor } from '../../enumeration/fornecedor';
import { LoaderService } from '../../services/loader.service';
import { Paginador } from '../../model/paginador';
import { ItensPorPagina } from '../../model/itens-pagina';

declare var $: any;

@Component({
  selector: 'app-contagem-consulta',
  templateUrl: './contagem-consulta.component.html',
  styleUrls: ['./contagem-consulta.component.css'],
  providers: [
    ContagemService,
    RestClientService,
    MensagemService,
    MessageService,
    SessaoService,
    ConfirmationService,
    LoaderService
  ]
})

export class ContagemConsultaComponent extends AbstractComponent implements OnInit {
  @ViewChild(DataTable) dataTable: DataTable;
  result: ConsultaResult<Contagem>;
  filtro: ContagemFiltro;
  situacaoList: SelectItem[];
  tipoContagemList: SelectItem[];
  tipoProjetoList: SelectItem[];
  fornecedorList: SelectItem[];
  itensPorPagina: ItensPorPagina[] = [
    { rows: 10 },
    { rows: 50 },
    { rows: 100 }
  ];

  constructor(
    private contagemService: ContagemService,
    private sessaoService: SessaoService,
    private activatedRoute: ActivatedRoute,
    private mensagemService: MensagemService,
    private router: Router,
    private loaderService: LoaderService,
    private confirmationService: ConfirmationService
  ) {
    super(sessaoService);
    this.situacaoList = Situacao.getSelectItem();
    this.tipoContagemList = TipoContagem.getSelectItem();
    this.tipoProjetoList = TipoProjeto.getSelectItem();
    this.fornecedorList = Fornecedor.getSelectItem();
  }

  /**
   * Método chamado quando o componente é inicializado
   */
  ngOnInit() {
    this.loaderService.showLoader();
    if (this.activatedRoute.snapshot.params["message_desc"]) {
      var mensagem = this.getMensagem(this.activatedRoute.snapshot.params["message_desc"]);
      this.mensagemService.sucessoTitulo('Sucesso', mensagem);
    }
    this.result = new ConsultaResult<Contagem>();
    this.filtro = new ContagemFiltro();
    this.carregarPrimeiraPaginaComTamanhoPaginaPadrao();
  }

  /**
   * Método chamado ao clicar no botão buscar
   */
  onClickBuscar(): void {
    this.loaderService.showLoader();
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
    this.filtro = new ContagemFiltro()
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
    this.filtro = new ContagemFiltro();
  }

  /**
   * Método chamado ao clicar no botão apagar
   */
  onClickDelete = (contagem: Contagem) => {
    this.confirmationService.confirm({
      message: 'Confirma a exclusão do registro?',
      accept: () => {
        this.loaderService.showLoader();
        this.contagemService
          .excluir(contagem)
          .subscribe(
            voidResult => this.recarregarPaginaAtual(),
            error => { }
          );
      }
    });
  }

  /**
   * Método chamada ao clicar no botão alterar
   */
  onClickAlterar = (contagem: Contagem) => {
    if (contagem.situacao == Situacao[Situacao.CONCLUIDO.toString()]) {
      this.mensagemService.erroTitulo('Erro', 'A situação da contagem não permite alteração.');
      return;
    }
    this.router.navigate(['/pages', 'contagem-edicao', { id: contagem.id, acao: 'ALTERACAO' }]);
  }

  /**
   * Efetua a paginação da tabela
   */
  irParaPagina = (numeroPagina: number, tamanhoPagina: number): void => {
    this.loaderService.showLoader();
    this.filtro.offset = (numeroPagina - 1) * tamanhoPagina;
    this.filtro.limit = tamanhoPagina;
    this.contagemService
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

  /**
   * Realiza a exportação para XLS
   */
  exportXLS = () => {
    this.contagemService.exportacaoXLS(this.filtro).subscribe(
      result => {
        var link = document.createElement("a");
        link.download = "contagem.xls";
        link.href = "data:application/vnd.ms-excel;base64," + result;
        document.body.appendChild(link);
        link.click();
      },
      error => { }
    );
  }

  /**
   * Realiza a exportação para PDF
   */
  exportPDF = () => {
    this.contagemService.exportacaoPDF(this.filtro).subscribe(
      result => {
        var link = document.createElement("a");
        link.download = "contagem.pdf";
        link.href = "data:application/pdf;base64," + result;
        document.body.appendChild(link);
        link.click();
      },
      error => { }
    );
  }
}
