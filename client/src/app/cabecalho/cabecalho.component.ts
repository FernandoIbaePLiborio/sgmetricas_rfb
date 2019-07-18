import { Component, OnInit } from '@angular/core';
import { AbstractComponent } from '../util/abstract.component';
import { SessaoService } from '../services/sessao.service';
import { Router } from '@angular/router';

declare var $ :any;

@Component({
  selector: 'cabecalho',
  templateUrl: './cabecalho.component.html',
  styleUrls: ['./cabecalho.component.css'],
  providers: [SessaoService]
})
export class CabecalhoComponent extends AbstractComponent implements OnInit {

  constructor(
    private router: Router,
    private sessaoService: SessaoService) {
    super(sessaoService);
  }

  ngOnInit() {
    
  }

  contraste() {
    $('body').toggleClass('contraste');
  }

  perfilList = (): string => {
    return this.sessaoService.sessao.usuario.perfis.map(w=>w.descricao).join(", ");
  }

  logout = () => {
    sessionStorage.clear();
    this.router.navigate(['/login']);
  }

  modalajuda(){
    
  }
}
