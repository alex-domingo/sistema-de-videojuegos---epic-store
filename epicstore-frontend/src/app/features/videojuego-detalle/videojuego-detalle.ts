import { Component, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { VideojuegosService } from '../../core/services/videojuegos.service';
import { ApiResponse } from '../../core/models/api.model';
import { VideojuegoDetalle as VideojuegoDetalleModel } from '../../core/models/videojuego.model';

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

  cargando = signal(true);
  error = signal<string | null>(null);

  resp = signal<ApiResponse<VideojuegoDetalleModel> | null>(null);
  juego = computed(() => this.resp()?.datos ?? null);

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
}
