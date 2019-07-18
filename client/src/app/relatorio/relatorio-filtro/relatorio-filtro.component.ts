import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Validators, FormControl, FormGroup, FormBuilder } from '@angular/forms';
import { RestClientService } from '../../services/restclient.service';
import { MensagemService } from '../../services/mensagem.service';
import { SessaoService } from '../../services/sessao.service';
import { MessageService } from 'primeng/components/common/messageservice';
import { AbstractComponent } from '../../util/abstract.component';
import { SelectItem } from 'primeng/components/common/selectitem';
import { TipoRelatorio } from '../../enumeration/tipo-relatorio';
import { FuncionalidadeFiltro } from '../../filtro/funcionalidade.filtro';
import { ContagemService } from '../../services/contagem.service';

@Component({
  selector: 'app-relatorio-filtro',
  templateUrl: './relatorio-filtro.component.html',
  styleUrls: ['./relatorio-filtro.component.css'],
  providers: [
    RestClientService,
    MensagemService,
    SessaoService,
    MessageService,
    ContagemService]
})
export class RelatorioFiltroComponent extends AbstractComponent implements OnInit {

  filtro: FuncionalidadeFiltro;
  tipoRelatorioList: SelectItem[];
  fronteiraList: string[];
  projetoList: string[];
  tipos: any;
  preenchimentoAutoCompletes: boolean;
  preenchimentoDropdown: boolean;

  /**
   * Construtor padrão 
   */
  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private mensagemService: MensagemService,
    private sessaoService: SessaoService,
    private contagemService: ContagemService) {
    super(sessaoService);
    this.tipoRelatorioList = TipoRelatorio.getSelectItem();
  }

  /**
   * Método chamado na inicialização do componente
   */
  ngOnInit() {
    this.filtro = new FuncionalidadeFiltro();
    this.fronteiraList = [];
    this.projetoList = [];
    this.preenchimentoAutoCompletes = true
    this.preenchimentoDropdown = true
  }

  /**
   * Gera a grid de funcionalidades
   */
  emitir = (): void => {
    if (this.filtro.tipoRelatorio == null) {
      this.preenchimentoDropdown = false
      return this.mensagemService.erro('Tipo é um campo obrigatório');
    }
    if (this.filtro.projeto == null && this.filtro.fronteira == null) {
      this.preenchimentoAutoCompletes = false
      return this.mensagemService.erro('Requer que o campo "Fronteira" e/ou "Projeto" seja preenchido');
    }
    sessionStorage.setItem("funcionalidadeFiltro", JSON.stringify(this.filtro));
    this.router.navigate(['/pages', 'relatorio-resultado']);
  };

  buscaFronteira(event) {
    this.contagemService.fronteira(event.query).subscribe(
      result => {
        this.fronteiraList = result;
      },
      error => { console.log(error) }
    );
  }

  buscaProjeto(event) {
    this.contagemService.projeto(event.query).subscribe(
      result => {
        this.projetoList = result;
      },
      error => { console.log(error) }
    );
  }

  validaTipo() {
    this.preenchimentoDropdown = true
  }

  /**
   * Método responsável por validar os dados
   */
  validaAutocomplete() {
    this.filtro.fronteira = this.fronteiraList.find(f => f == this.filtro.fronteira);
    this.filtro.projeto = this.projetoList.find(p => p == this.filtro.projeto);
    if (this.filtro.fronteira === undefined && this.filtro.projeto === undefined) {
      this.preenchimentoAutoCompletes = false
    } else {
      this.preenchimentoAutoCompletes = true
    }
  }
}
