import { Component, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  user = {
    email: '',
    password: ''
  };

  constructor(
    private authService: Auth, 
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  onSubmit() {
  this.authService.login(this.user).subscribe({
    next: (response) => {
      console.log('Login response:', response); // Add this
      localStorage.setItem('token', response.token);
      localStorage.setItem('userId', response.userId);
      console.log('Token stored:', localStorage.getItem('token')); // Add this
      this.router.navigate(['/dashboard']);
      this.cdr.markForCheck();
    },
    error: (error) => {
      console.log('Login error:', error); // Add this
      alert('Login failed: ' + error.message);
      this.cdr.markForCheck();
    }
  });
}

  goToRegister() {
    this.router.navigate(['/register']);
  }
}