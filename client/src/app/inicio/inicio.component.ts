import { Component, OnInit } from '@angular/core';
import { InicioService } from '../services/inicio.service';
import { RestClientService } from '../services/restclient.service';

@Component({
  selector: 'inicio',
  templateUrl: './inicio.component.html',
  styleUrls: ['./inicio.component.css'],
  providers: [InicioService, RestClientService]
})
export class InicioComponent implements OnInit {

  constructor(private InicioService: InicioService) { }

  ngOnInit() {
  }

}
