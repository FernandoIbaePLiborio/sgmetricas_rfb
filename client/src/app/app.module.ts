import { NgModule } from '@angular/core';
import { Paginador } from './model/paginador';
import { BrowserModule } from '@angular/platform-browser';
import { HttpModule } from '@angular/http';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule, NoopAnimationsModule } from '@angular/platform-browser/animations';
import { LocationStrategy, HashLocationStrategy } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';

import { PopoverModule, CollapseModule, TabsModule, AlertModule } from 'ngx-bootstrap';
import { ModalModule, PaginationModule } from 'ngx-bootstrap';
import { BsDropdownModule } from 'ngx-bootstrap';

import { CalendarModule, InputTextModule, InputMaskModule, DropdownModule, DialogModule, ConfirmDialogModule } from 'primeng/primeng';
import { FileUploadModule, DataTableModule, SharedModule, MessagesModule, PaginatorModule } from 'primeng/primeng';
import { DataGridModule, AutoCompleteModule } from 'primeng/primeng';
import { CurrencyMaskModule } from "ng2-currency-mask";

import { ApptemplateComponent } from './apptemplate/apptemplate.component';
import { CabecalhoComponent } from './cabecalho/cabecalho.component';
import { InicioComponent } from './inicio/inicio.component';
import { LoginComponent } from './login/login.component';
import { MenuComponent } from './menu/menu.component';
import { ModalajudaComponent } from './modalajuda/modalajuda.component';
import { PaginadorComponent } from './paginador/paginador.component';
import { ContagemConsultaComponent } from './contagem/contagem-consulta/contagem-consulta.component';
import { ContagemEdicaoComponent } from './contagem/contagem-edicao/contagem-edicao.component';
import { EnderecoConsultaComponent } from './endereco/endereco-consulta/endereco-consulta.component';
import { EnderecoEdicaoComponent } from './endereco/endereco-edicao/endereco-edicao.component';
import { ImportacaoComponent } from './importacao/importacao.component';
import { RelatorioFiltroComponent } from './relatorio/relatorio-filtro/relatorio-filtro.component';
import { RelatorioResultadoComponent } from './relatorio/relatorio-resultado/relatorio-resultado.component';
import { TemplateConsultaComponent } from './template/template-consulta/template-consulta.component';
import { TemplateEdicaoComponent } from './template/template-edicao/template-edicao.component';
import { AppRoutingModule } from './app-routing/app-routing.module';
import { LinkIncluirComponent } from './link-incluir/link-incluir.component';
import { AuthguardGuard } from './services/authguard.guard';
import { SessaoService } from './services/sessao.service';
import { LoaderComponent } from './loader/loader.component';
import { NumeralDirective } from './util/numeral.directive';
import { AppComponent } from './app.component';


@NgModule({
  declarations: [
    AppComponent,
    ApptemplateComponent,
    CabecalhoComponent,
    InicioComponent,
    LoginComponent,
    MenuComponent,
    ModalajudaComponent,
    PaginadorComponent,
    ContagemConsultaComponent,
    ContagemEdicaoComponent,
    EnderecoConsultaComponent,
    EnderecoEdicaoComponent,
    ImportacaoComponent,
    RelatorioFiltroComponent,
    RelatorioResultadoComponent,
    TemplateConsultaComponent,
    TemplateEdicaoComponent,
    LinkIncluirComponent,
    LoaderComponent,
    NumeralDirective
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpModule,
    BrowserAnimationsModule,
    NoopAnimationsModule,
    CalendarModule,
    InputTextModule,
    InputMaskModule,
    DropdownModule,
    FileUploadModule,
    DataTableModule,
    SharedModule,
    PaginatorModule,
    MessagesModule,
    DataGridModule,
    AutoCompleteModule,
    CurrencyMaskModule,
    DialogModule,
    HttpClientModule,
    ConfirmDialogModule,
    PopoverModule.forRoot(),
    CollapseModule.forRoot(),
    TabsModule.forRoot(),
    AlertModule.forRoot(),
    ModalModule.forRoot(),
    PaginationModule.forRoot(),
    BsDropdownModule.forRoot() 
  ],
  providers: [
    { provide: LocationStrategy, useClass: HashLocationStrategy },
    SessaoService,
    AuthguardGuard
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
