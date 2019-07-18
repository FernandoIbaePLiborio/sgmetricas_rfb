import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent } from '../login/login.component';
import { ApptemplateComponent } from '../apptemplate/apptemplate.component';
import { InicioComponent } from '../inicio/inicio.component';
import { ImportacaoComponent } from '../importacao/importacao.component';
import { EnderecoConsultaComponent } from '../endereco/endereco-consulta/endereco-consulta.component';
import { ContagemConsultaComponent } from '../contagem/contagem-consulta/contagem-consulta.component';
import { TemplateConsultaComponent } from '../template/template-consulta/template-consulta.component';
import { RelatorioFiltroComponent } from '../relatorio/relatorio-filtro/relatorio-filtro.component';
import { EnderecoEdicaoComponent } from '../endereco/endereco-edicao/endereco-edicao.component';
import { TemplateEdicaoComponent } from '../template/template-edicao/template-edicao.component';
import { ContagemEdicaoComponent } from '../contagem/contagem-edicao/contagem-edicao.component';
import { RelatorioResultadoComponent } from '../relatorio/relatorio-resultado/relatorio-resultado.component';
import { AuthguardGuard } from '../services/authguard.guard';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login',  component: LoginComponent },
  { path: 'pages', component: ApptemplateComponent,
    children: [
      {
        path: '',
        children: [
          { path: 'inicio', component: InicioComponent, canActivate: [AuthguardGuard] },
          { path: 'endereco-consulta', component: EnderecoConsultaComponent, canActivate: [AuthguardGuard] },
          { path: 'endereco-edicao', component: EnderecoEdicaoComponent, canActivate: [AuthguardGuard] },
          { path: 'template-consulta', component: TemplateConsultaComponent, canActivate: [AuthguardGuard] },
          { path: 'template-edicao', component: TemplateEdicaoComponent, canActivate: [AuthguardGuard] },
          { path: 'contagem-consulta', component: ContagemConsultaComponent, canActivate: [AuthguardGuard] },
          { path: 'contagem-edicao', component: ContagemEdicaoComponent, canActivate: [AuthguardGuard] },
          { path: 'relatorio-filtro', component: RelatorioFiltroComponent, canActivate: [AuthguardGuard] },
          { path: 'relatorio-resultado', component: RelatorioResultadoComponent, canActivate: [AuthguardGuard] },
          { path: 'importacao', component: ImportacaoComponent, canActivate: [AuthguardGuard] },
        ]
      }
    ]
  }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ],
  providers: []
})

export class AppRoutingModule {}
