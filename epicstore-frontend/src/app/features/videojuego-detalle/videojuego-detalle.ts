import { Component, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { VideojuegosService } from '../../core/services/videojuegos.service';
import { ApiResponse } from '../../core/models/api.model';
import { VideojuegoDetalle as VideojuegoDetalleModel } from '../../core/models/videojuego.model';
import { SessionState } from '../../core/state/session.state';
import { ComprasService } from '../../core/services/compras.service';
import type { AuthMeResponse } from '../../core/models/auth.model';

@Component({
  selector: 'app-videojuego-detalle',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './videojuego-detalle.html',
  styleUrl: './videojuego-detalle.scss'
})
export class VideojuegoDetalle {
  private route = inject(ActivatedRoute);
  private videojuegosSvc = inject(VideojuegosService);
  public sessionState = inject(SessionState);
  private comprasSvc = inject(ComprasService);

  cargando = signal(true);
  error = signal<string | null>(null);

  resp = signal<ApiResponse<VideojuegoDetalleModel> | null>(null);
  juego = computed(() => this.resp()?.datos ?? null);

  // Signals para la funcionalidad de compra
  comprando = signal(false);
  okCompra = signal<string | null>(null);
  errorCompra = signal<string | null>(null);
  fechaCompra = signal<string>(new Date().toISOString().slice(0, 10)); // YYYY-MM-DD

  puedeComprar = computed(() => this.sessionState.esUsuario() && !!this.juego());

  constructor() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!id || Number.isNaN(id)) {
      this.cargando.set(false);
      this.error.set('ID inválido');
      return;
    }

    this.videojuegosSvc.detallePorId(id).subscribe({
      next: (r) => this.resp.set(r),
      error: (e) => {
        console.error(e);
        this.error.set('Error cargando detalle del videojuego');
      },
      complete: () => this.cargando.set(false)
    });
  }

  // Helper para obtener el idUsuario de la sesión de forma segura
  private getIdUsuarioSesion(): number | null {
    const s = this.sessionState.session() as AuthMeResponse['datos'] | null;
    if (!s || s.autenticado !== true) return null;
    if (s.tipoSesion !== 'USUARIO') return null;
    return s.idUsuario;
  }

  // Método para realizar la compra
  comprar() {
    this.okCompra.set(null);
    this.errorCompra.set(null);

    const s = this.sessionState.session();
    if (!s || s.autenticado !== true || s.tipoSesion !== 'USUARIO') {
      this.errorCompra.set('Debes iniciar sesión como usuario para comprar.');
      return;
    }

    const v = this.juego();
    if (!v) {
      this.errorCompra.set('No se pudo detectar el videojuego.');
      return;
    }

    const fecha = this.fechaCompra().trim();
    if (!fecha) {
      this.errorCompra.set('Debes ingresar la fecha de compra.');
      return;
    }

    this.comprando.set(true);

    this.comprasSvc.comprar({
      idUsuario: s.idUsuario,
      idVideojuego: v.idVideojuego,
      fechaCompra: fecha
    }).subscribe({
      next: (r) => {
        if (!r.exito) {
          this.errorCompra.set(r.mensaje || 'No se pudo realizar la compra.');
          return;
        }
        this.okCompra.set(r.mensaje || 'Compra realizada.');
      },
      error: (e: any) => {
        const msg = e?.error?.mensaje || e?.message || 'Error al realizar la compra';
        this.errorCompra.set(msg);
      },
      complete: () => this.comprando.set(false)
    });
  }
}