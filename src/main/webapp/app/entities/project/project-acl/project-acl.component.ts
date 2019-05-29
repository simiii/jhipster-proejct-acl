import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Principal } from 'app/core';
import { IProjectAcl, IUserProjectAcl, UserProjectAcl } from 'app/shared/model/project-acl.model';
import { IProject } from 'app/shared/model/project.model';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { Subscription } from 'rxjs';
import { ProjectAclService } from './project-acl.service';

@Component({
    selector: 'jhi-project-acl',
    templateUrl: './project-acl.component.html'
})
export class ProjectAclComponent implements OnInit, OnDestroy {
    project: IProject;
    userProjectAcls: IUserProjectAcl[];
    acls: IUserProjectAcl[];

    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private activatedRoute: ActivatedRoute,
        private projectAclService: ProjectAclService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.userProjectAcls = [];
        this.activatedRoute.data.subscribe(({ project }) => {
            this.project = project;
            this.projectAclService.query(this.project.id).subscribe(
                (res: HttpResponse<IProjectAcl[]>) => {
                    res.body.forEach((projectAcl: IProjectAcl) => {
                        const idx = this.userProjectAcls.findIndex(item => {
                            return item.user.id === projectAcl.user.id;
                        });
                        idx !== -1
                            ? this.userProjectAcls[idx].acls.push(projectAcl)
                            : this.userProjectAcls.push(new UserProjectAcl(projectAcl.user, [projectAcl]));
                    });
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        });
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInProjectAcls();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IProjectAcl) {
        return item.id;
    }

    registerChangeInProjectAcls() {
        this.eventSubscriber = this.eventManager.subscribe('projectAclListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    addToProjectAcl(projectAcl, evt) {}
}
