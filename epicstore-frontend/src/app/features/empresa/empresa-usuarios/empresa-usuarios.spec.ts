import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmpresaUsuarios } from './empresa-usuarios';

describe('EmpresaUsuarios', () => {
  let component: EmpresaUsuarios;
  let fixture: ComponentFixture<EmpresaUsuarios>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmpresaUsuarios]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmpresaUsuarios);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
