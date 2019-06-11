import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IUser, Principal, UserService, User } from 'app/core';
import { IProjectAcl, IUserProjectAcl, UserProjectAcl, ProjectAcl, ProjectRole } from 'app/shared/model/project-acl.model';
import { IProject } from 'app/shared/model/project.model';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { Subscription } from 'rxjs';
import { ProjectAclService } from './project-acl.service';
import { ITEMS_PER_PAGE } from 'app/shared';

@Component({
    selector: 'jhi-project-acl',
    templateUrl: './project-acl.component.html'
})
export class ProjectAclComponent implements OnInit, OnDestroy {
    project: IProject;
    userProjectAcls: IUserProjectAcl[];
    acls: IUserProjectAcl[];
    roles = ['PROJECT_USER', 'PROJECT_ADMIN'];

    currentAccount: any;
    eventSubscriber: Subscription;

    error: any;
    success: any;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;

    constructor(
        private activatedRoute: ActivatedRoute,
        private projectAclService: ProjectAclService,
        private userService: UserService,
        private parseLinks: JhiParseLinks,
        private router: Router,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
    }

    loadAll() {
        this.userService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<User[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpResponse<any>) => this.onError(res.body)
            );
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;

        this.userProjectAcls = [];
        data.forEach(user => {
            this.userProjectAcls.push(new UserProjectAcl(user, []));
        });

        this.activatedRoute.data.subscribe(({ project }) => {
            this.project = project;
            this.projectAclService.query(this.project.id).subscribe(
                (res: HttpResponse<IProjectAcl[]>) => {
                    res.body.forEach((projectAcl: IProjectAcl) => {
                        const idx = this.userProjectAcls.findIndex(item => {
                            return item.user.id === projectAcl.user.id;
                        });
                        if (idx !== -1) {
                            this.userProjectAcls[idx].acls.push(projectAcl);
                        }
                    });
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        });
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/project/' + this.project.id + '/project-acl'], {
            queryParams: {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
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

    deletePermission(projectAcl) {
        this.projectAclService.delete(projectAcl.id, this.project.id).subscribe(() => {
            this.eventManager.broadcast({
                name: 'projectAclListModification',
                content: 'Deleted an projectAcl'
            });
        });
    }

    addPermission(user: IUser, role: ProjectRole) {
        this.projectAclService.create(new ProjectAcl(null, role, this.project, user)).subscribe(() => {
            this.eventManager.broadcast({
                name: 'projectAclListModification',
                content: 'Deleted an projectAcl'
            });
        });
    }

    containsRole(role, roles): boolean {
        let result = false;
        for (let index = 0; index < roles.length; index++) {
            const element = roles[index].role;
            if (role === element) {
                result = true;
                break;
            }
        }
        return result;
    }
}
