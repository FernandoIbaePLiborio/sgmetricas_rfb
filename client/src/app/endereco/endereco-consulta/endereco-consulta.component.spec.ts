import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EnderecoConsultaComponent } from './endereco-consulta.component';

describe('EnderecoConsultaComponent', () => {
  let component: EnderecoConsultaComponent;
  let fixture: ComponentFixture<EnderecoConsultaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EnderecoConsultaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EnderecoConsultaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
