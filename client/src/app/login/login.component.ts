import { Component, OnInit } from '@angular/core';
import { Credentials } from '../model/credentials';
import { AppComponent } from '../app.component';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { RestClientService } from '../services/restclient.service';
import { ErrorMessage } from '../util/error-message';
import { MensagemService } from '../services/mensagem.service';
import { MessageService } from 'primeng/components/common/messageservice';
import { Usuario } from '../model/usuario';
import { SessaoService } from '../services/sessao.service';
import { Token } from '../model/token';

@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  providers: [
    AuthService, 
    RestClientService, 
    MensagemService, 
    MessageService,
    SessaoService
    ]
})
export class LoginComponent implements OnInit {

  credentials: Credentials;

  constructor(
    private appComponent: AppComponent, 
    private router: Router, 
    private authService: AuthService,
    private mensagemService: MensagemService,
    private sessaoService: SessaoService) {
      this.appComponent.cssClass = 'login';
    }

  ngOnInit() {
    this.credentials = new Credentials();
    this.credentials.login = '00474718166'
    if(sessionStorage.getItem('message')){
      var mensagem = sessionStorage.getItem('message');
      this.mensagemService.erro(mensagem);
    }
    sessionStorage.clear();
    this.getUserCertificado();
  }

  onClickSubmit = (): void => {
    this.getUser();
  }

  getUser = () : Usuario => {
    this.authService.login(this.credentials).subscribe(
      (result: Usuario) => {
        this.getToken(result);
      },
      (error: Response) => {
        let errorMessages: ErrorMessage[] = ((error.json() as Object) as ErrorMessage[]);
        this.mensagemService.erro(ErrorMessage.descricao(errorMessages[0].error));
      }
    );

    return null;
  }

  getToken = (usuario: Usuario) => {
    this.authService.token(this.credentials).subscribe(
      (result: Token) => {
        this.sessaoService.sessao.usuario = usuario;
        this.sessaoService.sessao.token = result;
        this.sessaoService.sessao.logado = true;
        sessionStorage.clear();
        sessionStorage.setItem("sessaoGMETRICA", JSON.stringify(this.sessaoService.sessao));
        this.irParaTelaInicial();
      },
      (error: Response) => {
        let errorMessages: ErrorMessage[] = ((error.json() as Object) as ErrorMessage[]);
        this.mensagemService.erro(ErrorMessage.descricao(errorMessages[0].error));
      }
    );

    return null;
  }

  getUserCertificado = () : Usuario => {
    this.authService.loginCertificado(this.credentials).subscribe(
      (result: Usuario) => {
        this.getTokenCertificado(result);
      },
      (error: Response) => {
        let errorMessages: ErrorMessage[] = ((error.json() as Object) as ErrorMessage[]);
        this.mensagemService.erro(ErrorMessage.descricao(errorMessages[0].error));
      }
    );

    return null;
  }

  getTokenCertificado = (usuario: Usuario) => {
    this.authService.tokenCertificado(new Credentials()).subscribe(
      (result: Token) => {
        this.sessaoService.sessao.usuario = usuario;
        this.sessaoService.sessao.token = result;
        this.sessaoService.sessao.logado = true;
        sessionStorage.clear();
        sessionStorage.setItem("sessaoGMETRICA", JSON.stringify(this.sessaoService.sessao));
        this.irParaTelaInicial();
      },
      (error: Response) => {
        console.log(error);
        let errorMessages: ErrorMessage[] = ((error.json() as Object) as ErrorMessage[]);
        this.mensagemService.erro(ErrorMessage.descricao(errorMessages[0].error));
      }
    );

    return null;
  }

  irParaTelaInicial(): void {
    this.router.navigate(['/pages', 'inicio']);
  }

}
