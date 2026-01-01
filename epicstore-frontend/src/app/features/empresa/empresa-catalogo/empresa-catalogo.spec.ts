import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmpresaCatalogo } from './empresa-catalogo';

describe('EmpresaCatalogo', () => {
  let component: EmpresaCatalogo;
  let fixture: ComponentFixture<EmpresaCatalogo>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmpresaCatalogo]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmpresaCatalogo);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
