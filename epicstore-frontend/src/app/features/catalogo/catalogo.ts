import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CategoriasService } from '../../core/services/categorias.service';
import { VideojuegosService } from '../../core/services/videojuegos.service';

@Component({
  selector: 'app-catalogo',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './catalogo.html',
  styleUrl: './catalogo.scss'
})
export class Catalogo {
  private categoriasSvc = inject(CategoriasService);
  private videojuegosSvc = inject(VideojuegosService);

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
