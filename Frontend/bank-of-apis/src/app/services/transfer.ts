import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Auth } from './auth';

@Injectable({
  providedIn: 'root'
})
export class TransferService {  // Changed from 'Transfer' to 'TransferService'
  private apiUrl = 'http://localhost:8080/api/transfers';

  constructor(private http: HttpClient, private authVariable: Auth) { }

  private getHeaders(): HttpHeaders {
    const token = this.authVariable.getToken();
    const userId = this.authVariable.getUserId();
    
    let headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }

    if (userId) {
      headers = headers.set('X-User-ID', userId.toString());
    }

    return headers;
  }

  transferFunds(transferData: any): Observable<any> {
    // Convert string account IDs to numbers for backend
    const payload = {
      fromAccountId: Number(transferData.fromAccountId),
      toAccountId: Number(transferData.toAccountId),
      amount: Number(transferData.amount),
      description: transferData.remarks || ''
    };
    
    return this.http.post<any>(this.apiUrl, payload, { headers: this.getHeaders() });
  }

  getTransferById(transferId: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${transferId}`, { headers: this.getHeaders() });
  }

  getAllTransfers(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl, { headers: this.getHeaders() });
  }
}