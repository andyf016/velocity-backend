import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { RoomassnSharedModule } from 'app/shared/shared.module';
import { RoomassnCoreModule } from 'app/core/core.module';
import { RoomassnAppRoutingModule } from './app-routing.module';
import { RoomassnHomeModule } from './home/home.module';
import { RoomassnEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    RoomassnSharedModule,
    RoomassnCoreModule,
    RoomassnHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    RoomassnEntityModule,
    RoomassnAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent],
})
export class RoomassnAppModule {}
