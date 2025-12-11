import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { LoginRequest } from '../../models/auth.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  credentials: LoginRequest = {
    username: '',
    password: ''
  };

  errorMessage: string = '';
  isLoading: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onSubmit(): void {
    // Validación básica
    if (!this.credentials.username || !this.credentials.password) {
      this.errorMessage = 'Por favor, ingrese usuario y contraseña';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    // Llamada al servicio de autenticación
    this.authService.login(this.credentials).subscribe({
      next: (response) => {
        console.log('Login exitoso:', response);
        this.isLoading = false;
        // Redirigir al dashboard o página principal
        this.router.navigate(['/dashboard']);
      },
      error: (error) => {
        console.error('Error en login:', error);
        this.isLoading = false;
        
        // Manejo de errores según el código HTTP
        if (error.status === 401) {
          this.errorMessage = 'Usuario o contraseña incorrectos';
        } else if (error.status === 0) {
          this.errorMessage = 'No se pudo conectar con el servidor. Verifica que el backend esté corriendo.';
        } else {
          this.errorMessage = 'Error al intentar iniciar sesión. Intente nuevamente.';
        }
      }
    });
  }
}
