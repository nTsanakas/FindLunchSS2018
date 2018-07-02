import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { ChooseLoginPage } from './choose-login';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [
    ChooseLoginPage,
  ],
  imports: [
    IonicPageModule.forChild(ChooseLoginPage),
    SharedModule,
  ],
})
export class ChooseLoginPageModule {}
