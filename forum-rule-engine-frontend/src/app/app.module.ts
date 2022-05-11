import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MaterialModule } from './material-module';
import { HelpDialogComponent } from './components/help-dialog/help-dialog.component';
import { MainPageComponent } from './components/main-page/main-page.component';
import { ErrorPageComponent } from './components/error-page/error-page.component';
import { AvatarModule } from 'ngx-avatar';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    AppComponent,
    HelpDialogComponent,
    MainPageComponent,
    ErrorPageComponent,
  ],
  imports: [
    MaterialModule,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    HttpClientModule,
    AvatarModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
