import { Injectable, EventEmitter } from '@angular/core';
import { LoaderComponent } from '../loader/loader.component';

@Injectable()
export class LoaderService {

  eventDisplay = new EventEmitter<any>();
  private display: boolean = false;

  constructor() {
  }

  showLoader() {
    this.display = true;
    this.eventDisplay.emit(true);
  }

  hideLoader() {
    this.display = false;
    this.eventDisplay.emit(false);
  }

}
