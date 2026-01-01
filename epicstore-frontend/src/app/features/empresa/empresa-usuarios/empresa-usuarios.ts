import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { EmpresaUsuariosService } from '../../../core/services/empresa-usuarios.service';
import type { UsuarioEmpresaItem } from '../../../core/models/empresa-usuarios.model';

@Component({
  selector: 'app-empresa-usuarios',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './empresa-usuarios.html',
  styleUrl: './empresa-usuarios.scss'
})
export class EmpresaUsuarios {
  private svc = inject(EmpresaUsuariosService);

  cargando = signal(true);
  creando = signal(false);
  eliminandoId = signal<number | null>(null);

  usuarios = signal<UsuarioEmpresaItem[]>([]);

  ok = signal<string | null>(null);
  error = signal<string | null>(null);

  // form crear usuario empresa
  nombre = signal('');
  correo = signal('');
  fechaNacimiento = signal(''); // YYYY-MM-DD por input type="date"
  password = signal('');
  password2 = signal('');

  puedeCrear = signal(false);

  constructor() {
    this.refrescar();
  }

  refrescar() {
    this.cargando.set(true);
    this.ok.set(null);
    this.error.set(null);

    this.svc.listar().subscribe({
      next: (r) => {
        if (!r.exito) {
          this.error.set(r.mensaje || 'No se pudieron cargar los usuarios');
          this.usuarios.set([]);
          return;
        }
        this.usuarios.set(r.datos ?? []);
      },
      error: (e: any) => {
        console.error(e);
        this.error.set(e?.error?.mensaje || 'Error cargando usuarios');
      },
      complete: () => this.cargando.set(false)
    });
  }

  crear() {
    this.ok.set(null);
    this.error.set(null);

    const nombre = this.nombre().trim();
    const correo = this.correo().trim().toLowerCase();
    const fechaNacimiento = this.fechaNacimiento().trim();
    const pass = this.password();
    const pass2 = this.password2();

    if (!nombre) return this.error.set('Nombre requerido');
    if (!correo) return this.error.set('Correo requerido');
    if (!fechaNacimiento) return this.error.set('Fecha de nacimiento requerida');
    if (!pass) return this.error.set('Password requerido');
    if (pass !== pass2) return this.error.set('Las contraseñas no coinciden');

    this.creando.set(true);

    this.svc.crear({ nombre, correo, fechaNacimiento, password: pass }).subscribe({
      next: (r) => {
        if (!r.exito) {
          this.error.set(r.mensaje || 'No se pudo crear el usuario');
          return;
        }
        this.ok.set(r.mensaje || 'Usuario creado');
        this.limpiarForm();
        this.refrescar();
      },
      error: (e: any) => {
        console.error(e);
        this.error.set(e?.error?.mensaje || 'Error creando usuario');
      },
      complete: () => this.creando.set(false)
    });
  }

  eliminar(u: UsuarioEmpresaItem) {
    this.ok.set(null);
    this.error.set(null);

    const id = u.idUsuarioEmpresa;
    if (!id) return;

    this.eliminandoId.set(id);

    this.svc.eliminar(id).subscribe({
      next: (r) => {
        if (!r.exito) {
          // ejemplo típico: "No se puede eliminar el último usuario de empresa" :contentReference[oaicite:3]{index=3}
          this.error.set(r.mensaje || 'No se pudo eliminar el usuario');
          return;
        }
        this.ok.set(r.mensaje || 'Usuario eliminado');
        this.refrescar();
      },
      error: (e: any) => {
        console.error(e);
        this.error.set(e?.error?.mensaje || 'Error eliminando usuario');
      },
      complete: () => this.eliminandoId.set(null)
    });
  }

  private limpiarForm() {
    this.nombre.set('');
    this.correo.set('');
    this.fechaNacimiento.set('');
    this.password.set('');
    this.password2.set('');
  }
}
