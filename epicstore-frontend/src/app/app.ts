import { Component, signal, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { CategoriasService } from './core/services/categorias.service';
import { VideojuegosService } from './core/services/videojuegos.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('epicstore-frontend');

  private categoriasSvc = inject(CategoriasService);
  private videojuegosSvc = inject(VideojuegosService);

  // ✅ signals para que el template siempre se actualice y no haya confusión
  categorias = signal<any>(null);
  videojuegos = signal<any>(null);

  constructor() {
    this.categoriasSvc.listar().subscribe({
      next: (data) => this.categorias.set(data),
      error: (e) => this.categorias.set({ error: true, status: e?.status, message: e?.message })
    });

    this.videojuegosSvc.listar().subscribe({
      next: (data) => this.videojuegos.set(data),
      error: (e) => this.videojuegos.set({ error: true, status: e?.status, message: e?.message })
    });
  }
}
