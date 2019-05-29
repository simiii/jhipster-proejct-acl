import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Project } from 'app/shared/model/project.model';
import { ProjectService } from './project.service';
import { ProjectComponent } from './project.component';
import { ProjectDetailComponent } from './project-detail.component';
import { ProjectUpdateComponent } from './project-update.component';
import { ProjectDeletePopupComponent } from './project-delete-dialog.component';
import { IProject } from 'app/shared/model/project.model';
import { IProjectAcl, ProjectAcl } from 'app/shared/model/project-acl.model';
import { ProjectAclService, ProjectAclComponent, ProjectAclDetailComponent, ProjectAclDeletePopupComponent } from '.';
import { ProjectAclUpdateComponent } from './project-acl/project-acl-update.component';

@Injectable({ providedIn: 'root' })
export class ProjectResolve implements Resolve<IProject> {
    constructor(private service: ProjectService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((project: HttpResponse<Project>) => project.body));
        }
        return of(new Project());
    }
}

@Injectable({ providedIn: 'root' })
export class ProjectAclResolve implements Resolve<IProjectAcl> {
    constructor(private service: ProjectAclService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        const aclId = route.params['aclId'] ? route.params['aclId'] : null;
        if (id && aclId) {
            return this.service.find(aclId, id).pipe(map((projectAcl: HttpResponse<ProjectAcl>) => projectAcl.body));
        }
        return of(new ProjectAcl());
    }
}

export const projectRoute: Routes = [
    {
        path: 'project',
        component: ProjectComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'defaultApp.project.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'project/:id/view',
        component: ProjectDetailComponent,
        resolve: {
            project: ProjectResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'defaultApp.project.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'project/new',
        component: ProjectUpdateComponent,
        resolve: {
            project: ProjectResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'defaultApp.project.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'project/:id/edit',
        component: ProjectUpdateComponent,
        resolve: {
            project: ProjectResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'defaultApp.project.home.title'
        },
        canActivate: [UserRouteAccessService]
    },

    {
        path: 'project/:id/project-acl',
        component: ProjectAclComponent,
        resolve: {
            project: ProjectResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'defaultApp.projectAcl.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'project/:id/project-acl/:aclId/view',
        component: ProjectAclDetailComponent,
        resolve: {
            project: ProjectResolve,
            projectAcl: ProjectAclResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'defaultApp.projectAcl.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'project/:id/project-acl/:aclId/edit',
        component: ProjectAclUpdateComponent,
        resolve: {
            project: ProjectResolve,
            projectAcl: ProjectAclResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'defaultApp.projectAcl.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'project/:id/project-acl/new',
        component: ProjectAclUpdateComponent,
        resolve: {
            project: ProjectResolve,
            projectAcl: ProjectAclResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'defaultApp.projectAcl.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const projectPopupRoute: Routes = [
    {
        path: 'project/:id/delete',
        component: ProjectDeletePopupComponent,
        resolve: {
            project: ProjectResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'defaultApp.project.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

export const projectAclPopupRoute: Routes = [
    {
        path: 'project/:id/project-acl/:aclId/delete',
        component: ProjectAclDeletePopupComponent,
        resolve: {
            projectAcl: ProjectAclResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'defaultApp.projectAcl.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
