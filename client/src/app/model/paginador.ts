import { Headers } from '@angular/http';

export class Paginador {

    numerosPaginasPaginador: number[];
    hasPreviousPage: boolean;
    hasNextPage: boolean;
    numeroPaginaAtual: number;
    numeroTotalPaginas: number;
    numeroTotalItens: number;
    numeroItensPorPagina: number;

    constructor(private tamanhoPagina: number, contentRangeHeader?: string) {
        if (contentRangeHeader) {
            this.numeroTotalItens = +contentRangeHeader.substring(contentRangeHeader.lastIndexOf("/") + 1);
            let numeroOffsetAtual = +contentRangeHeader.substring(0, contentRangeHeader.indexOf("-"));

            this.numeroItensPorPagina = tamanhoPagina;
            this.numeroPaginaAtual = Math.ceil((numeroOffsetAtual + 1) / this.numeroItensPorPagina);
            this.numeroTotalPaginas = Math.ceil(this.numeroTotalItens / this.numeroItensPorPagina);

            let numeroPrimeiraPaginaPaginador: number;
            let numeroUltimaPaginaPaginador: number;
            if (this.numeroTotalPaginas <= 5) {
                numeroPrimeiraPaginaPaginador = 1;
                numeroUltimaPaginaPaginador = this.numeroTotalPaginas;
            } else if (this.numeroPaginaAtual > 2 && this.numeroPaginaAtual < (this.numeroTotalPaginas - 2)) {
                numeroPrimeiraPaginaPaginador = this.numeroPaginaAtual - 2;
                numeroUltimaPaginaPaginador = this.numeroPaginaAtual + 2;
            } else if (this.numeroPaginaAtual < 3) {
                numeroPrimeiraPaginaPaginador = 1;
                numeroUltimaPaginaPaginador = 5;
            } else {
                numeroPrimeiraPaginaPaginador = this.numeroTotalPaginas - 4;
                numeroUltimaPaginaPaginador = this.numeroTotalPaginas;
            }
            this.numerosPaginasPaginador = [];
            for (var i = numeroPrimeiraPaginaPaginador; i <= numeroUltimaPaginaPaginador; i++) {
                this.numerosPaginasPaginador.push(i)
            }

            this.hasPreviousPage = (this.numeroPaginaAtual > 1);
            this.hasNextPage = (this.numeroPaginaAtual < this.numeroTotalPaginas);
        }
    }
}
