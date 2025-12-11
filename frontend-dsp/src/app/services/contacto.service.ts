import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Contacto } from '../models/contacto.model';

@Injectable({ providedIn: 'root' })
export class ContactoService {
  private apiRoot = 'http://10.1.1.38:8080/api/clientes';

  constructor(private http: HttpClient) {}

  getContactosByCliente(codCli: string): Observable<Contacto[]> {
    return this.http.get<Contacto[]>(`${this.apiRoot}/${encodeURIComponent(codCli)}/contactos`);
  }

  getContactoById(codCli: string, contactoId: number): Observable<Contacto> {
    return this.http.get<Contacto>(`${this.apiRoot}/${encodeURIComponent(codCli)}/contactos/${contactoId}`);
  }
}
