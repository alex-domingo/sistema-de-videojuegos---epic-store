import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { ApiResponse } from '../models/api.model';
import { Categoria } from '../models/categoria.model';

@Injectable({ providedIn: 'root' })
export class CategoriasService {
  constructor(private api: ApiService) { }

  listar() {
    return this.api.get<ApiResponse<Categoria[]>>('/categorias');
  }
}
