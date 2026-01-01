import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';

import { EmpresaCatalogoService } from '../../../core/services/empresa-catalogo.service';
import type { EmpresaVideojuegoItem, SN } from '../../../core/models/empresa-catalogo.model';

@Component({
  selector: 'app-empresa-catalogo',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './empresa-catalogo.html',
  styleUrl: './empresa-catalogo.scss'
})
export class EmpresaCatalogo {
  private svc = inject(EmpresaCatalogoService);

  cargando = signal(true);
  error = signal<string | null>(null);
  ok = signal<string | null>(null);

  items = signal<EmpresaVideojuegoItem[]>([]);
  togglingId = signal<number | null>(null);

  constructor() {
    this.cargar();
  }

  cargar() {
    this.cargando.set(true);
    this.error.set(null);
    this.ok.set(null);

    this.svc.listar().subscribe({
      next: (r) => {
        if (!r.exito) {
          this.error.set(r.mensaje || 'No se pudo cargar el catálogo');
          this.items.set([]);
          return;
        }
        this.items.set(r.datos ?? []);
      },
      error: (e: any) => {
        console.error(e);
        this.error.set(e?.error?.mensaje || 'Error cargando catálogo');
      },
      complete: () => this.cargando.set(false)
    });
  }

  toggleVenta(v: EmpresaVideojuegoItem) {
    const nuevo: SN = v.ventaActiva === 'S' ? 'N' : 'S';
    this.togglingId.set(v.idVideojuego);
    this.ok.set(null);
    this.error.set(null);

    this.svc.setVenta(v.idVideojuego, nuevo).subscribe({
      next: (r) => {
        if (!r.exito) {
          this.error.set(r.mensaje || 'No se pudo actualizar la venta');
          return;
        }
        // update local (sin recargar)
        this.items.set(
          this.items().map(x => x.idVideojuego === v.idVideojuego ? { ...x, ventaActiva: nuevo } : x)
        );
        this.ok.set(r.mensaje || 'Venta actualizada');
      },
      error: (e: any) => {
        console.error(e);
        this.error.set(e?.error?.mensaje || 'Error actualizando venta');
      },
      complete: () => this.togglingId.set(null)
    });
  }
}
