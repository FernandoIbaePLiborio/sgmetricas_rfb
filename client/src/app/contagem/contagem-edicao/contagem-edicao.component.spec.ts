import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContagemEdicaoComponent } from './contagem-edicao.component';

describe('ContagemEdicaoComponent', () => {
  let component: ContagemEdicaoComponent;
  let fixture: ComponentFixture<ContagemEdicaoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContagemEdicaoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContagemEdicaoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
