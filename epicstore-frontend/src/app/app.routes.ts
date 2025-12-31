import { Routes } from '@angular/router';
import { Catalogo } from './features/catalogo/catalogo';
import { VideojuegoDetalle } from './features/videojuego-detalle/videojuego-detalle';
import { EmpresaPerfil } from './features/empresa-perfil/empresa-perfil';

export const routes: Routes = [
    { path: '', component: Catalogo },
    { path: 'videojuego/:id', component: VideojuegoDetalle },
    { path: 'empresa/:id', component: EmpresaPerfil },
    { path: '**', redirectTo: '' }
];
