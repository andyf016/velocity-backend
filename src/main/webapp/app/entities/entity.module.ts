import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'facility',
        loadChildren: () => import('./facility/facility.module').then(m => m.RoomassnFacilityModule),
      },
      {
        path: 'room',
        loadChildren: () => import('./room/room.module').then(m => m.RoomassnRoomModule),
      },
      {
        path: 'resident',
        loadChildren: () => import('./resident/resident.module').then(m => m.RoomassnResidentModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class RoomassnEntityModule {}
