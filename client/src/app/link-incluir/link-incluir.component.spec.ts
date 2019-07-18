import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LinkIncluirComponent } from './link-incluir.component';

describe('LinkIncluirComponent', () => {
  let component: LinkIncluirComponent;
  let fixture: ComponentFixture<LinkIncluirComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LinkIncluirComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LinkIncluirComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
