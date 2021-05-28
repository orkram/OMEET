import {TranslateService} from '@ngx-translate/core';
import {MatPaginatorIntl} from '@angular/material/paginator';

export class PaginatorIntlService extends MatPaginatorIntl {

  constructor(private translate: TranslateService) {
    super();
  }
  itemsPerPageLabel = 'Items per page: ';
  nextPageLabel     = 'Next page! ';
  previousPageLabel = 'Previous page!';

  getRangeLabel = (page: number, pageSize: number, length: number): string => {
    this.translateLabels();
    const of = this.translate ? this.translate.instant('paginator.of') : 'of';
    if (length === 0 || pageSize === 0) {
      return '0 ' + of + ' ' + length;
    }
    length = Math.max(length, 0);
    const startIndex = page * pageSize;
    const endIndex = startIndex < length ?
      Math.min(startIndex + pageSize, length) :
      startIndex + pageSize;
    return startIndex + 1 + ' - ' + endIndex + ' ' + of + ' ' + length;
  }

  injectTranslateService(translate: TranslateService): void {
    this.translate = translate;

    this.translate.onLangChange.subscribe(() => {
      this.translateLabels();
    });

    this.translateLabels();
  }

  translateLabels(): void {
    this.itemsPerPageLabel = this.translate.instant('paginator.items_per_page');
    this.nextPageLabel = this.translate.instant('paginator.next_page');
    this.previousPageLabel = this.translate.instant('paginator.previous_page');
    this.changes.next();
  }

}
