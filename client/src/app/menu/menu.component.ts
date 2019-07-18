import { Component, OnInit, Input } from '@angular/core';
import { AbstractComponent } from '../util/abstract.component';
import { SessaoService } from '../services/sessao.service';

declare var $ :any;

@Component({
  selector: 'menucomponent',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css'],
  providers: [SessaoService]
})
export class MenuComponent extends AbstractComponent implements OnInit {

  @Input() 
  breadCrumb: string;
  
  @Input() 
  tituloFuncionalidadeAtual: string;  
  
  constructor(private sessaoService: SessaoService) {
    super(sessaoService);
  }

  ngOnInit() {
  }

  hideMenu = () => {
    if(this.sessaoService.sessao.isShowMenu){
      $('.sidebar').addClass('hide-div');
      $('.menu-lateral-ativo article .container').addClass('full-div');
      this.sessaoService.sessao.isShowMenu = false;
    }
    else {
      $('.sidebar').removeClass('hide-div');
      $('.menu-lateral-ativo article .container').removeClass('full-div');
      this.sessaoService.sessao.isShowMenu = true;
    }
  }
}
