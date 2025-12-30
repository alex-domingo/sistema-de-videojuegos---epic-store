import { Injectable } from '@angular/core';
import { ApiService } from './api.service';

@Injectable({ providedIn: 'root' })
export class CategoriasService {
  constructor(private api: ApiService) {}
  listar() { return this.api.get<any>('/categorias'); }
}
