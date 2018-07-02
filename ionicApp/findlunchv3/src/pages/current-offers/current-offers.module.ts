import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { CurrentOffersPage} from "./current-offers";
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [
    CurrentOffersPage,
  ],
  imports: [
    IonicPageModule.forChild(CurrentOffersPage),
    SharedModule,
  ],
})
export class CurrentOffersPageModule {}
