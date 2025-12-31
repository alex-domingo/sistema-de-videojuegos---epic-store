import { Component, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { BibliotecaService } from '../../core/services/biblioteca.service';
import { SessionState } from '../../core/state/session.state';
import { ApiResponse } from '../../core/models/api.model';
import type { BibliotecaItem, EstadoInstalacion } from '../../core/models/biblioteca.model';

@Component({
  selector: 'app-biblioteca',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './biblioteca.html',
  styleUrl: './biblioteca.scss'
})
export class Biblioteca {

  private bibliotecaSvc = inject(BibliotecaService);
  private session = inject(SessionState);

  cargando = signal(true);
  error = signal<string | null>(null);
  mensaje = signal<string | null>(null);

  // ⬇️ respuesta completa
  resp = signal<ApiResponse<BibliotecaItem[]> | null>(null);

  // ⬇️ lista cómoda para el template
  items = computed(() => this.resp()?.datos ?? []);

  // ⬇️ estado por videojuego (para deshabilitar botón individual)
  guardando = signal<Record<number, boolean>>({});

  constructor() {
    const s = this.session.session();

    if (!s || s.autenticado !== true || s.tipoSesion !== 'USUARIO') {
      this.cargando.set(false);
      this.error.set('Sesión inválida para biblioteca');
      return;
    }

    this.cargar(s.idUsuario);
  }

  cargar(idUsuario: number) {
    this.cargando.set(true);
    this.error.set(null);
    this.mensaje.set(null);

    this.bibliotecaSvc.listarPorUsuario(idUsuario).subscribe({
      next: (r) => this.resp.set(r),
      error: (e) => {
        console.error(e);
        this.error.set('Error cargando la biblioteca');
      },
      complete: () => this.cargando.set(false)
    });
  }

  // ==========================================================
  // ✅ PASO 3: CAMBIAR ESTADO DE INSTALACIÓN (INSTALAR / DESINSTALAR)
  // ==========================================================
  toggleInstalacion(item: BibliotecaItem) {
    const s = this.session.session();
    if (!s || s.autenticado !== true || s.tipoSesion !== 'USUARIO') return;

    const idUsuario = s.idUsuario;
    const idVideojuego = item.idVideojuego;

    const nuevoEstado: EstadoInstalacion =
      item.estadoInstalacion === 'INSTALADO' ? 'NO_INSTALADO' : 'INSTALADO';

    // marcar solo este juego como "guardando"
    this.guardando.update((m) => ({ ...m, [idVideojuego]: true }));
    this.error.set(null);
    this.mensaje.set(null);

    this.bibliotecaSvc.actualizarEstado({
      idUsuario,
      idVideojuego,
      nuevoEstado
    }).subscribe({
      next: (r) => {
        this.mensaje.set(r.mensaje);

        // 🔁 actualización local (sin recargar todo)
        const current = this.resp();
        if (current) {
          const updated = current.datos.map((x) =>
            x.idVideojuego === idVideojuego
              ? {
                ...x,
                estadoInstalacion: nuevoEstado,
                fechaUltimoCambioEstado: new Date().toISOString()
              }
              : x
          );
          this.resp.set({ ...current, datos: updated });
        }
      },
      error: (e) => {
        console.error(e);
        this.error.set('No se pudo actualizar el estado de instalación');
      },
      complete: () => {
        this.guardando.update((m) => ({ ...m, [idVideojuego]: false }));
      }
    });
  }
}
