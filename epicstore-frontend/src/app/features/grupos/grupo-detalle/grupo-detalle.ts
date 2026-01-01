import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { SessionState } from '../../../core/state/session.state';
import { GruposService } from '../../../core/services/grupos.service';
import type { MiembroGrupo } from '../../../core/models/grupos.model';

@Component({
  selector: 'app-grupo-detalle',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './grupo-detalle.html',
  styleUrl: './grupo-detalle.scss'
})
export class GrupoDetalle {
  private route = inject(ActivatedRoute);
  private session = inject(SessionState);
  private gruposSvc = inject(GruposService);

  idGrupo = signal<number>(0);

  cargando = signal(true);
  error = signal<string | null>(null);
  ok = signal<string | null>(null);

  miembros = signal<MiembroGrupo[]>([]);

  // agregar
  idUsuarioNuevo = signal<number>(0);
  agregando = signal(false);

  constructor() {
    const id = Number(this.route.snapshot.paramMap.get('idGrupo') ?? 0);
    this.idGrupo.set(id);
    this.cargarMiembros();
  }

  private getIdUsuario(): number | null {
    const s = this.session.session();
    if (!s || s.autenticado !== true || s.tipoSesion !== 'USUARIO') return null;
    return s.idUsuario;
  }

  cargarMiembros() {
    const idGrupo = this.idGrupo();
    if (!idGrupo) {
      this.error.set('idGrupo inválido');
      this.cargando.set(false);
      return;
    }

    this.cargando.set(true);
    this.error.set(null);

    this.gruposSvc.listarMiembros(idGrupo).subscribe({
      next: (r) => {
        if (!r.exito) {
          this.error.set(r.mensaje || 'No se pudieron cargar miembros');
          return;
        }
        this.miembros.set(r.miembros ?? []);
      },
      error: (e: any) => {
        console.error(e);
        this.error.set(e?.error?.mensaje || 'Error cargando miembros');
      },
      complete: () => this.cargando.set(false)
    });
  }

  agregarMiembro() {
    const idDueno = this.getIdUsuario();
    if (!idDueno) {
      this.error.set('Debes iniciar sesión como usuario.');
      return;
    }

    const idGrupo = this.idGrupo();
    const idUsuarioNuevo = Number(this.idUsuarioNuevo());

    if (!idUsuarioNuevo || idUsuarioNuevo <= 0) {
      this.error.set('idUsuarioNuevo inválido');
      return;
    }

    this.agregando.set(true);
    this.error.set(null);
    this.ok.set(null);

    this.gruposSvc.agregarMiembro({ idGrupo, idDueno, idUsuarioNuevo }).subscribe({
      next: (r) => {
        if (!r.exito) {
          this.error.set(r.mensaje || 'No se pudo agregar el miembro');
          return;
        }
        this.ok.set(r.mensaje);
        this.idUsuarioNuevo.set(0);
        this.cargarMiembros();
      },
      error: (e: any) => {
        console.error(e);
        this.error.set(e?.error?.mensaje || 'Error agregando miembro');
      },
      complete: () => this.agregando.set(false)
    });
  }
}
