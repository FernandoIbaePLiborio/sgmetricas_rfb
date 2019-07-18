import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TemplateEdicaoComponent } from './template-edicao.component';

describe('TemplateEdicaoComponent', () => {
  let component: TemplateEdicaoComponent;
  let fixture: ComponentFixture<TemplateEdicaoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TemplateEdicaoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TemplateEdicaoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
