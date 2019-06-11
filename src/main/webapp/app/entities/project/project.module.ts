import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DefaultSharedModule } from 'app/shared';
import {
    ProjectAclComponent,
    ProjectComponent,
    ProjectDetailComponent,
    ProjectUpdateComponent,
    ProjectDeletePopupComponent,
    ProjectDeleteDialogComponent,
    projectRoute,
    projectPopupRoute
} from './';

const ENTITY_STATES = [...projectRoute, ...projectPopupRoute];

@NgModule({
    imports: [DefaultSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ProjectAclComponent,
        ProjectComponent,
        ProjectDetailComponent,
        ProjectUpdateComponent,
        ProjectDeleteDialogComponent,
        ProjectDeletePopupComponent
    ],
    entryComponents: [
        ProjectAclComponent,
        ProjectComponent,
        ProjectUpdateComponent,
        ProjectDeleteDialogComponent,
        ProjectDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DefaultProjectModule {}
