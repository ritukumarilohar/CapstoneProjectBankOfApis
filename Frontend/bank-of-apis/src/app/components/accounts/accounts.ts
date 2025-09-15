import { Component, OnInit, ChangeDetectorRef, signal } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AccountService, AccountDTO, TransactionDTO } from '../../services/accountService';

@Component({
  selector: 'app-accounts',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './accounts.html',
  styleUrl: './accounts.css'
})
export class Accounts implements OnInit {
  accounts = signal<AccountDTO[]>([]);
  transactions = signal<TransactionDTO[]>([]);
  selectedAccount = signal<AccountDTO | null>(null);
  showAddAccountForm = signal<boolean>(false);
  
  // Form data for new account
  newAccount: Partial<AccountDTO> = {
    bankName: '',
    accountNumber: '',
    accountType: 'SAVINGS',
    balance: 0,
    status: 'ACTIVE'
  };

  accountTypes = ['SAVINGS', 'CURRENT', 'FIXED_DEPOSIT', 'RECURRING_DEPOSIT'];

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
        this.cdr.markForCheck();
      },
      error: (error: any) => {
        console.error('Error loading accounts:', error);
        // Handle the case where backend is not available
        if (error.status === 503) {
          console.error('Backend service is unavailable. Please check if the server is running on port 8080');
          alert('Unable to connect to the server. Please ensure the backend is running.');
        }
        this.cdr.markForCheck();
      }
    });
  }

  viewTransactions(account: AccountDTO): void {
    this.selectedAccount.set(account);
    if (account.id) {
      this.accountService.getAccountTransactions(account.id).subscribe({
        next: (transactions: TransactionDTO[]) => {
          this.transactions.set(transactions);
          this.cdr.markForCheck();
        },
        error: (error: any) => {
          console.error('Error loading transactions:', error);
          this.cdr.markForCheck();
        }
      });
    }
  }

  // Toggle add account form
  toggleAddAccountForm(): void {
    this.showAddAccountForm.set(!this.showAddAccountForm());
    if (!this.showAddAccountForm()) {
      this.resetForm();
    }
  }

  // Add new account
  addAccount(): void {
    if (this.validateForm()) {
      this.accountService.createAccount(this.newAccount).subscribe({
        next: (createdAccount: AccountDTO) => {
          console.log('Account created successfully:', createdAccount);
          // Add the new account to the list
          this.accounts.update(accounts => [...accounts, createdAccount]);
          // Reset form and hide it
          this.resetForm();
          this.showAddAccountForm.set(false);
          alert('Account created successfully!');
          this.cdr.markForCheck();
        },
        error: (error: any) => {
          console.error('Error creating account:', error);
          alert('Failed to create account. Please try again.');
          this.cdr.markForCheck();
        }
      });
    }
  }

  // Form validation
  validateForm(): boolean {
    if (!this.newAccount.bankName?.trim()) {
      alert('Please enter bank name');
      return false;
    }
    if (!this.newAccount.accountNumber?.trim()) {
      alert('Please enter account number');
      return false;
    }
    if ((this.newAccount.balance ?? 0) < 0) {
      alert('Balance cannot be negative');
      return false;
    }
    return true;
  }

  // Reset form
  resetForm(): void {
    this.newAccount = {
      bankName: '',
      accountNumber: '',
      accountType: 'SAVINGS',
      balance: 0,
      status: 'ACTIVE'
    };
  }

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }

  
}