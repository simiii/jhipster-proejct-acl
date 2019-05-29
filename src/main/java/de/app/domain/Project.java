package de.app.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Project implements Serializable
{

	private static final long	serialVersionUID	= 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long				id;

	@NotNull
	@Column(name = "name", nullable = false)
	private String				name;

	@JsonIgnore
	@OneToMany(mappedBy = "project")
	private Set<ProjectAcl>		acls;

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Project name(String name) {
		this.name = name;
		return this;
	}

	public void setName(String name) {
		this.name = name;
	}
	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

	public Set<ProjectAcl> getAcls() {
		return acls;
	}

	public void setAcls(Set<ProjectAcl> acls) {
		this.acls = acls;
	}
	
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Project project = (Project) o;
		if (project.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), project.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "Project{" + "id=" + getId() + ", name='" + getName() + "'" + "}";
	}
}
