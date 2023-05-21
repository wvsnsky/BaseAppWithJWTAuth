import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HomeRoutingModule } from './home-routing.module';
import {RouterModule} from "@angular/router";
import {HomeComponent} from "../../components/home/home.component";
import {BarsComponent} from "../../components/bars/bars.component";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatListModule, MatNavList} from "@angular/material/list";
import {MatButtonModule} from "@angular/material/button";


@NgModule({
  declarations: [
    HomeComponent,
    BarsComponent
  ],
  imports: [
    RouterModule,
    CommonModule,
    HomeRoutingModule,
    MatToolbarModule,
    MatIconModule,
    MatSidenavModule,
    MatListModule,
    MatButtonModule
  ]
})
export class HomeModule { }
