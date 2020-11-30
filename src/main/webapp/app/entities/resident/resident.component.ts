import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IResident } from 'app/shared/model/resident.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ResidentService } from './resident.service';
import { ResidentDeleteDialogComponent } from './resident-delete-dialog.component';

@Component({
  selector: 'jhi-resident',
  templateUrl: './resident.component.html',
})
export class ResidentComponent implements OnInit, OnDestroy {
  residents: IResident[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected residentService: ResidentService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.residents = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.residentService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IResident[]>) => this.paginateResidents(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.residents = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInResidents();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IResident): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInResidents(): void {
    this.eventSubscriber = this.eventManager.subscribe('residentListModification', () => this.reset());
  }

  delete(resident: IResident): void {
    const modalRef = this.modalService.open(ResidentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.resident = resident;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateResidents(data: IResident[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.residents.push(data[i]);
      }
    }
  }
}
