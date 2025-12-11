import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cliente } from '../models/cliente.model';

@Injectable({
  providedIn: 'root'
})
export class ClienteService {
  private apiUrl = 'http://10.1.1.38:8080/api/clientes';

  constructor(private http: HttpClient) {}

  /**
   * Obtener todos los clientes
   * El token JWT se añade automáticamente por el interceptor
   */
  getClientes(): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(this.apiUrl);
  }

  /**
   * Obtener cliente por código
   */
  getClienteByCodigo(codigo: string): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(`${this.apiUrl}/${codigo}`);
  }

  /**
   * Crear un nuevo cliente
   */
  createCliente(cliente: Cliente): Observable<Cliente> {
    return this.http.post<Cliente>(this.apiUrl, cliente);
  }
}