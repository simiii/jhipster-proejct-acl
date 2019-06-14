package de.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.app.domain.Project;
import de.app.repository.ProjectRepository;
import de.app.security.AuthoritiesConstants;
import de.app.security.SecurityUtils;
import de.app.service.ProjectService;
import de.app.web.rest.errors.BadRequestAlertException;
import de.app.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Project.
 */
@RestController
@RequestMapping("/api")
public class ProjectResource {

    private final Logger log = LoggerFactory.getLogger(ProjectResource.class);

    private static final String ENTITY_NAME = "project";

    private ProjectRepository projectRepository;

    private ProjectService projectService;

    public ProjectResource(ProjectRepository projectRepository, ProjectService projectService) {
        this.projectRepository = projectRepository;
        this.projectService = projectService;
    }

    /**
     * POST /projects : Create a new project.
     *
     * @param project the project to create
     * @return the ResponseEntity with status 201 (Created) and with body the new project, or with status 400 (Bad Request) if the project has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/projects")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    @Timed
    public ResponseEntity<Project> createProject(@Valid @RequestBody Project project) throws URISyntaxException {
        log.debug("REST request to save Project : {}", project);
        if (project.getId() != null) {
            throw new BadRequestAlertException("A new project cannot already have an ID", ENTITY_NAME, "idexists");
        }
        final Project result = projectService.saveWithAcl(project);

        return ResponseEntity.created(new URI("/api/projects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId()
                .toString()))
            .body(result);
    }

    /**
     * PUT /projects : Updates an existing project.
     *
     * @param project the project to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated project,
     * or with status 400 (Bad Request) if the project is not valid,
     * or with status 500 (Internal Server Error) if the project couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/projects")
    @PreAuthorize("@securityService.hasAccess(#project, {'PROJECT_ADMIN'})")
    @Timed
    public ResponseEntity<Project> updateProject(@Valid @RequestBody Project project) throws URISyntaxException {
        log.debug("REST request to update Project : {}", project);
        if (project.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        final Project result = projectRepository.save(project);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, project.getId()
                .toString()))
            .body(result);
    }

    /**
     * GET /projects : get all the projects.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of projects in body
     */
    @GetMapping("/projects")
    @Timed
    public List<Project> getAllProjects() {
        log.debug("REST request to get all Projects");
        final Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        return projectRepository.findDistinctByAclsUserLogin(currentUserLogin.get());
    }

    /**
     * GET /projects/:id : get the "id" project.
     *
     * @param id the id of the project to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the project, or with status 404 (Not Found)
     */
    @GetMapping("/projects/{id}")
    @PreAuthorize("@securityService.hasAccess(#id, {'PROJECT_USER'})")
    @Timed
    public ResponseEntity<Project> getProject(@PathVariable Long id) {
        log.debug("REST request to get Project : {}", id);
        final Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        final Optional<Project> project = projectRepository.findByIdAndAclsUserLogin(id, currentUserLogin.get());
        return ResponseUtil.wrapOrNotFound(project);
    }

    /**
     * DELETE /projects/:id : delete the "id" project.
     *
     * @param id the id of the project to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/projects/{id}")
    @PreAuthorize("@securityService.hasAccess(#id, {'PROJECT_ADMIN'})")
    @Timed
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        log.debug("REST request to delete Project : {}", id);

        final Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();

        projectRepository.deleteByIdAndAclsUserLogin(id, currentUserLogin.get());
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString()))
            .build();
    }
}
