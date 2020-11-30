import { IRoom } from 'app/shared/model/room.model';
import { IUser } from 'app/core/user/user.model';

export interface IFacility {
  id?: number;
  name?: string;
  rooms?: IRoom[];
  user?: IUser;
}

export class Facility implements IFacility {
  constructor(public id?: number, public name?: string, public rooms?: IRoom[], public user?: IUser) {}
}
