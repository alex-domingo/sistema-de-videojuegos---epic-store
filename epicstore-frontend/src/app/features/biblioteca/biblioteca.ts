import { Component, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { BibliotecaService } from '../../core/services/biblioteca.service';
import { SessionState } from '../../core/state/session.state';
import { ApiResponse } from '../../core/models/api.model';
import { BibliotecaItem } from '../../core/models/biblioteca.model';

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

  resp = signal<ApiResponse<BibliotecaItem[]> | null>(null);
  items = computed(() => this.resp()?.datos ?? []);

  constructor() {
    // Solo aplica para USUARIO (lo protegeremos con usuarioGuard)
    const s = this.session.session();

    if (!s || s.autenticado !== true || s.tipoSesion !== 'USUARIO') {
      this.cargando.set(false);
      this.error.set('Sesión inválida para biblioteca');
      return;
    }

    const idUsuario = s.idUsuario;
    this.cargar(idUsuario);
  }

  cargar(idUsuario: number) {
    this.cargando.set(true);
    this.error.set(null);

    this.bibliotecaSvc.listarPorUsuario(idUsuario).subscribe({
      next: (r) => this.resp.set(r),
      error: (e) => {
        console.error(e);
        this.error.set('Error cargando la biblioteca');
      },
      complete: () => this.cargando.set(false)
    });
  }
}
