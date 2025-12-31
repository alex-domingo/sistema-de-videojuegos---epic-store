import { Routes } from '@angular/router';
import { Catalogo } from './features/catalogo/catalogo';
import { VideojuegoDetalle } from './features/videojuego-detalle/videojuego-detalle';
import { EmpresaPerfil } from './features/empresa-perfil/empresa-perfil';
import { Login } from './features/auth/login/login';
import { LoginEmpresa } from './features/auth/login-empresa/login-empresa';

export const routes: Routes = [
    { path: '', component: Catalogo },
    { path: 'videojuego/:id', component: VideojuegoDetalle },
    { path: 'empresa/:id', component: EmpresaPerfil },

    { path: 'login', component: Login },
    { path: 'login-empresa', component: LoginEmpresa },

    { path: '**', redirectTo: '' }
];

