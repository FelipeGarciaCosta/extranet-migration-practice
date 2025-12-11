import { Component, OnDestroy, OnInit, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { Contacto } from '../../models/contacto.model';
import { ContactoService } from '../../services/contacto.service';

@Component({
	selector: 'app-contactos',
	standalone: true,
	imports: [CommonModule, FormsModule],
	templateUrl: './contactos.html',
	styleUrl: './contactos.css'
})
export class Contactos implements OnInit, OnDestroy {
	isLoading = false;
	errorMessage = '';
	toastMessage = '';

	codCli = '';

	contactos: Contacto[] = [];
	contactosFiltrados: Contacto[] = [];

	columns: Array<{ key: keyof Contacto; label: string }> = [
		{ key: 'codigoCon', label: 'Código' },
		{ key: 'nombreCon', label: 'Nombre' },
		{ key: 'telefonoCon', label: 'Teléfono' },
		{ key: 'movilCon', label: 'Móvil' },
		{ key: 'emailCon', label: 'Email' },
		{ key: 'activoCon', label: 'Activo' },
		{ key: 'principalCon', label: 'Principal' },
	];

	mostrarFiltros: Record<string, boolean> = {};
	filtros: Record<string, string> = {};

	private destroy$ = new Subject<void>();

	constructor(
		private route: ActivatedRoute,
		private router: Router,
		private contactoService: ContactoService,
		private host: ElementRef,
	) {}

	ngOnInit(): void {
		this.route.paramMap.subscribe(p => {
			const param = p.get('codCli');
			if (param) {
				this.codCli = param;
				this.cargarContactos();
			}
		});
	}

	ngOnDestroy(): void {
		this.destroy$.next();
		this.destroy$.complete();
	}

	cargarContactos(): void {
		if (!this.codCli?.trim()) {
			this.mostrarToast('Ingrese un código de cliente');
			return;
		}
		this.isLoading = true;
		this.errorMessage = '';
		this.contactoService.getContactosByCliente(this.codCli)
			.pipe(takeUntil(this.destroy$))
			.subscribe({
				next: (list) => {
					this.contactos = list ?? [];
					this.contactosFiltrados = [...this.contactos];
					this.isLoading = false;
				},
				error: (err) => {
					this.isLoading = false;
					this.errorMessage = 'No se pudieron cargar los contactos.';
					console.error('Error contactos', err);
				}
			});
	}

	refrescar(): void {
		this.cargarContactos();
	}

	// Filtros genéricos (mismo patrón que Clientes)
	toggleFiltro(key: string): void {
		this.mostrarFiltros[key] = true;
		setTimeout(() => {
			const el = (this.host.nativeElement as HTMLElement).querySelector(`input[data-filter-key="${key}"]`) as HTMLInputElement | null;
			el?.focus();
		});
	}
	onFiltroChange(key: string): void { this.aplicarFiltros(); }
	onFiltroBlur(key: string): void {
		if ((this.filtros[key] || '').trim().length === 0) this.cerrarFiltro(key);
	}
	onFiltroKey(event: KeyboardEvent, key: string): void { if (event.key === 'Escape') this.cerrarFiltro(key); }
	cerrarFiltro(key: string): void { this.mostrarFiltros[key] = false; this.filtros[key] = ''; this.aplicarFiltros(); }

	private aplicarFiltros(): void {
		let resultado = [...this.contactos];
		for (const col of this.columns) {
			const valor = (this.filtros[col.key as string] || '').trim().toLowerCase();
			if (valor) {
				resultado = resultado.filter(c => (this.getContactoValue(c, col.key) ?? '')
					.toString().toLowerCase().includes(valor));
			}
		}
		this.contactosFiltrados = resultado;
	}

	getContactoValue(c: Contacto, key: keyof Contacto): unknown { return c[key]; }

	private mostrarToast(msg: string): void {
		this.toastMessage = msg;
		setTimeout(() => this.toastMessage = '', 2500);
	}
}
