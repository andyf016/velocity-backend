import { IRoom } from 'app/shared/model/room.model';

export interface IResident {
  id?: number;
  name?: string;
  room?: IRoom;
}

export class Resident implements IResident {
  constructor(public id?: number, public name?: string, public room?: IRoom) {}
}
