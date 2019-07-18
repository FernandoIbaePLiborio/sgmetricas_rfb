import { Component, OnInit, Input } from '@angular/core';

import { EventEmitter } from "@angular/core";
import { LoaderService } from '../services/loader.service';
import { Router } from "@angular/router";

@Component({
  selector: 'loader',
  templateUrl: './loader.component.html',
  styleUrls: ['./loader.component.css']
})
export class LoaderComponent implements OnInit {

  display:boolean = false;

  constructor(
    private loaderService: LoaderService
  ) {
}

ngOnInit() {
  this.loaderService.eventDisplay.subscribe(
    event => this.display = event
  );
}

setDisplay(val: boolean) {
  this.display = val;
}

}
