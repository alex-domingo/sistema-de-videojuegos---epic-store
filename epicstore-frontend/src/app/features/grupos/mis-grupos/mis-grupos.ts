import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { SessionState } from '../../../core/state/session.state';
import { GruposService } from '../../../core/services/grupos.service';
import type { GrupoItem } from '../../../core/models/grupos.model';

@Component({
  selector: 'app-mis-grupos',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './mis-grupos.html',
  styleUrl: './mis-grupos.scss'
})
export class MisGrupos {
  private session = inject(SessionState);
  private gruposSvc = inject(GruposService);

  cargando = signal(true);
  error = signal<string | null>(null);
  ok = signal<string | null>(null);

  grupos = signal<GrupoItem[]>([]);

  // crear
  nombreGrupo = signal('');
  creando = signal(false);

  constructor() {
    this.cargar();
  }

  private getIdUsuario(): number | null {
    const s = this.session.session();
    if (!s || s.autenticado !== true || s.tipoSesion !== 'USUARIO') return null;
    return s.idUsuario;
  }

  cargar() {
    const idUsuario = this.getIdUsuario();
    if (!idUsuario) {
      this.cargando.set(false);
      this.error.set('Debes iniciar sesión como usuario.');
      return;
    }

    this.cargando.set(true);
    this.error.set(null);

    this.gruposSvc.listarGrupos(idUsuario).subscribe({
      next: (r) => {
        if (!r.exito) {
          this.error.set(r.mensaje || 'No se pudieron cargar los grupos');
          return;
        }
        this.grupos.set(r.datos ?? []);
      },
      error: (e) => {
        console.error(e);
        this.error.set('Error cargando grupos');
      },
      complete: () => this.cargando.set(false)
    });
  }

  crear() {
    const idUsuario = this.getIdUsuario();
    if (!idUsuario) return;

    const nombre = this.nombreGrupo().trim();
    if (!nombre) {
      this.error.set('Nombre de grupo requerido');
      return;
    }

    this.creando.set(true);
    this.ok.set(null);
    this.error.set(null);

    this.gruposSvc.crearGrupo({ idDueno: idUsuario, nombreGrupo: nombre }).subscribe({
      next: (r) => {
        if (!r.exito) {
          this.error.set(r.mensaje || 'No se pudo crear el grupo');
          return;
        }
        this.ok.set(r.mensaje);
        this.nombreGrupo.set('');
        this.cargar();
      },
      error: (e: any) => {
        console.error(e);
        this.error.set(e?.error?.mensaje || 'Error creando grupo');
      },
      complete: () => this.creando.set(false)
    });
  }
}
