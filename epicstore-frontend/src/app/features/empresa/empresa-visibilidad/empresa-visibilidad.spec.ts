import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmpresaVisibilidad } from './empresa-visibilidad';

describe('EmpresaVisibilidad', () => {
  let component: EmpresaVisibilidad;
  let fixture: ComponentFixture<EmpresaVisibilidad>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmpresaVisibilidad]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmpresaVisibilidad);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
