package de.app.service.dto;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import de.app.domain.Project;
import de.app.domain.enumeration.ProjectRole;

public class ProjectAclDTO
{

	private String				projectName;

	private Set<ProjectRole>	roles	= new HashSet<>();

	public ProjectAclDTO()
	{}

	public ProjectAclDTO(Entry<Project, Set<ProjectRole>> entry)
	{
		this.projectName = entry.getKey()
			.getName();
		this.roles = entry.getValue();

	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Set<ProjectRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<ProjectRole> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "ProjectAclDTO [projectName=" + projectName + ", roles=" + roles + "]";
	}

}
