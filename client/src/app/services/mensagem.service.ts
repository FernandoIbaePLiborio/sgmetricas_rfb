import { Injectable } from "@angular/core";
import { MessageService } from "primeng/components/common/messageservice";

declare var $: any;

@Injectable()
export class MensagemService {

    private static RESOURCE_PATH: string = "/inicio";

    constructor(private messageService: MessageService) {

    }

    clear = () => {
        this.messageService.clear();
    }

    erroTitulo = (titulo: string, descricao: string) => {
        this.messageService.clear();
        this.messageService.add({ severity: 'error', summary: titulo, detail: descricao });
        this.ajustaIconeErro();
    }

    erro = (descricao: string) => {
        this.messageService.clear();
        this.messageService.add({ severity: 'error', detail: descricao });
        this.ajustaIconeErro();
    }

    erroAll = (descricao: string[]) => {
        this.messageService.clear();
        for (let desc of descricao) {
            this.messageService.add({ severity: 'error', detail: desc });
        }
        this.ajustaIconeErro();
    }

    sucessoTitulo = (titulo: string, descricao: string) => {
        this.messageService.clear();
        this.messageService.add({ severity: 'success', summary: titulo, detail: descricao });
        this.ajustaIconeSucesso();
    }

    sucesso = (descricao: string) => {
        this.messageService.clear();
        this.messageService.add({ severity: 'success', detail: descricao });
        this.ajustaIconeSucesso();
    }

    sucessoAll = (descricao: string[]) => {
        this.messageService.clear();
        for (let desc of descricao) {
            this.messageService.add({ severity: 'success', detail: desc });
        }
        this.ajustaIconeSucesso();
    }

    avisoTitulo = (titulo: string, descricao: string) => {
        this.messageService.clear();
        this.messageService.add({ severity: 'warn', summary: titulo, detail: descricao });
        this.ajustaIconeAviso();
    }

    aviso = (descricao: string) => {
        this.messageService.clear();
        this.messageService.add({ severity: 'warn', detail: descricao });
        this.ajustaIconeAviso();
    }

    avisoAll = (descricao: string[]) => {
        this.messageService.clear();
        for (let desc of descricao) {
            this.messageService.add({ severity: 'warn', detail: desc });
        }
        this.ajustaIconeAviso();
    }

    ajustaIconeSucesso = () => {
        setTimeout(function () {
            $('.fa-check, .ui-messages-icon').html('<i class="material-icons icon-48">check_circle</i>');
            $('.fa-check, .ui-messages-icon').removeClass('fa-check');
            $("html, body").animate({ scrollTop: 0 }, "slow");
        }, 0);
    }

    ajustaIconeErro = () => {
        setTimeout(function () {
            $('.fa-close, .ui-messages-icon').html('<i class="material-icons icon-48">error</i>');
            $('.fa-close, .ui-messages-icon').removeClass('fa-close');
            $("html, body").animate({ scrollTop: 0 }, "slow");
        }, 0);
    }

    ajustaIconeAviso = () => {
        setTimeout(function () {
            $('.fa-warning, .ui-messages-icon').html('<i class="material-icons icon-48">warning</i>');
            $('.fa-warning, .ui-messages-icon').removeClass('fa-warning');
            $("html, body").animate({ scrollTop: 0 }, "slow");
        }, 0);
    }
}
