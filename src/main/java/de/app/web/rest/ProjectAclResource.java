package de.app.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import de.app.domain.ProjectAcl;
import de.app.repository.ProjectAclRepository;
import de.app.web.rest.errors.BadRequestAlertException;
import de.app.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing ProjectAcl.
 */
@RestController
@RequestMapping("/api")
public class ProjectAclResource
{

	private final Logger			log			= LoggerFactory.getLogger(ProjectAclResource.class);

	private static final String		ENTITY_NAME	= "projectAcl";

	private ProjectAclRepository	projectAclRepository;

	public ProjectAclResource(ProjectAclRepository projectAclRepository)
	{
		this.projectAclRepository = projectAclRepository;
	}

	/**
	 * POST /project-acls : Create a new projectAcl.
	 *
	 * @param projectAcl
	 *            the projectAcl to create
	 * @param projectId
	 *            the id of the project
	 * @return the ResponseEntity with status 201 (Created) and with body the new projectAcl, or with status 400 (Bad Request) if the projectAcl has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/project-acls")
	@PreAuthorize("@securityService.hasAccess(#projectId, {'PROJECT_ADMIN'})")
	@Timed
	public ResponseEntity<ProjectAcl> createProjectAcl(@Valid @RequestBody ProjectAcl projectAcl, @RequestHeader(value = "X-PROJECT-ID", required = true) Long projectId) throws URISyntaxException {
		log.debug("REST request to save ProjectAcl : {}", projectAcl);
		if (projectAcl.getId() != null) {
			throw new BadRequestAlertException("A new projectAcl cannot already have an ID", ENTITY_NAME, "idexists");
		}
		ProjectAcl result = projectAclRepository.save(projectAcl);
		return ResponseEntity.created(new URI("/api/project-acls/" + result.getId()))
			.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId()
				.toString()))
			.body(result);
	}

	/**
	 * PUT /project-acls : Updates an existing projectAcl.
	 *
	 * @param projectAcl
	 *            the projectAcl to update
	 * @param projectId
	 *            the id of the project
	 * @return the ResponseEntity with status 200 (OK) and with body the updated projectAcl,
	 *         or with status 400 (Bad Request) if the projectAcl is not valid,
	 *         or with status 500 (Internal Server Error) if the projectAcl couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/project-acls")
	@PreAuthorize("@securityService.hasAccess(#projectId, {'PROJECT_ADMIN'})")
	@Timed
	public ResponseEntity<ProjectAcl> updateProjectAcl(@Valid @RequestBody ProjectAcl projectAcl, @RequestHeader(value = "X-PROJECT-ID", required = true) Long projectId) throws URISyntaxException {
		log.debug("REST request to update ProjectAcl : {}", projectAcl);
		if (projectAcl.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		ProjectAcl result = projectAclRepository.save(projectAcl);
		return ResponseEntity.ok()
			.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, projectAcl.getId()
				.toString()))
			.body(result);
	}

	/**
	 * GET /project-acls : get all the projectAcls.
	 * 
	 * @param projectId
	 *            the id of the project
	 * @return the ResponseEntity with status 200 (OK) and the list of projectAcls in body
	 */
	@GetMapping("/project-acls")
	@PreAuthorize("@securityService.hasAccess(#projectId, {'PROJECT_ADMIN'})")
	@Timed
	public List<ProjectAcl> getAllProjectAcls(@RequestHeader(value = "X-PROJECT-ID", required = true) Long projectId) {
		log.debug("REST request to get all ProjectAcls");
		return projectAclRepository.findAll();
	}

	/**
	 * GET /project-acls/:id : get the "id" projectAcl.
	 *
	 * @param id
	 *            the id of the projectAcl to retrieve
	 * @param projectId
	 *            the id of the project
	 * @return the ResponseEntity with status 200 (OK) and with body the projectAcl, or with status 404 (Not Found)
	 */
	@GetMapping("/project-acls/{projectAclId}")
	@PreAuthorize("@securityService.hasAccess(#projectId, {'PROJECT_ADMIN'})")
	@Timed
	public ResponseEntity<ProjectAcl> getProjectAcl(@PathVariable Long projectAclId, @RequestHeader(value = "X-PROJECT-ID", required = true) Long projectId) {
		log.debug("REST request to get ProjectAcl : {}", projectAclId);
		Optional<ProjectAcl> projectAcl = projectAclRepository.findById(projectAclId);
		return ResponseUtil.wrapOrNotFound(projectAcl);
	}

	/**
	 * DELETE /project-acls/:id : delete the "id" projectAcl.
	 *
	 * @param id
	 *            the id of the projectAcl to delete
	 * @param projectId
	 *            the id of the project
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/project-acls/{projectAclId}")
	@PreAuthorize("@securityService.hasAccess(#projectId, {'PROJECT_ADMIN'})")
	@Timed
	public ResponseEntity<Void> deleteProjectAcl(@PathVariable Long projectAclId, @RequestHeader(value = "X-PROJECT-ID", required = true) Long projectId) {
		log.debug("REST request to delete ProjectAcl : {}", projectAclId);

		projectAclRepository.deleteById(projectAclId);
		return ResponseEntity.ok()
			.headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, projectAclId.toString()))
			.build();
	}
}
