import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProjectAcl } from 'app/shared/model/project-acl.model';
import { ProjectAclService } from './project-acl.service';

@Component({
    selector: 'jhi-project-acl-delete-dialog',
    templateUrl: './project-acl-delete-dialog.component.html'
})
export class ProjectAclDeleteDialogComponent {
    projectAcl: IProjectAcl;

    constructor(private projectAclService: ProjectAclService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.projectAclService.delete(id, this.projectAcl.project.id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'projectAclListModification',
                content: 'Deleted an projectAcl'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-project-acl-delete-popup',
    template: ''
})
export class ProjectAclDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ projectAcl }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ProjectAclDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.projectAcl = projectAcl;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
