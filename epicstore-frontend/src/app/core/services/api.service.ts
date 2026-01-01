import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { API_BASE } from '../config/api.config';

@Injectable({
    providedIn: 'root'
})
export class ApiService {
    constructor(private http: HttpClient) { }

    // ---------- GET ----------
    get<T>(path: string, params?: Record<string, any>) {
        let httpParams = new HttpParams();

        if (params) {
            Object.keys(params).forEach((key) => {
                const value = params[key];
                if (value !== undefined && value !== null) {
                    httpParams = httpParams.set(key, value);
                }
            });
        }

        return this.http.get<T>(
            `${API_BASE}${path}`,
            {
                params: httpParams,
                withCredentials: true
            }
        );
    }

    // ---------- POST ----------
    post<T>(path: string, body: unknown) {
        return this.http.post<T>(
            `${API_BASE}${path}`,
            body,
            { withCredentials: true }
        );
    }

    // ---------- PUT ----------
    put<T>(path: string, body: unknown) {
        return this.http.put<T>(
            `${API_BASE}${path}`,
            body,
            { withCredentials: true }
        );
    }

    // ---------- DELETE ----------
    delete<T>(path: string, params?: Record<string, any>) {
        let httpParams = new HttpParams();

        if (params) {
            Object.keys(params).forEach((key) => {
                const value = params[key];
                if (value !== undefined && value !== null) {
                    httpParams = httpParams.set(key, value);
                }
            });
        }

        return this.http.delete<T>(
            `${API_BASE}${path}`,
            {
                params: httpParams,
                withCredentials: true
            }
        );
    }
    deleteWithBody<T>(path: string, body: any) {
        return this.http.delete<T>(
            `${API_BASE}${path}`,
            {
                body,
                withCredentials: true
            }
        );
    }
}
