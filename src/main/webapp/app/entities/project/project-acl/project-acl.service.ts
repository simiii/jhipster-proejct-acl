import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IProjectAcl } from 'app/shared/model/project-acl.model';
import { Observable } from 'rxjs';

type EntityResponseType = HttpResponse<IProjectAcl>;
type EntityArrayResponseType = HttpResponse<IProjectAcl[]>;

@Injectable({ providedIn: 'root' })
export class ProjectAclService {
    public resourceUrl = SERVER_API_URL + 'api/project-acls';

    constructor(private http: HttpClient) {}

    create(projectAcl: IProjectAcl): Observable<EntityResponseType> {
        return this.http.post<IProjectAcl>(this.resourceUrl, projectAcl, {
            observe: 'response',
            headers: new HttpHeaders({ 'X-PROJECT-ID': `${projectAcl.project.id}` })
        });
    }

    update(projectAcl: IProjectAcl): Observable<EntityResponseType> {
        return this.http.put<IProjectAcl>(this.resourceUrl, projectAcl, {
            observe: 'response',
            headers: new HttpHeaders({ 'X-PROJECT-ID': `${projectAcl.project.id}` })
        });
    }

    find(id: number, projectId: number): Observable<EntityResponseType> {
        return this.http.get<IProjectAcl>(`${this.resourceUrl}/${id}`, {
            observe: 'response',
            headers: new HttpHeaders({ 'X-PROJECT-ID': `${projectId}` })
        });
    }

    query(projectId: number, req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IProjectAcl[]>(this.resourceUrl, {
            params: options,
            observe: 'response',
            headers: new HttpHeaders({ 'X-PROJECT-ID': `${projectId}` })
        });
    }

    delete(id: number, projectId: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, {
            observe: 'response',
            headers: new HttpHeaders({ 'X-PROJECT-ID': `${projectId}` })
        });
    }
}
