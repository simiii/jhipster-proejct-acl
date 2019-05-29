import { IProject } from 'app/shared/model//project.model';
import { IUser } from 'app/core/user/user.model';

export const enum ProjectRole {
    PROJECT_USER = 'PROJECT_USER',
    PROJECT_ADMIN = 'PROJECT_ADMIN'
}

export interface IUserProjectAcl {
    user?: IUser;
    acls?: IProjectAcl[];
}

export class UserProjectAcl implements IUserProjectAcl {
    constructor(public user?: IUser, public acls?: IUserProjectAcl[]) {}
}

export interface IProjectAcl {
    id?: number;
    role?: ProjectRole;
    project?: IProject;
    user?: IUser;
}

export class ProjectAcl implements IProjectAcl {
    constructor(public id?: number, public role?: ProjectRole, public project?: IProject, public user?: IUser) {}
}
