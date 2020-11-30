import { IFacility } from 'app/shared/model/facility.model';

export interface IRoom {
  id?: number;
  number?: number;
  facility?: IFacility;
}

export class Room implements IRoom {
  constructor(public id?: number, public number?: number, public facility?: IFacility) {}
}
