import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { Product } from './product.class';
import { tap } from 'rxjs/operators';
@Injectable({
  providedIn: 'root'
})
export class ProductsService {

    private static productslist: Product[] = null;

    private products$: BehaviorSubject<Product[]> = new BehaviorSubject<Product[]>([]);

    private _refreshNedeed$ = new Subject<void>() ;
    private readonly URL = "http://localhost:3000/api/products";

    constructor(private http: HttpClient) { }

    get refreshNedeed$(){
      return this._refreshNedeed$ ;
    }

    getProducts(): Observable<Product[]> {
       this.http.get<any>(this.URL).subscribe(data => {
                ProductsService.productslist = data;                
                this.products$.next(ProductsService.productslist);
            });
        return this.products$;
    }

    create(prod: Product): Observable<Product> {
       return this.http.post<Product>(this.URL, prod)
       .pipe(tap(() => {
           this._refreshNedeed$.next() ;
        }) 
       ); 
    }

    update(prod: Product): Observable<Product>{
        return this.http.put<Product>(this.URL, prod)
       .pipe(tap(() => {
           this._refreshNedeed$.next() ;
        })
       ); 
    }

    delete(id: number): Observable<Product>{
        return this.http.delete(this.URL + '/' + id)
        .pipe(tap(() => {
            this._refreshNedeed$.next() ;
         })
        );
    }
}