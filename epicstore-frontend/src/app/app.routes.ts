import { Routes } from '@angular/router';
import { Catalogo } from './features/catalogo/catalogo';
import { VideojuegoDetalle } from './features/videojuego-detalle/videojuego-detalle';

export const routes: Routes = [
    { path: '', component: Catalogo },
    { path: 'videojuego/:id', component: VideojuegoDetalle },
    { path: '**', redirectTo: '' }
];
