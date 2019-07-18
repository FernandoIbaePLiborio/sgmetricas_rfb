import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'linkincluir',
  templateUrl: './link-incluir.component.html',
  styleUrls: ['./link-incluir.component.css']
})
export class LinkIncluirComponent implements OnInit {

  @Input() 
  titulo: string;
  
  @Input() 
  caminho: string[];

  constructor() { }

  ngOnInit() {
  }

}
