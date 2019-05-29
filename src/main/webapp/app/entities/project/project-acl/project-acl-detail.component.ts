import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProjectAcl } from 'app/shared/model/project-acl.model';

@Component({
    selector: 'jhi-project-acl-detail',
    templateUrl: './project-acl-detail.component.html'
})
export class ProjectAclDetailComponent implements OnInit {
    projectAcl: IProjectAcl;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ projectAcl }) => {
            this.projectAcl = projectAcl;
        });
    }

    previousState() {
        window.history.back();
    }
}
