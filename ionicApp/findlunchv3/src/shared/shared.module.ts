import { TranslateModule } from "@ngx-translate/core";
import { NgModule } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { TranslateHttpLoader } from "@ngx-translate/http-loader";

@NgModule({
  imports:      [ TranslateModule.forChild() ],
  exports:      [ TranslateModule ]
})
export class SharedModule { }

export function createTranslateLoader(http: HttpClient): TranslateHttpLoader{
    return new TranslateHttpLoader(http, './assets/i18n/', '.json')
}