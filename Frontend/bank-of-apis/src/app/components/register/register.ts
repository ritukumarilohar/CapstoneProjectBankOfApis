//register.ts
import { Component, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Auth } from '../../services/auth';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, CommonModule, HttpClientModule],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register {
  user = {
    name: '',
    email: '',
    phone: '',
    password: '',
    confirmPassword: '',
    role:'customer'
  };
  passwordStrength = 0;
  isLoading = false;
  errorMessage = '';
  constructor(
    private authService: Auth, 
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  updatePasswordStrength() {
    if (!this.user.password) {
      this.passwordStrength = 0;
      return;
    }
    
    let strength = 0;
    
    // Length check
    if (this.user.password.length >= 8) strength += 25;
    
    // Contains both lower and uppercase
    if (this.user.password.match(/([a-z].*[A-Z])|([A-Z].*[a-z])/)) strength += 25;
    
    // Contains numbers
    if (this.user.password.match(/([0-9])/)) strength += 25;
    
    // Contains special characters
    if (this.user.password.match(/([!,@,#,$,%,^,&,*,?,_,~])/)) strength += 25;
    
    this.passwordStrength = strength;
  }

  getPasswordStrengthColor() {
    if (this.passwordStrength < 25) return '#e74c3c'; // Weak - red
    if (this.passwordStrength < 50) return '#f39c12'; // Medium - orange
    if (this.passwordStrength < 75) return '#3498db'; // Good - blue
    return '#2ecc71'; // Strong - green
  }

  selectRole(role: string) {
    this.user.role = role;
  }
  
 onSubmit() {
  this.errorMessage = '';
  
  // Basic validation
  if (this.user.password !== this.user.confirmPassword) {
    this.errorMessage = 'Passwords do not match';
    return;
  }
  
  if (!this.user.role) {
    this.errorMessage = 'Please select a role';
    return;
  }
  
  this.isLoading = true;
  
  const { confirmPassword, ...userData } = this.user;
  
  this.authService.register(userData).subscribe({
    next: (response: any) => {
      console.log('Registration response:', response); // Add this for debugging
      this.isLoading = false;
      
      // Check if response indicates success
      if (response && (response.status === 'success' || response.message?.includes('successful'))) {
        alert('Registration successful! Please login.');
        this.router.navigate(['/login']);
      } else {
        // Even if we get a 200 response, check the content
        this.errorMessage = response?.message || 'Registration completed but please verify.';
        alert('Registration may have succeeded. Please try logging in.');
        this.router.navigate(['/login']);
      }
      this.cdr.markForCheck();
    },
    error: (error: any) => {
      console.error('Registration error:', error); // Add this for debugging
      this.isLoading = false;
      
      // More detailed error handling
      if (error.status === 0) {
        this.errorMessage = 'Unable to connect to server. Please check your connection.';
      } else if (error.status === 409) {
        this.errorMessage = 'Email already exists. Please use a different email.';
      } else if (error.error?.message) {
        this.errorMessage = error.error.message;
      } else {
        this.errorMessage = 'Registration failed. Please try again.';
      }
      this.cdr.markForCheck();
    }
  });
}
  goToLogin() {
    this.router.navigate(['/login']);
  }
}