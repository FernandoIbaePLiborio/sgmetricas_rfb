import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { RestClientService } from '../../services/restclient.service';
import { EnderecoService } from '../../services/endereco.service';
import { ConsultaResult } from '../../util/consultaresult';
import { Endereco } from '../../model/endereco';
import { EnderecoFiltro } from '../../filtro/endereco.filtro';
import { LazyLoadEvent } from 'primeng/components/common/lazyloadevent';
import { Config } from '../../model/config';
import { MessageService } from 'primeng/components/common/messageservice';
import { AbstractComponent } from '../../util/abstract.component';
import { SessaoService } from '../../services/sessao.service';
import { MensagemService } from '../../services/mensagem.service';
import { ErrorMessage } from '../../util/error-message';

declare var $ :any;

@Component({
  selector: 'consulta',
  templateUrl: './endereco-consulta.component.html',
  styleUrls: ['./endereco-consulta.component.css'],
  providers: [
    EnderecoService, 
    RestClientService, 
    MensagemService,
    MessageService, 
    SessaoService]
})
export class EnderecoConsultaComponent extends AbstractComponent implements OnInit {

  result: ConsultaResult<Endereco>;
  filtro: EnderecoFiltro;

  /**
   * Construtor padrão
   */
  constructor(
    private enderecoService: EnderecoService,
    private sessaoService: SessaoService,
    private activatedRoute: ActivatedRoute,
    private mensagemService: MensagemService,
    private router: Router
    ) {
    super(sessaoService);
  }

  /**
   * Método chamado quando o componente é inicializado
   */
  ngOnInit() {
    if(this.activatedRoute.snapshot.params["message_desc"]){
      var mensagem = this.getMensagem(this.activatedRoute.snapshot.params["message_desc"]);
      this.mensagemService.sucessoTitulo('Sucesso', mensagem);
    }
    this.result = new ConsultaResult<Endereco>();
    this.filtro = new EnderecoFiltro();
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
    this.filtro.sortField = event.sortField;
    this.irParaPagina(event.first / event.rows + 1, event.rows);
  }

  /**
   * Método chamado ao clicar no botão limpar
   */
  onClickLimpar(): void {
    this.filtro.numeroProcesso = null;
    this.filtro.cep = null;
    this.filtro.rua = null;
    this.filtro.complemento = null;
  }

  /**
   * Método chamado ao clicar no botão apagar
   */
  onClickDelete = (endereco: Endereco) => {
    this.enderecoService
      .deleteEndereco(endereco)
      .subscribe(
        voidResult => this.recarregarPaginaAtual(),
        error => {} 
      );
  }

  /**
   * Efetua a paginação da tabela
   */
  irParaPagina = (numeroPagina: number, tamanhoPagina: number): void => {
    this.filtro.offset = (numeroPagina - 1) * tamanhoPagina;
    this.filtro.limit = tamanhoPagina;
    this.enderecoService
      .findEnderecos(this.filtro)
      .subscribe(
        result => this.result = result,
        error => {
          let errorMessages: ErrorMessage[] = ((error.json() as Object) as ErrorMessage[]);
          sessionStorage.setItem('message', errorMessages[0].error_description);
          this.router.navigate(['/login']);
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
