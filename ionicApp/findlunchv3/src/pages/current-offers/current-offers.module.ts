import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { CurrentOffersPage} from "./current-offers";

@NgModule({
  declarations: [
    CurrentOffersPage,
  ],
  imports: [
    IonicPageModule.forChild(CurrentOffersPage),
  ],
})
export class CurrentOffersPageModule {}
