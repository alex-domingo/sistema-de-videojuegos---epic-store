import { Component, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { EmpresasService } from '../../core/services/empresas.service';
import { EmpresaPerfilResponse } from '../../core/models/empresa-catalogo.model';

@Component({
  selector: 'app-empresa-perfil',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './empresa-perfil.html',
  styleUrl: './empresa-perfil.scss'
})
export class EmpresaPerfil {
  private route = inject(ActivatedRoute);
  private empresasSvc = inject(EmpresasService);

  cargando = signal(true);
  error = signal<string | null>(null);

  resp = signal<EmpresaPerfilResponse | null>(null);
  empresa = computed(() => this.resp()?.empresa ?? null);
  catalogo = computed(() => this.resp()?.catalogo ?? []);

  constructor() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!id || Number.isNaN(id)) {
      this.cargando.set(false);
      this.error.set('ID de empresa inválido');
      return;
    }

    this.empresasSvc.perfilPublico(id).subscribe({
      next: (r) => this.resp.set(r),
      error: (e) => {
        console.error(e);
        this.error.set('Error cargando perfil público de la empresa');
      },
      complete: () => this.cargando.set(false)
    });
  }
}
