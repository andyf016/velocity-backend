import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IResident, Resident } from 'app/shared/model/resident.model';
import { ResidentService } from './resident.service';
import { IRoom } from 'app/shared/model/room.model';
import { RoomService } from 'app/entities/room/room.service';

@Component({
  selector: 'jhi-resident-update',
  templateUrl: './resident-update.component.html',
})
export class ResidentUpdateComponent implements OnInit {
  isSaving = false;
  rooms: IRoom[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    room: [null, Validators.required],
  });

  constructor(
    protected residentService: ResidentService,
    protected roomService: RoomService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resident }) => {
      this.updateForm(resident);

      this.roomService.query().subscribe((res: HttpResponse<IRoom[]>) => (this.rooms = res.body || []));
    });
  }

  updateForm(resident: IResident): void {
    this.editForm.patchValue({
      id: resident.id,
      name: resident.name,
      room: resident.room,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const resident = this.createFromForm();
    if (resident.id !== undefined) {
      this.subscribeToSaveResponse(this.residentService.update(resident));
    } else {
      this.subscribeToSaveResponse(this.residentService.create(resident));
    }
  }

  private createFromForm(): IResident {
    return {
      ...new Resident(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      room: this.editForm.get(['room'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResident>>): void {
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

  trackById(index: number, item: IRoom): any {
    return item.id;
  }
}
