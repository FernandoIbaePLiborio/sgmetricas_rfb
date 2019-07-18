import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContagemConsultaComponent } from './contagem-consulta.component';

describe('ContagemConsultaComponent', () => {
  let component: ContagemConsultaComponent;
  let fixture: ComponentFixture<ContagemConsultaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContagemConsultaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContagemConsultaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
