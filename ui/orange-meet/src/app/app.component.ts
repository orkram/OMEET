import {Component} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {library} from '@fortawesome/fontawesome-svg-core';
import {faTrashAlt} from '@fortawesome/free-solid-svg-icons/faTrashAlt';
import {faPencilAlt} from '@fortawesome/free-solid-svg-icons';

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

    library.add(faTrashAlt, faPencilAlt);
  }
}
