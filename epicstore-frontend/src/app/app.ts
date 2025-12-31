import { Component, inject, signal } from '@angular/core';
import { Router, RouterOutlet, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from './core/services/auth.service';
import { SessionState } from './core/state/session.state';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterModule],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('EpicStore');

  private auth = inject(AuthService);
  protected state = inject(SessionState);
  private router = inject(Router);

  cerrando = signal(false);
  errorLogout = signal<string | null>(null);

  logout() {
    this.errorLogout.set(null);
    this.cerrando.set(true);

    this.auth.logout().subscribe({
      next: () => {
        // refrescar estado real
        this.auth.me().subscribe({
          next: (resp) => this.state.session.set(resp.datos),
          error: () => this.state.session.set({ autenticado: false }),
          complete: () => this.router.navigateByUrl('/')
        });
      },
      error: () => this.errorLogout.set('No se pudo cerrar sesión'),
      complete: () => this.cerrando.set(false)
    });
  }
}
