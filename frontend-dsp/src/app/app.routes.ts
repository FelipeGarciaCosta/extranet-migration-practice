import { Routes } from '@angular/router';
import { Login } from './components/login/login';
import { Dashboard } from './components/dashboard/dashboard';
import { Clientes } from './components/clientes/clientes';
import { Contactos } from './components/contactos/contactos';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: Login
  },
  {
    path: 'dashboard',
    component: Dashboard
  },
  {
    path: 'clientes',
    component: Clientes
  },
  { path: 'contactos', component: Contactos },
  { path: 'contactos/:codCli', component: Contactos },
  {
    path: '**',
    redirectTo: '/login'
  }
];