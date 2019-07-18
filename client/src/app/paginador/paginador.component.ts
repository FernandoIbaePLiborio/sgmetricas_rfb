import { LazyLoadEvent } from 'primeng/components/common/lazyloadevent';
import { Component, OnInit, forwardRef, Input, ViewChild } from '@angular/core';
import { NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';
import { PaginationComponent } from 'ngx-bootstrap';
import { Paginador } from '../model/paginador';
import { ContagemConsultaComponent } from '../contagem/contagem-consulta/contagem-consulta.component';
import { ItensPorPagina } from '../model/itens-pagina';
import { DataTable } from 'primeng/primeng';
import { ContagemFiltro } from '../filtro/contagem.filtro';

@Component({
  selector: 'app-paginador',
  templateUrl: './paginador.component.html',
  styleUrls: ['./paginador.component.css'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(()=> PaginadorComponent),
      multi: true
    }
  ]
})
export class PaginadorComponent implements OnInit, ControlValueAccessor {

  @Input() options: ItensPorPagina[];

  @ViewChild(DataTable) dataTable: DataTable;

  filtro: ContagemFiltro;

  value: any
  onChange: any

  constructor() { }

  ngOnInit() {
  }

  /**
   * Carrega a página inserida no campo input.
   * @param event 
   */
  setPage(event: number) {
    this.dataTable.reset();
    let paging = {
      first: ((event - 1) * this.dataTable.rows),
      rows: this.dataTable.rows
    };
    this.dataTable.first = paging.first;
    this.dataTable.paginate();
  }

  /**
   * Carrega a quantidade de linhas em dataTable.
   * @param event 
   */
  setRows(event: number) {
    this.dataTable.reset();
    this.dataTable.rows = event;
    this.dataTable.paginate();
  }

  /**
   * Carrega os filtros indicados para ordenação.
   * @param event 
   */
  setSortOrder(event: any) {
    this.filtro.desc = event.order == -1;
    this.filtro.sortField = event.field;
    this.dataTable.paginate();
  }

  /**
   * Write a new value to the element.
   */
  writeValue(obj: any): void{
    this.value = obj
  }
  /**
   * Set the function to be called when the control receives a change event.
   */
  registerOnChange(fn: any): void{
    this.onChange = fn
  }
  /**
   * Set the function to be called when the control receives a touch event.
   */
  registerOnTouched(fn: any): void{}
  /**
   * This function is called when the control status changes to or from "DISABLED".
   * Depending on the value, it will enable or disable the appropriate DOM element.
   *
   * @param isDisabled
   */
  setDisabledState?(isDisabled: boolean): void{}

}
