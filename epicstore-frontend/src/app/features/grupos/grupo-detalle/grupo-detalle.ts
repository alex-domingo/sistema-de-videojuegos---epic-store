import { Component, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { SessionState } from '../../../core/state/session.state';
import { GruposService } from '../../../core/services/grupos.service';
import { PrestamosService } from '../../../core/services/prestamos.service';
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
  private prestamosSvc = inject(PrestamosService);

  idGrupo = signal<number>(0);

  cargando = signal(true);
  error = signal<string | null>(null);
  ok = signal<string | null>(null);

  miembros = signal<MiembroGrupo[]>([]);

  // agregar
  idUsuarioNuevo = signal<number>(0);
  agregando = signal(false);

  // ---- PRESTAR ----
  idReceptor = signal<number>(0);
  idVideojuego = signal<number>(0);

  prestando = signal(false);
  okPrestamo = signal<string | null>(null);
  errorPrestamo = signal<string | null>(null);

  private idUsuarioSesion = computed(() => {
    const s = this.session.session();
    if (!s || s.autenticado !== true || s.tipoSesion !== 'USUARIO') return null;
    return s.idUsuario;
  });

  esDueno = computed(() => {
    const id = this.idUsuarioSesion();
    if (!id) return false;
    return this.miembros().some(m => m.idUsuario === id && m.rol === 'DUENIO');
  });

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

  prestar() {
    this.okPrestamo.set(null);
    this.errorPrestamo.set(null);

    const idDueno = this.idUsuarioSesion();
    if (!idDueno) {
      this.errorPrestamo.set('Debes iniciar sesión como usuario.');
      return;
    }

    const idGrupo = this.idGrupo();
    const idReceptor = Number(this.idReceptor());
    const idVideojuego = Number(this.idVideojuego());

    if (!idGrupo) return this.errorPrestamo.set('idGrupo inválido');
    if (!idReceptor || idReceptor <= 0) return this.errorPrestamo.set('ID receptor inválido');
    if (!idVideojuego || idVideojuego <= 0) return this.errorPrestamo.set('ID videojuego inválido');

    this.prestando.set(true);

    this.prestamosSvc.prestar({ idGrupo, idDueno, idReceptor, idVideojuego }).subscribe({
      next: (r) => {
        if (!r.exito) {
          this.errorPrestamo.set(r.mensaje || 'No se pudo realizar el préstamo');
          return;
        }
        this.okPrestamo.set(r.mensaje || 'Préstamo realizado correctamente');

        // Limpieza del form
        this.idReceptor.set(0);
        this.idVideojuego.set(0);
      },
      error: (e: any) => {
        const msg = e?.error?.mensaje || e?.message || 'Error realizando préstamo';
        this.errorPrestamo.set(msg);
      },
      complete: () => this.prestando.set(false)
    });
  }
}