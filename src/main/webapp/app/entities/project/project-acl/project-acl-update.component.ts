import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IUser, UserService } from 'app/core';
import { IProjectAcl } from 'app/shared/model/project-acl.model';
import { IProject } from 'app/shared/model/project.model';
import { JhiAlertService } from 'ng-jhipster';
import { Observable } from 'rxjs';
import { ProjectAclService } from './project-acl.service';

@Component({
    selector: 'jhi-project-acl-update',
    templateUrl: './project-acl-update.component.html'
})
export class ProjectAclUpdateComponent implements OnInit {
    projectAcl: IProjectAcl;
    isSaving: boolean;
    projects: IProject[] = [];
    users: IUser[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private projectAclService: ProjectAclService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ project, projectAcl }) => {
            this.projectAcl = projectAcl;
            this.projects.push(project);
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.projectAcl.id !== undefined) {
            this.subscribeToSaveResponse(this.projectAclService.update(this.projectAcl));
        } else {
            this.subscribeToSaveResponse(this.projectAclService.create(this.projectAcl));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IProjectAcl>>) {
        result.subscribe((res: HttpResponse<IProjectAcl>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackProjectById(index: number, item: IProject) {
        return item.id;
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
}
