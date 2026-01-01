import { Component, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { VideojuegosService } from '../../core/services/videojuegos.service';
import { ApiResponse } from '../../core/models/api.model';
import { VideojuegoDetalle as VideojuegoDetalleModel } from '../../core/models/videojuego.model';
import { SessionState } from '../../core/state/session.state';
import { ComprasService } from '../../core/services/compras.service';
import { ComentariosService } from '../../core/services/comentarios.service';
import type { AuthMeResponse } from '../../core/models/auth.model';
import type { ComentarioItemApi, ComentarioNodo } from '../../core/models/comentarios.model';

// Helper para armar hilos (árbol) + filtrar visibles
function armarHilos(lista: ComentarioItemApi[]): ComentarioNodo[] {
  // solo visibles
  const visibles = (lista ?? []).filter(c => c.textoVisible);

  const map = new Map<number, ComentarioNodo>();
  const roots: ComentarioNodo[] = [];

  for (const c of visibles) {
    map.set(c.idComentario, { ...c, respuestas: [] });
  }

  for (const c of map.values()) {
    const padre = c.idComentarioPadre;
    if (padre == null) {
      roots.push(c);
    } else {
      map.get(padre)?.respuestas.push(c);
    }
  }

  // opcional: ordenar por id o fecha (hoy lo dejamos simple)
  return roots;
}

@Component({
  selector: 'app-videojuego-detalle',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './videojuego-detalle.html',
  styleUrl: './videojuego-detalle.scss'
})
export class VideojuegoDetalle {
  private route = inject(ActivatedRoute);
  private videojuegosSvc = inject(VideojuegosService);
  private comprasSvc = inject(ComprasService);
  private comentariosSvc = inject(ComentariosService);
  public sessionState = inject(SessionState);

  // Signals para el videojuego
  cargando = signal(true);
  error = signal<string | null>(null);
  resp = signal<ApiResponse<VideojuegoDetalleModel> | null>(null);
  juego = computed(() => this.resp()?.datos ?? null);

  // Signals para la funcionalidad de compra
  comprando = signal(false);
  okCompra = signal<string | null>(null);
  errorCompra = signal<string | null>(null);
  fechaCompra = signal<string>(new Date().toISOString().slice(0, 10)); // YYYY-MM-DD
  puedeComprar = computed(() => this.sessionState.esUsuario() && !!this.juego());

  // Signals para comentarios
  comentarios = signal<ComentarioNodo[]>([]);
  cargandoComentarios = signal(false);
  errorComentarios = signal<string | null>(null);

  // Formulario de comentarios
  textoComentario = signal('');
  calificacion = signal(5);
  respondiendoA = signal<number | null>(null); // null = comentario raíz
  enviandoComentario = signal(false);
  okComentario = signal<string | null>(null);

  constructor() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!id || Number.isNaN(id)) {
      this.cargando.set(false);
      this.error.set('ID inválido');
      return;
    }

    this.videojuegosSvc.detallePorId(id).subscribe({
      next: (r) => {
        this.resp.set(r);
        // Cargar comentarios después de obtener el juego
        if (r.datos) {
          this.cargarComentarios(r.datos.idVideojuego);
        }
      },
      error: (e) => {
        console.error(e);
        this.error.set('Error cargando detalle del videojuego');
      },
      complete: () => this.cargando.set(false)
    });
  }

  // Helper para obtener el idUsuario de la sesión de forma segura
  private getIdUsuarioSesion(): number | null {
    const s = this.sessionState.session() as AuthMeResponse['datos'] | null;
    if (!s || s.autenticado !== true) return null;
    if (s.tipoSesion !== 'USUARIO') return null;
    return s.idUsuario;
  }

  // Método para realizar la compra
  comprar() {
    this.okCompra.set(null);
    this.errorCompra.set(null);

    const s = this.sessionState.session();
    if (!s || s.autenticado !== true || s.tipoSesion !== 'USUARIO') {
      this.errorCompra.set('Debes iniciar sesión como usuario para comprar.');
      return;
    }

    const v = this.juego();
    if (!v) {
      this.errorCompra.set('No se pudo detectar el videojuego.');
      return;
    }

    const fecha = this.fechaCompra().trim();
    if (!fecha) {
      this.errorCompra.set('Debes ingresar la fecha de compra.');
      return;
    }

    this.comprando.set(true);

    this.comprasSvc.comprar({
      idUsuario: s.idUsuario,
      idVideojuego: v.idVideojuego,
      fechaCompra: fecha
    }).subscribe({
      next: (r) => {
        if (!r.exito) {
          this.errorCompra.set(r.mensaje || 'No se pudo realizar la compra.');
          return;
        }
        this.okCompra.set(r.mensaje || 'Compra realizada.');
      },
      error: (e: any) => {
        const msg = e?.error?.mensaje || e?.message || 'Error al realizar la compra';
        this.errorCompra.set(msg);
      },
      complete: () => this.comprando.set(false)
    });
  }

  // Métodos para comentarios
  cargarComentarios(idVideojuego: number) {
    this.cargandoComentarios.set(true);
    this.errorComentarios.set(null);

    this.comentariosSvc.listar(idVideojuego).subscribe({
      next: (r) => {
        if (!r.exito) {
          this.errorComentarios.set(r.mensaje || 'No se pudieron cargar comentarios');
          return;
        }
        this.comentarios.set(armarHilos(r.datos));
      },
      error: (e) => {
        console.error(e);
        this.errorComentarios.set('Error cargando comentarios');
      },
      complete: () => this.cargandoComentarios.set(false),
    });
  }

  enviarComentario() {
    this.okComentario.set(null);
    this.errorComentarios.set(null);

    const s: any = this.sessionState.session();
    if (!s || s.autenticado !== true || s.tipoSesion !== 'USUARIO') {
      this.errorComentarios.set('Debes iniciar sesión como usuario para comentar.');
      return;
    }

    const v = this.juego();
    if (!v) return;

    const texto = this.textoComentario().trim();
    if (!texto) {
      this.errorComentarios.set('Escribe un comentario.');
      return;
    }

    const cal = Number(this.calificacion());
    if (cal < 1 || cal > 5) {
      this.errorComentarios.set('Calificación inválida (1 a 5).');
      return;
    }

    this.enviandoComentario.set(true);

    this.comentariosSvc.crear({
      idUsuario: s.idUsuario,
      idVideojuego: v.idVideojuego,
      calificacion: cal,
      texto,
      idComentarioPadre: this.respondiendoA(), // null = raíz, número = respuesta
    }).subscribe({
      next: (r: any) => {
        if (r?.exito === false) {
          this.errorComentarios.set(r?.mensaje || 'No se pudo enviar el comentario');
          return;
        }
        this.okComentario.set(r?.mensaje || 'Comentario enviado');
        this.textoComentario.set('');
        this.respondiendoA.set(null);
        this.cargarComentarios(v.idVideojuego); // fuente de verdad
      },
      error: (e: any) => {
        const msg = e?.error?.mensaje || 'Error enviando comentario';
        this.errorComentarios.set(msg);
      },
      complete: () => this.enviandoComentario.set(false),
    });
  }

  // Métodos auxiliares para comentarios
  comenzarRespuesta(idComentarioPadre: number) {
    this.respondiendoA.set(idComentarioPadre);
    // Opcional: desplazar el foco al textarea
  }

  cancelarRespuesta() {
    this.respondiendoA.set(null);
  }
}