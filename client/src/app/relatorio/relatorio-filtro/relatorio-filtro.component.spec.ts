import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RelatorioFiltroComponent } from './relatorio-filtro.component';

describe('RelatorioFiltroComponent', () => {
  let component: RelatorioFiltroComponent;
  let fixture: ComponentFixture<RelatorioFiltroComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RelatorioFiltroComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RelatorioFiltroComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
