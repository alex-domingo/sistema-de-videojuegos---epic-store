import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VideojuegoDetalle } from './videojuego-detalle';

describe('VideojuegoDetalle', () => {
  let component: VideojuegoDetalle;
  let fixture: ComponentFixture<VideojuegoDetalle>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VideojuegoDetalle]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VideojuegoDetalle);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
