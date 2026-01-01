import { Routes } from '@angular/router';
import { Catalogo } from './features/catalogo/catalogo';
import { VideojuegoDetalle } from './features/videojuego-detalle/videojuego-detalle';
import { EmpresaPerfil } from './features/empresa-perfil/empresa-perfil';
import { Login } from './features/auth/login/login';
import { LoginEmpresa } from './features/auth/login-empresa/login-empresa';
import { Forbidden } from './features/auth/forbidden/forbidden';
import { usuarioGuard } from './core/guards/usuario.guard';
import { empresaGuard } from './core/guards/empresa.guard';
import { adminGuard } from './core/guards/admin.guard';
import { Biblioteca } from './features/biblioteca/biblioteca';
import { Cartera } from './features/cartera/cartera';
import { Registro } from './features/auth/registro/registro';
import { MisGrupos } from './features/grupos/mis-grupos/mis-grupos';
import { GrupoDetalle } from './features/grupos/grupo-detalle/grupo-detalle';

export const routes: Routes = [
    // públicas
    { path: '', component: Catalogo },
    { path: 'videojuego/:id', component: VideojuegoDetalle },
    { path: 'empresa/:id', component: EmpresaPerfil },
    { path: 'login', component: Login },
    { path: 'login-empresa', component: LoginEmpresa },
    { path: 'forbidden', component: Forbidden },

    // protegidas (placeholder por ahora)
    // ejemplo:
    { path: 'biblioteca', component: Biblioteca, canActivate: [usuarioGuard] },
    { path: 'cartera', component: Cartera, canActivate: [usuarioGuard] },
    { path: 'registro', component: Registro },
    { path: 'grupos', component: MisGrupos, canActivate: [usuarioGuard] },
    { path: 'grupos/:idGrupo', component: GrupoDetalle, canActivate: [usuarioGuard] },
    // { path: 'empresa/dashboard', component: EmpresaDashboard, canActivate: [empresaGuard] },
    // { path: 'admin', component: AdminHome, canActivate: [adminGuard] },

    { path: '**', redirectTo: '' }
];
