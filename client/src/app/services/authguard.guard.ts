import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import { SessaoService } from './sessao.service';

@Injectable()
export class AuthguardGuard implements CanActivate {
	constructor(private sessaoService: SessaoService, private router: Router) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
      if(sessionStorage.getItem("sessaoGMETRICA") || this.sessaoService.sessao.logado){
          return true;
      }
      this.router.navigate(['/login']);
      return false;
      // return true;
    }
}