import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmpresaPerfil } from './empresa-perfil';

describe('EmpresaPerfil', () => {
  let component: EmpresaPerfil;
  let fixture: ComponentFixture<EmpresaPerfil>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmpresaPerfil]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmpresaPerfil);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
