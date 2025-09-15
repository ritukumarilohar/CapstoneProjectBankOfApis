import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Auth } from './auth';

// Define interfaces for type safety
export interface AccountDTO {
  id: number;           // Changed from accountId to match backend
  accountNumber: string;
  accountType: string;
  balance: number;
  bankName: string;
  status?: string;
  userId?: number;
}

// Add this interface for backend response mapping
interface BackendAccountDTO {
  id: number;
  accountNumber: string;
  accountType: string;
  balance: number;
  bankName: string;
  status?: string;
}

export interface TransactionDTO {
  id?: number;          // Changed from transactionId to match backend
  accountId: number;
  transactionType: string;
  amount: number;
  date: string;
  status: string;
  description?: string;
  toAccountId?: number; // Added to match backend
  toAccountNumber?: string; 
}

export interface StatementDTO {
  id?: number;          
  accountId: number;
  startDate: string;
  endDate: string;
  statementPeriod?: string; 
  openingBalance?: number;  
  closingBalance?: number;  
  generatedDate: string;
  transactions: TransactionDTO[];
  downloadUrl?: string;     
}

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private baseUrl = 'http://localhost:8080/api/accounts';
  
  constructor(private http: HttpClient, private authVariable: Auth) {}

  private getHeaders(): HttpHeaders {
    const token = this.authVariable.getToken();
    const userId = this.authVariable.getUserId();
    
    let headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }

    if (userId && userId !== null) {
      headers = headers.set('X-User-ID', userId.toString());
    }

    return headers;
  }

  // UPDATED: Map backend response to frontend expected format
  getAccounts(): Observable<AccountDTO[]> {
    return this.http.get<BackendAccountDTO[]>(`${this.baseUrl}`, { 
      headers: this.getHeaders() 
    }).pipe(
      map(accounts => accounts.map(account => ({
        id: account.id,
        accountNumber: account.accountNumber,
        accountType: account.accountType,
        balance: account.balance,
        bankName: account.bankName,
        status: account.status
      })))
    );
  }

  // Get specific account details
  getAccountById(accountId: number): Observable<AccountDTO> {
    return this.http.get<BackendAccountDTO>(`${this.baseUrl}/${accountId}`, { 
      headers: this.getHeaders() 
    }).pipe(
      map(account => ({
        id: account.id,
        accountNumber: account.accountNumber,
        accountType: account.accountType,
        balance: account.balance,
        bankName: account.bankName,
        status: account.status
      }))
    );
  }

  // Get account transactions - UPDATED to match backend response
  getAccountTransactions(accountId: number): Observable<TransactionDTO[]> {
    return this.http.get<any[]>(`${this.baseUrl}/${accountId}/transactions`, { 
      headers: this.getHeaders() 
    }).pipe(
      map(transactions => transactions.map(transaction => ({
        id: transaction.id,
        accountId: transaction.accountId,
        transactionType: transaction.transactionType,
        amount: transaction.amount,
        date: transaction.date,
        status: transaction.status,
        description: transaction.description,
        toAccountId: transaction.toAccountId,
        toAccountNumber: transaction.toAccountNumber
      })))
    );
  }

  // Get account statements - UPDATED to match backend
  getAccountStatements(accountId: number, startDate?: string, endDate?: string): Observable<TransactionDTO[]> {
    let url = `${this.baseUrl}/${accountId}/statements`;
    const params: string[] = [];
    
    if (startDate) params.push(`startDate=${startDate}`);
    if (endDate) params.push(`endDate=${endDate}`);
    
    if (params.length > 0) {
      url += '?' + params.join('&');
    }
    
    return this.http.get<any[]>(url, { 
      headers: this.getHeaders() 
    }).pipe(
      map(transactions => transactions.map(transaction => ({
        id: transaction.id,
        accountId: transaction.accountId,
        transactionType: transaction.transactionType,
        amount: transaction.amount,
        date: transaction.date,
        status: transaction.status,
        description: transaction.description,
        toAccountId: transaction.toAccountId,
        toAccountNumber: transaction.toAccountNumber
      })))
    );
  }

  // Get statement history - UPDATED to match backend
  getAccountStatementHistory(accountId: number): Observable<StatementDTO[]> {
    return this.http.get<any[]>(`${this.baseUrl}/${accountId}/statement-history`, { 
      headers: this.getHeaders() 
    }).pipe(
      map(statements => statements.map(statement => ({
        id: statement.id,
        accountId: statement.accountId,
        startDate: statement.startDate,
        endDate: statement.endDate,
        statementPeriod: statement.statementPeriod,
        openingBalance: statement.openingBalance,
        closingBalance: statement.closingBalance,
        generatedDate: statement.generatedDate || new Date().toISOString(),
        status: statement.status,
        downloadUrl: statement.downloadUrl,
        transactions: statement.transactions || []
      })))
    );
  }

  // Create new account - UPDATED to match backend expected format
  createAccount(accountData: Partial<AccountDTO>): Observable<AccountDTO> {
    const backendAccountData = {
      accountNumber: accountData.accountNumber,
      accountType: accountData.accountType,
      balance: accountData.balance,
      bankName: accountData.bankName,
      status: accountData.status || 'ACTIVE'
    };

    return this.http.post<BackendAccountDTO>(this.baseUrl, backendAccountData, {
      headers: this.getHeaders()
    }).pipe(
      map(account => ({
        id: account.id,
        accountNumber: account.accountNumber,
        accountType: account.accountType,
        balance: account.balance,
        bankName: account.bankName,
        status: account.status
      }))
    );
  }

  // Create new transaction - UPDATED to match backend expected format
  createTransaction(transactionData: Partial<TransactionDTO>): Observable<TransactionDTO> {
    const backendTransactionData = {
      amount: transactionData.amount,
      transactionType: transactionData.transactionType,
      description: transactionData.description,
      accountId: transactionData.accountId,
      toAccountId: transactionData.toAccountId
    };

    return this.http.post<any>(`${this.baseUrl}/transactions, backendTransactionData`, { 
      headers: this.getHeaders() 
    }).pipe(
      map(transaction => ({
        id: transaction.id,
        accountId: transaction.accountId,
        transactionType: transaction.transactionType,
        amount: transaction.amount,
        date: transaction.date,
        status: transaction.status,
        description: transaction.description,
        toAccountId: transaction.toAccountId,
        toAccountNumber: transaction.toAccountNumber
      }))
    );
  }

  // Generate statement - UPDATED to match backend
  generateStatement(accountId: number, startDate: string, endDate: string): Observable<StatementDTO> {
    const params = startDate=`${startDate}&endDate=${endDate}`;
    return this.http.post<any>(`${this.baseUrl}/${accountId}/generate-statement?${params}`, {}, { 
      headers: this.getHeaders() 
    }).pipe(
      map(statement => ({
        id: statement.id,
        accountId: statement.accountId,
        startDate: statement.startDate,
        endDate: statement.endDate,
        statementPeriod: statement.statementPeriod,
        openingBalance: statement.openingBalance,
        closingBalance: statement.closingBalance,
        generatedDate: new Date().toISOString(),
        status: statement.status,
        downloadUrl: statement.downloadUrl,
        transactions: []
      }))
    );
  }

  // Download statement - UPDATED to match backend
  downloadStatement(statementId: number, format: string = 'pdf'): Observable<{ downloadUrl: string }> {
    return this.http.get<{ downloadUrl: string }>(`${this.baseUrl}/statements/${statementId}/download?format=${format}`, { 
      headers: this.getHeaders() 
    });
  }
}