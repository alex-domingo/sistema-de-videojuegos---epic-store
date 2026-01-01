import { Component, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { SessionState } from '../../core/state/session.state';
import { CarteraService } from '../../core/services/cartera.service';
import type { MovimientoCartera } from '../../core/models/cartera.model';

@Component({
  selector: 'app-cartera',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './cartera.html',
  styleUrl: './cartera.scss'
})
export class Cartera {
  private session = inject(SessionState);
  private carteraSvc = inject(CarteraService);

  cargando = signal(true);
  error = signal<string | null>(null);
  ok = signal<string | null>(null);

  saldoActual = signal<number>(0);
  movimientos = signal<MovimientoCartera[]>([]);

  // form recarga
  monto = signal<number>(0);
  descripcion = signal<string>('Recarga mediante depósito');
  recargando = signal(false);

  totalMovimientos = computed(() => this.movimientos().length);

  constructor() {
    const s = this.session.session();
    if (!s || s.autenticado !== true || s.tipoSesion !== 'USUARIO') {
      this.cargando.set(false);
      this.error.set('Sesión inválida para cartera');
      return;
    }
    this.cargarMovimientos(s.idUsuario);
  }

  cargarMovimientos(idUsuario: number) {
    this.cargando.set(true);
    this.error.set(null);

    this.carteraSvc.movimientos(idUsuario).subscribe({
      next: (r) => {
        if (!r.exito) {
          this.error.set(r.mensaje || 'No se pudo cargar la cartera');
          return;
        }
        this.saldoActual.set(r.saldoActual ?? 0);
        this.movimientos.set(r.movimientos ?? []);
      },
      error: (e) => {
        console.error(e);
        this.error.set('Error cargando movimientos');
      },
      complete: () => this.cargando.set(false)
    });
  }

  recargar() {
    const s = this.session.session();
    if (!s || s.autenticado !== true || s.tipoSesion !== 'USUARIO') return;

    this.ok.set(null);
    this.error.set(null);

    const monto = Number(this.monto());
    if (!Number.isFinite(monto) || monto <= 0) {
      this.error.set('Monto inválido para recarga');
      return;
    }

    this.recargando.set(true);

    this.carteraSvc.recargar({
      idUsuario: s.idUsuario,
      monto,
      descripcion: this.descripcion().trim() || 'Recarga'
    }).subscribe({
      next: (r) => {
        if (!r.exito) {
          this.error.set(r.mensaje || 'No se pudo realizar la recarga');
          return;
        }
        this.ok.set(r.mensaje);

        // Actualización rápida del saldo (si viene)
        if (typeof r.saldoNuevo === 'number') this.saldoActual.set(r.saldoNuevo);

        // Fuente de verdad: recargar movimientos desde backend
        this.cargarMovimientos(s.idUsuario);

        // reset form
        this.monto.set(0);
      },
      error: (e) => {
        console.error(e);
        const msg = e?.error?.mensaje || 'Error realizando recarga';
        this.error.set(msg);
      },
      complete: () => this.recargando.set(false)
    });
  }
}
