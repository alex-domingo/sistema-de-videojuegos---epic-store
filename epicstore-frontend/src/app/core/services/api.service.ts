import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_BASE } from '../config/api.config';

@Injectable({ providedIn: 'root' })
export class ApiService {
    constructor(private http: HttpClient) { }

    get<T>(path: string, params?: Record<string, string | number | boolean>) {
        return this.http.get<T>(`${API_BASE}${path}`, { params: params as any });
    }
}
