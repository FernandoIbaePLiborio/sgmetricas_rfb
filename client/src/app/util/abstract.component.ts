import { TipoFuncionalidade } from './../enumeration/tipo-funcionalidade';
import { TipoRelatorio } from './../enumeration/tipo-relatorio';
import * as msgResource from '../../assets/resources/mensagem.json';
import * as configResource from '../../assets/resources/config.json';
import { Component, OnInit } from '@angular/core';
import { Sessao } from "../model/sessao";
import { SessaoService } from "../services/sessao.service";
import { ActivatedRoute, Router, Params } from '@angular/router';
import { MessageService } from "primeng/components/common/messageservice";
import { SituacaoTemplate } from '../enumeration/situacao-template';
import { Situacao } from '../enumeration/situacao';
import { Fornecedor } from '../enumeration/fornecedor';
import { Classificacao } from '../enumeration/classificacao';
import { Complexidade } from '../enumeration/complexidade';
import { TipoContagem } from '../enumeration/tipo-contagem';
import { TipoDemanda } from '../enumeration/tipo-demanda';
import { TipoProjeto } from '../enumeration/tipo-projeto';
import { Mensagem } from '../model/mensagem';
import { Configuracao } from '../model/configuracao';

declare var $: any;

export class AbstractComponent implements OnInit {

    sessao: Sessao;
    isCollapsed: boolean;
    pt: any;
    loading: boolean = false;
    dataTableVisible: boolean = true;

    constructor(sessaoService: SessaoService) {
        this.sessao = sessaoService.sessao;
        this.pt = {
            firstDayOfWeek: 0,
            dayNames: ["Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"],
            dayNamesShort: ["Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sab"],
            dayNamesMin: ["Do", "Se", "Te", "Qu", "Qu", "Se", "Sa"],
            monthNames: ["Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"],
            monthNamesShort: ["Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"],
            today: 'Today',
            clear: 'Clear'
        };
        setTimeout(() => {
            $('.fa-calendar').addClass('fa-calendar-o fa-lg');
        }, 0);
    }

    ngOnInit() {

    }

    getMensagem = (chave: string): string => {
        var mensagens: Mensagem[] = <any>msgResource;
        return mensagens.find(w => w.chave == chave).texto;
    }

    getConfiguracao = (chave: string): string => {
        var configuracoes: Configuracao[] = <any>configResource;
        return configuracoes.find(w => w.chave == chave).valor;
    }

    getSituacaoTemplateDescricao = (situacaoTemplate: SituacaoTemplate): string => {
        return SituacaoTemplate[situacaoTemplate];
    }

    getSituacaoDescricao = (situacao: Situacao): string => {
        return Situacao[situacao];
    }

    getFornecedorDescricao = (fornecedor: Fornecedor): string => {
        return Fornecedor[fornecedor];
    }

    getClassificacaoDescricao = (classificacao: Classificacao): string => {
        return Classificacao[classificacao];
    }

    getComplexidadeDescricao = (complexidade: Complexidade): string => {
        return Complexidade[complexidade];
    }

    getTipoContagemDescricao = (tipoContagem: TipoContagem): string => {
        return TipoContagem[tipoContagem];
    }

    getTipoProjetoDescricao = (tipoProjeto: TipoProjeto): string => {
        return TipoProjeto[tipoProjeto];
    }

    getTipoDemandaDescricao = (tipoDemanda: TipoDemanda): string => {
        return TipoDemanda[tipoDemanda];
    }

    getTipoFuncionalidadeDescricao = (tipoFuncionalidade: TipoFuncionalidade): string => {
        return TipoFuncionalidade[tipoFuncionalidade];
    }

    getTipoRelatorioDescricao = (tipoRelatorio: TipoRelatorio): string => {
        return TipoRelatorio[tipoRelatorio];
    }

    getFormatarDecimal(numero: number) {
        return new Intl.NumberFormat('pt-BR').format(numero);
    }

    getTipoRelatorio = (tipo: string): TipoRelatorio => {
        if (tipo == TipoRelatorio.TODAS.toString()) {
            return TipoRelatorio.TODAS;
        } else {
            return TipoRelatorio.BASELINE;
        }
    }

}
