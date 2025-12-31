import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { SessionState } from '../../../core/state/session.state';

@Component({
  selector: 'app-login-empresa',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login-empresa.html'
})
export class LoginEmpresa {
  private auth = inject(AuthService);
  private state = inject(SessionState);
  private router = inject(Router);

  correo = signal('');
  password = signal('');
  error = signal<string | null>(null);
  loading = signal(false);

  submit() {
    this.error.set(null);
    this.loading.set(true);

    this.auth.loginEmpresa({ correo: this.correo(), password: this.password() }).subscribe({
      next: () => {
        this.auth.me().subscribe({
          next: (resp) => this.state.session.set(resp.datos),
          complete: () => this.router.navigateByUrl('/')
        });
      },
      error: () => this.error.set('Credenciales inválidas o error de servidor'),
      complete: () => this.loading.set(false)
    });
  }
}
