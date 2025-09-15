import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AccountService } from '../../services/accountService';
import { TransferService } from '../../services/transfer';

@Component({
  selector: 'app-transfer',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './transfer.html',
  styleUrl: './transfer.css'
})
export class Transfer implements OnInit {
   accounts: any[] = [];
  transferData = {
    fromAccountId: '',
    toAccountId: '',
    amount: 0,
    remarks: ''
  };
  isTransferring: boolean = false;

  constructor(
    private accountService: AccountService, 
    private transferService: TransferService, 
    private router: Router
  ) {}

  ngOnInit() {
    this.loadAccounts();
  }

  loadAccounts() {
    this.accountService.getAccounts().subscribe({
      next: (accounts) => {
        this.accounts = accounts;
      },
      error: (error) => {
        console.error('Error loading accounts:', error);
      }
    });
  }

  onSubmit() {
    if (!this.transferData.fromAccountId || !this.transferData.toAccountId) {
      alert('Please select both source and destination accounts');
      return;
    }

    const fromAcc = this.accounts.find(acc => acc.id === this.transferData.fromAccountId);
    const toAcc = this.accounts.find(acc => acc.id === this.transferData.toAccountId);

    if (fromAcc && toAcc && fromAcc.accountNumber === toAcc.accountNumber) {
      alert('Source and destination accounts cannot be the same');
      return;
    }

    if (this.transferData.amount <= 0) {
      alert('Amount must be greater than 0');
      return;
    }

    if (fromAcc && this.transferData.amount > fromAcc.balance) {
      alert('Insufficient balance');
      return;
    }

    this.isTransferring = true;
    this.transferService.transferFunds(this.transferData).subscribe({
      next: (response) => {
        alert('Transfer successful! Reference ID: ' + response.referenceNumber);
        this.router.navigate(['/dashboard']);
        this.isTransferring = false;
      },
      error: (error) => {
        console.error('Transfer error:', error);
        alert('Transfer failed: ' + (error.error?.message || error.message || 'Unknown error'));
        this.isTransferring = false;
      }
    });
  }

  goBack() {
    this.router.navigate(['/dashboard']);
  }
}