import {Component} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'orange-meet';

  constructor(public translate: TranslateService) {
    translate.addLangs(['en', 'pl']);
    const lang = localStorage.getItem('appLanguage');
    translate.use(lang ? lang : 'en' );
  }
}
