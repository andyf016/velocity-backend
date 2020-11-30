import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IRoom, Room } from 'app/shared/model/room.model';
import { RoomService } from './room.service';
import { IFacility } from 'app/shared/model/facility.model';
import { FacilityService } from 'app/entities/facility/facility.service';

@Component({
  selector: 'jhi-room-update',
  templateUrl: './room-update.component.html',
})
export class RoomUpdateComponent implements OnInit {
  isSaving = false;
  facilities: IFacility[] = [];

  editForm = this.fb.group({
    id: [],
    number: [],
    facility: [],
  });

  constructor(
    protected roomService: RoomService,
    protected facilityService: FacilityService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ room }) => {
      this.updateForm(room);

      this.facilityService.query().subscribe((res: HttpResponse<IFacility[]>) => (this.facilities = res.body || []));
    });
  }

  updateForm(room: IRoom): void {
    this.editForm.patchValue({
      id: room.id,
      number: room.number,
      facility: room.facility,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const room = this.createFromForm();
    if (room.id !== undefined) {
      this.subscribeToSaveResponse(this.roomService.update(room));
    } else {
      this.subscribeToSaveResponse(this.roomService.create(room));
    }
  }

  private createFromForm(): IRoom {
    return {
      ...new Room(),
      id: this.editForm.get(['id'])!.value,
      number: this.editForm.get(['number'])!.value,
      facility: this.editForm.get(['facility'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoom>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IFacility): any {
    return item.id;
  }
}
