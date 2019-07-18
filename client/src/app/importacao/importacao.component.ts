import { ViewChild, Component, OnInit, ElementRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Validators, FormControl, FormGroup, FormBuilder } from '@angular/forms';
import { Response } from '@angular/http';
import { ImportacaoService } from '../services/importacao.service';
import { RestClientService } from '../services/restclient.service';
import { MensagemService } from '../services/mensagem.service';
import { SessaoService } from '../services/sessao.service';
import { MessageService } from 'primeng/components/common/messageservice';
import { AbstractComponent } from '../util/abstract.component';
import { SelectItem } from 'primeng/components/common/selectitem';
import { Fornecedor } from '../enumeration/fornecedor';
import { TemplateService } from '../services/template.service';
import { Importacao } from '../model/importacao';
import { FileUpload } from 'primeng/primeng';
import { ErrorMessage } from '../util/error-message';
import { SituacaoTemplate } from '../enumeration/situacao-template';
import { LoaderService } from '../services/loader.service';
import { HttpClient } from '@angular/common/http';

declare var $: any;

@Component({
  selector: 'app-importacao',
  templateUrl: './importacao.component.html',
  styleUrls: ['./importacao.component.css'],
  providers: [
    ImportacaoService,
    RestClientService,
    MensagemService,
    TemplateService,
    SessaoService,
    MessageService,
    LoaderService
  ]
})
export class ImportacaoComponent extends AbstractComponent implements OnInit {

  tituloPagina: string;
  importacao: Importacao;
  fileReader: FileReader;
  fornecedorList: SelectItem[];
  templateList: SelectItem[];
  arquivoSelecionado: string;

  /**
   * Construtor padrão
   */
  constructor(
    private importacaoService: ImportacaoService,
    private router: Router,
    private route: ActivatedRoute,
    private mensagemService: MensagemService,
    private sessaoService: SessaoService,
    private templateService: TemplateService,
    private loaderService: LoaderService,
    private http: HttpClient
  ) {
    super(sessaoService);
    this.fornecedorList = Fornecedor.getSelectItem();
    this.templateList = templateService.getTemplateSelectItem(null, SituacaoTemplate.ATIVO);
  }

  /**
   * Método chamado na inicialização do componente
   */
  ngOnInit() {
    this.importacao = new Importacao();
    this.fileReader = new FileReader();
    this.tituloPagina = "Importar Planilha";
  }

  onFileSelected(event: any) {
    var target = event.target || event.srcElement; //if target isn't there then take srcElement
    let files = target.files;
    this.fileReader.readAsDataURL(files[0]);
    for (let file of files) {
      this.arquivoSelecionado = file.name;
      if (file.type != "application/vnd.oasis.opendocument.spreadsheet"){
        this.limpar();
        return this.mensagemService.erro('Aceita somente arquivos do tipo .ODS');
      }
    }
  }

  /**
   *
   */
  importarArquivo = (): void => {
    this.mensagemService.clear();
    this.importacao.arquivoBase64 = this.fileReader.result;
    if (!this.validacaoBasica(this.importacao)) {
      return;
    }
    this.loaderService.showLoader();
    this.importacaoService.importarPlanilha(this.importacao).subscribe(
      (result: Importacao) => {
        this.limpar()
        this.loaderService.hideLoader()
        this.mensagemService.sucessoTitulo('Sucesso', 'Importação realizada com sucesso.');
      },
      (error: Response) => {
        let errorMessages: ErrorMessage[] = ((error.json() as Object) as ErrorMessage[]);
        this.mensagemService.erroTitulo("Erro", errorMessages[0].error_description);
        this.loaderService.hideLoader();
      }
    );
  };

  /**
   * Realiza a validação básica do formulário
   */
  validacaoBasica = (importacao: Importacao): boolean => {
    var isValid = true;
    var descricao: string[] = [];
    this.removerValidacaoBasica();
    if (importacao.arquivoBase64 == null || importacao.arquivoBase64.trim() == "") {
      descricao.push("O arquivo é obrigatório.");
      $('#div_numeroProcesso').addClass('has-error');
      isValid = false;
    }
    if (importacao.idTemplate == null) {
      descricao.push("O template é obrigatório.");
      $('#div_cep').addClass('has-error');
      isValid = false;
    }
    if (importacao.fornecedor == null) {
      descricao.push("O fornecedor é obrigatório.");
      $('#div_cep').addClass('has-error');
      isValid = false;
    }
    if (importacao.numeroDemanda == null || importacao.numeroDemanda.trim() == "") {
      descricao.push("O número da demanda é obrigatório.");
      $('#div_cep').addClass('has-error');
      isValid = false;
    }
    if (importacao.data == null) {
      descricao.push("A data é obrigatória.");
      $('#div_cep').addClass('has-error');
      isValid = false;
    }

    if (!isValid) {
      this.mensagemService.erroAll(descricao);
    }

    return isValid;
  }

  limpar() {
    this.importacao = new Importacao()
    this.fileReader = new FileReader()
    this.mensagemService.clear()
    this.arquivoSelecionado = null;
  }

  /**
   * Remove a validação do formulário
   */
  removerValidacaoBasica = (): void => {
    $('#div_numeroProcesso').removeClass('has-error');
    $('#div_cep').removeClass('has-error');
  }
}
