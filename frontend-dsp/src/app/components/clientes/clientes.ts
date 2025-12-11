import { Component, OnInit, OnDestroy, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { ClienteService } from '../../services/cliente.service';
import { Cliente } from '../../models/cliente.model';

@Component({
  selector: 'app-clientes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './clientes.html',
  styleUrl: './clientes.css'
})
export class Clientes implements OnInit, OnDestroy {
  clientes: Cliente[] = [];
  clientesFiltrados: Cliente[] = [];
  isLoading: boolean = false;
  errorMessage: string = '';

  // Definición genérica de columnas para header y filas
  columns: Array<{ key: keyof Cliente; label: string }> = [
    { key: 'codigoCli', label: 'Código' },
    { key: 'nombreCli', label: 'Nombre' },
    { key: 'agenteCli', label: 'Agente' },
    { key: 'cifCli', label: 'CIF' },
    { key: 'titularCli', label: 'Titular' },
    { key: 'enlaceCli', label: 'Enlace' }
  ];

  // Estado de visibilidad de cada filtro
  mostrarFiltros: Record<string, boolean> = {};
  // Valores de cada filtro
  filtros: Record<string, string> = {};

  // Modal nuevo cliente
  mostrarModalNuevo: boolean = false;
  nuevoCliente: Cliente = {
    codigoCli: '',
    nombreCli: '',
    agenteCli: '',
    activoCli: true,
    formenvCli: '',
    formpagCli: '',
    cifCli: '',
    titularCli: '',
    enlaceCli: 0,
    cliente2Cli: ''
  };

  // Toast para mensajes temporales
  toastMessage: string = '';
  private toastHandle: any;
  
  // Para cleanup de subscripciones
  private destroy$ = new Subject<void>();

  constructor(
    private clienteService: ClienteService,
    private router: Router,
    private hostElement: ElementRef
  ) {}

  ngOnInit(): void {
    this.cargarClientes();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  cargarClientes(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.clienteService.getClientes()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (clientes) => {
          // Filtrar solo clientes activos
          this.clientes = clientes.filter(cliente => cliente.activoCli);
          this.clientesFiltrados = [...this.clientes];
          this.isLoading = false;
          console.log('Clientes cargados:', this.clientes);
        },
        error: (error) => {
          console.error('Error al cargar clientes:', error);
          this.isLoading = false;
          
          if (error.status === 401) {
            this.errorMessage = 'No autorizado. Por favor, inicie sesión nuevamente.';
            // Opcional: redirigir al login
            // this.router.navigate(['/login']);
          } else if (error.status === 0) {
            this.errorMessage = 'No se pudo conectar con el servidor. Verifica que el backend esté corriendo.';
          } else {
            this.errorMessage = 'Error al cargar los clientes. Intente nuevamente.';
          }
        }
      });
  }

  // Toggle para mostrar un filtro específico
  toggleFiltro(key: string): void {
    this.mostrarFiltros[key] = true;
    setTimeout(() => {
      const el = (this.hostElement.nativeElement as HTMLElement).querySelector(`input[data-filter-key="${key}"]`) as HTMLInputElement | null;
      el?.focus();
    });
  }

  // Cambio en cualquier filtro
  onFiltroChange(key: string): void {
    this.aplicarFiltros();
  }

  // Blur: si vacío, cerrar
  onFiltroBlur(key: string): void {
    if ((this.filtros[key] || '').trim().length === 0) {
      this.cerrarFiltro(key);
    }
  }

  // Tecla Escape cierra
  onFiltroKey(event: KeyboardEvent, key: string): void {
    if (event.key === 'Escape') {
      this.cerrarFiltro(key);
    }
  }

  // Cerrar un filtro y restaurar datos si no quedan filtros activos
  cerrarFiltro(key: string): void {
    this.mostrarFiltros[key] = false;
    this.filtros[key] = '';
    this.aplicarFiltros();
  }

  // Aplicar todos los filtros activos
  private aplicarFiltros(): void {
    let resultado = [...this.clientes];
    for (const col of this.columns) {
      const valor = (this.filtros[col.key as string] || '').trim().toLowerCase();
      if (valor.length > 0) {
        resultado = resultado.filter(c => (c[col.key] ?? '')
          .toString()
          .toLowerCase()
          .includes(valor));
      }
    }
    this.clientesFiltrados = resultado;
  }

  // Obtener valor de una columna del cliente para el template
  getClienteValue(cliente: Cliente, key: keyof Cliente): unknown {
    return cliente[key];
  }

  // Método para refrescar los datos
  refrescar(): void {
    this.cargarClientes();
  }

  // -------- Modal: Nuevo cliente --------
  abrirModalNuevo(): void {
    this.mostrarModalNuevo = true;
  }

  cerrarModalNuevo(): void {
    this.mostrarModalNuevo = false;
  }

  guardarNuevoCliente(): void {
    // Validación previa al POST
    if (!this.validarNuevoCliente()) {
      this.mostrarToast('Los datos del cliente no pueden estar vacíos');
      return;
    }

    this.isLoading = true;
    this.clienteService.createCliente(this.nuevoCliente).pipe(takeUntil(this.destroy$)).subscribe({
      next: (creado) => {
        // Añadir al listado local y re-aplicar filtros
        this.clientes = [creado, ...this.clientes];
        this.aplicarFiltros();
        this.isLoading = false;
        this.mostrarModalNuevo = false;
        // Reset del formulario
        this.nuevoCliente = {
          codigoCli: '',
          nombreCli: '',
          agenteCli: '',
          activoCli: true,
          formenvCli: '',
          formpagCli: '',
          cifCli: '',
          titularCli: '',
          enlaceCli: 0,
          cliente2Cli: ''
        };
        this.errorMessage = '';
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'No se pudo crear el cliente. Verifique los datos o intente más tarde.';
        console.error('Error creando cliente', err);
      }
    });
  }

  // -------- Utilidades: validación y toast --------
  private validarNuevoCliente(): boolean {
    const req = [
      this.nuevoCliente.codigoCli?.trim(),
      this.nuevoCliente.nombreCli?.trim(),
    ];
    return req.every(v => !!v && v.length > 0);
  }

  private mostrarToast(msg: string): void {
    this.toastMessage = msg;
    if (this.toastHandle) {
      clearTimeout(this.toastHandle);
    }
    this.toastHandle = setTimeout(() => {
      this.toastMessage = '';
    }, 3000);
  }
}
