package de.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.app.domain.Project;
import de.app.domain.enumeration.ProjectRole;
import de.app.repository.ProjectAclRepository;

@Service
public class SecurityService
{

	@Autowired
	private ProjectAclRepository projectAclRepository;

	/**
	 * Checks the access of the current user to the project. The user must have one of the projectRoles
	 * 
	 * @param projectId
	 *            the project id
	 * @param projectRoles
	 *            one of the projectRoles a user must have to get access
	 * @return if the user has access to this project
	 */
	public boolean hasAccess(Long projectId, ProjectRole... projectRoles) {
		return projectAclRepository.findByUserIsCurrentUser()
			.stream()
			.filter(acl -> projectId.equals(acl.getProject().getId()))
			.filter(acl -> containsProjectRole(acl.getRole(), projectRoles))
			.findFirst()
			.isPresent();
	}

	/**
	 * Checks the access of the current user to the project. The user must have one of the projectRoles
	 * 
	 * @param project
	 *            the project
	 * @param projectRoles
	 *            one of the projectRoles a user must have to get access
	 * @return if the user has access to this project
	 */
	public boolean hasAccess(Project project, ProjectRole... projectRoles) {
		return projectAclRepository.findByUserIsCurrentUser()
			.stream()
			.filter(acl -> project.equals(acl.getProject()))
			.filter(acl -> containsProjectRole(acl.getRole(), projectRoles))
			.findFirst()
			.isPresent();
	}

	private boolean containsProjectRole(ProjectRole projectRole, ProjectRole... projectRoles) {
		boolean contains = false;
		if (projectRole != null && projectRoles != null) {
			for (ProjectRole pr : projectRoles) {
				if (pr.equals(projectRole)) {
					contains = true;
					break;
				}
			}
		}
		return contains;
	}

}
