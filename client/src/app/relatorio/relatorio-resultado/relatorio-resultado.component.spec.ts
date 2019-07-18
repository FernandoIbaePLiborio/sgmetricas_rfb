import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RelatorioResultadoComponent } from './relatorio-resultado.component';

describe('RelatorioResultadoComponent', () => {
  let component: RelatorioResultadoComponent;
  let fixture: ComponentFixture<RelatorioResultadoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RelatorioResultadoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RelatorioResultadoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
