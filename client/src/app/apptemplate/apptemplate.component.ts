import { Component, OnInit } from '@angular/core';
import { AppComponent } from '../app.component';

declare var $ :any;

@Component({
  selector: 'app-apptemplate',
  templateUrl: './apptemplate.component.html',
  styleUrls: ['./apptemplate.component.css']
})
export class ApptemplateComponent implements OnInit {

  constructor(private appComponent: AppComponent) {
    this.appComponent.cssClass = 'consulta bookmark menu-lateral-ativo';
  }

  ngOnInit() {
    
  }

}
