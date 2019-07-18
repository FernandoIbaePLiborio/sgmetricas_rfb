import { TipoRelatorio } from './../../enumeration/tipo-relatorio';
import { Filtro } from './../../model/filtro';
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { Headers, RequestOptions } from '@angular/http';
import { RestClientService } from '../../services/restclient.service';
import { MensagemService } from '../../services/mensagem.service';
import { MessageService } from 'primeng/components/common/messageservice';
import { SessaoService } from '../../services/sessao.service';
import { AbstractComponent } from '../../util/abstract.component';
import { ConsultaResult } from '../../util/consultaresult';
import { Funcionalidade } from '../../model/funcionalidade';
import { FuncionalidadeFiltro } from '../../filtro/funcionalidade.filtro';
import { LazyLoadEvent } from 'primeng/components/common/lazyloadevent';
import { FuncionalidadeService } from '../../services/funcionalidade.service';
import { Config } from '../../model/config';
import { Situacao } from '../../enumeration/situacao';
import { ItensPorPagina } from '../../model/itens-pagina';
import { DataTable, DataGridModule } from 'primeng/primeng';
import { Paginador } from '../../model/paginador';
import { LoaderService } from '../../services/loader.service';

declare var $: any;

@Component({
  selector: 'app-relatorio-resultado',
  templateUrl: './relatorio-resultado.component.html',
  styleUrls: ['./relatorio-resultado.component.css'],
  providers: [
    FuncionalidadeService,
    RestClientService,
    MensagemService,
    MessageService,
    SessaoService,
    LoaderService]
})

export class RelatorioResultadoComponent extends AbstractComponent implements OnInit {
  @ViewChild(DataTable) dataTable: DataTable;
  result: ConsultaResult<Funcionalidade>;
  filtro: FuncionalidadeFiltro;
  dataDe: Date;
  dataAte: Date;
  projeto: string;
  fronteira: string;
  dataImportacao: Date;
  tipoRelatorioTemp: string;
  itensPorPagina: ItensPorPagina[] = [
    { rows: 10 },
    { rows: 50 },
    { rows: 100 }
  ];
  
  /**
   * Construtor padrão
   */
  constructor(
    private funcionalidadeService: FuncionalidadeService,
    private sessaoService: SessaoService,
    private activatedRoute: ActivatedRoute,
    private mensagemService: MensagemService,
    private router: Router,
    private loaderService: LoaderService,
  ) {
    super(sessaoService);
  }

  /**
   * Método chamado quando o componente é inicializado
   */
  ngOnInit() {
    this.result = new ConsultaResult<Funcionalidade>();
    this.filtro = JSON.parse(sessionStorage.getItem("funcionalidadeFiltro"));
    if (this.fronteira === undefined && this.projeto === undefined){
      this.dataDe = this.filtro.dataImportacaoDe;
      this.dataAte = this.filtro.dataImportacaoAte;
      this.projeto = this.filtro.projeto;
      this.fronteira = this.filtro.fronteira;
      this.tipoRelatorioTemp = this.getTipoRelatorioDescricao(this.filtro.tipoRelatorio);
    }
    
    this.carregarPrimeiraPaginaComTamanhoPaginaPadrao();
  }

  /**
   * Carrega os dados para o datatable
   * @param event 
   */
  loadData(event: any) {
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
    this.filtro = new FuncionalidadeFiltro()
    this.filtro.dataImportacaoDe = this.dataDe;
    this.filtro.dataImportacaoAte = this.dataAte;
    this.filtro.projeto = this.projeto;
    this.filtro.fronteira = this.fronteira;
    this.filtro.tipoRelatorio = this.getTipoRelatorio(this.tipoRelatorioTemp);
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
   * Efetua a paginação da tabela
   */
  irParaPagina = (numeroPagina: number, tamanhoPagina: number): void => {
    this.loaderService.showLoader();
    this.filtro.offset = (numeroPagina - 1) * tamanhoPagina;
    this.filtro.limit = tamanhoPagina;
    this.filtro.situacaoContagem = Situacao[Situacao.CONCLUIDO.toString()];
    this.funcionalidadeService
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
   * Retorna para a página de filtro
   */
  voltar = (): void => {
    this.router.navigate(['/pages', 'relatorio-filtro']);
  }

  exportXLS = () => {
    this.funcionalidadeService.relatorioBaselineXLS(this.filtro).subscribe(
      result => {
        var link = document.createElement("a");
        link.download = "relatorio-baseline.xls";
        link.href = "data:application/vnd.ms-excel;base64," + result;
        document.body.appendChild(link);
        link.click();
      },
      error => { console.log(error) }
    );
  }

  exportPDF = () => {
    this.funcionalidadeService.relatorioBaselinePDF(this.filtro).subscribe(
      result => {
        var link = document.createElement("a");
        link.download = "relatorio-baseline.pdf";
        link.href = "data:application/pdf;base64," + result;
        document.body.appendChild(link);
        link.click();
      },
      error => { console.log(error) }
    );
  }
}
