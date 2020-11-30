import { IRoom } from 'app/shared/model/room.model';

export interface IFacility {
  id?: number;
  name?: string;
  rooms?: IRoom[];
}

export class Facility implements IFacility {
  constructor(public id?: number, public name?: string, public rooms?: IRoom[]) {}
}
