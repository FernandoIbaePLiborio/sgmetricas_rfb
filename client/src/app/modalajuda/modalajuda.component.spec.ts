import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalajudaComponent } from './modalajuda.component';

describe('ModalajudaComponent', () => {
  let component: ModalajudaComponent;
  let fixture: ComponentFixture<ModalajudaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModalajudaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalajudaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
