import { Component, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

import { UsuariosService } from '../../../core/services/usuarios.service';
import type { RegistroUsuarioRequest } from '../../../core/models/registro.model';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './registro.html',
  styleUrl: './registro.scss'
})
export class Registro {
  private usuariosSvc = inject(UsuariosService);
  private router = inject(Router);

  // form
  nickname = signal('');
  correo = signal('');
  password = signal('');
  password2 = signal('');
  fechaNacimiento = signal(''); // input type="date" -> YYYY-MM-DD
  telefono = signal('');
  pais = signal('Guatemala');
  avatar = signal('avatar_default.png');
  bibliotecaPublica = signal(true);

  loading = signal(false);
  error = signal<string | null>(null);
  ok = signal<string | null>(null);

  // Computed signals para validación
  puedeEnviar = computed(() => {
    const nick = this.nickname().trim();
    const correo = this.correo().trim();
    const pass = this.password();
    const pass2 = this.password2();
    const fecha = this.fechaNacimiento().trim();

    return !!nick && !!correo && !!pass && pass === pass2 && !!fecha;
  });

  passwordsIguales = computed(() => this.password() === this.password2());

  registrar() {
    this.error.set(null);
    this.ok.set(null);

    const nick = this.nickname().trim();
    const correo = this.correo().trim().toLowerCase(); // Convertir a minúsculas
    const pass = this.password();
    const pass2 = this.password2();
    const fecha = this.fechaNacimiento().trim();
    const tel = this.telefono().replace(/[^\d]/g, ''); // Limpiar teléfono - solo dígitos

    // validaciones mínimas frontend
    if (!nick) return this.error.set('Nickname requerido');
    if (!correo) return this.error.set('Correo requerido');
    if (!pass) return this.error.set('Password requerido');
    if (pass.length < 4) return this.error.set('Password muy corto');
    if (pass !== pass2) return this.error.set('Las contraseñas no coinciden');
    if (!fecha) return this.error.set('Fecha de nacimiento requerida');

    const payload: RegistroUsuarioRequest = {
      nickname: nick,
      password: pass,
      fechaNacimiento: fecha, // mandamos "YYYY-MM-DD"
      correo, // ahora en minúsculas
      telefono: tel, // teléfono limpio de solo dígitos
      pais: this.pais().trim() || 'Guatemala',
      avatar: this.avatar().trim() || 'avatar_default.png',
      bibliotecaPublica: !!this.bibliotecaPublica()
    };

    this.loading.set(true);

    this.usuariosSvc.registrar(payload).subscribe({
      next: (r) => {
        if (!r.exito) {
          this.error.set(r.mensaje || 'No se pudo registrar el usuario');
          return;
        }
        this.ok.set(r.mensaje || 'Usuario registrado correctamente');
        setTimeout(() => {
          this.router.navigateByUrl('/login?registro=ok');
        }, 300);
      },
      error: (e: any) => {
        const msg = e?.error?.mensaje || e?.message || 'Error registrando usuario';
        this.error.set(msg);
      },
      complete: () => this.loading.set(false)
    });
  }
}