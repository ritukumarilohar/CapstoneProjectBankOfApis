import { Component, OnInit, ChangeDetectorRef, signal } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AccountService, AccountDTO } from '../../services/accountService';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit {
  accounts = signal<AccountDTO[]>([]);
  totalBalance = signal<number>(0);
  
  constructor(
    private accountService: AccountService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}
  
  ngOnInit(): void {
    this.loadAccounts();
  }
  
  loadAccounts(): void {
    this.accountService.getAccounts().subscribe({
      next: (accounts: AccountDTO[]) => {
        this.accounts.set(accounts);
        this.calculateTotalBalance();
        this.cdr.markForCheck();
      },
      error: (error: any) => {
        console.error('Error loading accounts:', error);
        if (error.status === 503) {
          console.error('Backend service is unavailable. Please check if the server is running on port 8080');
        }
        this.cdr.markForCheck();
      }
    });
  }
  
  calculateTotalBalance(): void {
    const total = this.accounts().reduce((sum, account) => sum + (account.balance || 0), 0);
    this.totalBalance.set(total);
  }
  
  navigateToAccounts(): void {
    this.router.navigate(['/accounts']);
  }
  
  navigateToTransfer(): void {
    this.router.navigate(['/transfer']);
  }
  
  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('userId');
    this.router.navigate(['/login']);
  }
}