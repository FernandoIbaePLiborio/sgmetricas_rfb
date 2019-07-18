import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EnderecoEdicaoComponent } from './endereco-edicao.component';

describe('EnderecoEdicaoComponent', () => {
  let component: EnderecoEdicaoComponent;
  let fixture: ComponentFixture<EnderecoEdicaoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EnderecoEdicaoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EnderecoEdicaoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
