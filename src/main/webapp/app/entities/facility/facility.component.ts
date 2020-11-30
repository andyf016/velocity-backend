import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFacility } from 'app/shared/model/facility.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { FacilityService } from './facility.service';
import { FacilityDeleteDialogComponent } from './facility-delete-dialog.component';

@Component({
  selector: 'jhi-facility',
  templateUrl: './facility.component.html',
})
export class FacilityComponent implements OnInit, OnDestroy {
  facilities: IFacility[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected facilityService: FacilityService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.facilities = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.facilityService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IFacility[]>) => this.paginateFacilities(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.facilities = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInFacilities();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IFacility): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInFacilities(): void {
    this.eventSubscriber = this.eventManager.subscribe('facilityListModification', () => this.reset());
  }

  delete(facility: IFacility): void {
    const modalRef = this.modalService.open(FacilityDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.facility = facility;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateFacilities(data: IFacility[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.facilities.push(data[i]);
      }
    }
  }
}
