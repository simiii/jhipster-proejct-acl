import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DefaultSharedModule } from 'app/shared';
import {
    ProjectAclComponent,
    ProjectAclDetailComponent,
    ProjectAclUpdateComponent,
    ProjectAclDeletePopupComponent,
    ProjectAclDeleteDialogComponent,
    ProjectComponent,
    ProjectDetailComponent,
    ProjectUpdateComponent,
    ProjectDeletePopupComponent,
    ProjectDeleteDialogComponent,
    projectRoute,
    projectPopupRoute
} from './';
import { projectAclPopupRoute } from './project.route';

const ENTITY_STATES = [...projectRoute, ...projectPopupRoute, ...projectAclPopupRoute];

@NgModule({
    imports: [DefaultSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ProjectAclComponent,
        ProjectAclDetailComponent,
        ProjectAclUpdateComponent,
        ProjectAclDeletePopupComponent,
        ProjectAclDeleteDialogComponent,
        ProjectComponent,
        ProjectDetailComponent,
        ProjectUpdateComponent,
        ProjectDeleteDialogComponent,
        ProjectDeletePopupComponent
    ],
    entryComponents: [
        ProjectAclComponent,
        ProjectAclDetailComponent,
        ProjectAclUpdateComponent,
        ProjectAclDeletePopupComponent,
        ProjectAclDeleteDialogComponent,
        ProjectComponent,
        ProjectUpdateComponent,
        ProjectDeleteDialogComponent,
        ProjectDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DefaultProjectModule {}
