import { Component, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { CategoriasService } from '../../core/services/categorias.service';
import { VideojuegosService } from '../../core/services/videojuegos.service';
import { Categoria } from '../../core/models/categoria.model';
import { VideojuegoListadoItem } from '../../core/models/videojuego.model';
import { ApiResponse } from '../../core/models/api.model';

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

  categoriasResp = signal<ApiResponse<Categoria[]> | null>(null);
  videojuegosResp = signal<ApiResponse<VideojuegoListadoItem[]> | null>(null);

  categoriaSeleccionadaId = signal<number | 'todas'>('todas');

  categorias = computed(() => this.categoriasResp()?.datos ?? []);
  videojuegos = computed(() => this.videojuegosResp()?.datos ?? []);

  videojuegosFiltrados = computed(() => {
    const lista = this.videojuegos();
    const cat = this.categoriaSeleccionadaId();
    if (cat === 'todas') return lista;
    return lista;
  });

  cargando = signal(true);
  error = signal<string | null>(null);

  constructor() {
    this.cargar();
  }

  cargar() {
    this.cargando.set(true);
    this.error.set(null);

    this.categoriasSvc.listar().subscribe({
      next: (resp) => this.categoriasResp.set(resp),
      error: (e) => {
        console.error(e);
        this.error.set('Error cargando categorías');
      }
    });

    this.videojuegosSvc.listar().subscribe({
      next: (resp) => this.videojuegosResp.set(resp),
      error: (e) => {
        console.error(e);
        this.error.set('Error cargando videojuegos');
      },
      complete: () => this.cargando.set(false)
    });
  }

  seleccionarCategoria(id: number | 'todas') {
    this.categoriaSeleccionadaId.set(id);
  }
}
