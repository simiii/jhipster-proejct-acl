package de.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import de.app.domain.enumeration.ProjectRole;

/**
 * A ProjectAcl.
 */
@Entity
@Table(name = "project_acl")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProjectAcl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_role", nullable = false)
    private ProjectRole role;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Project project;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProjectRole getRole() {
        return role;
    }

    public ProjectAcl role(ProjectRole role) {
        this.role = role;
        return this;
    }

    public void setRole(ProjectRole role) {
        this.role = role;
    }

    public Project getProject() {
        return project;
    }

    public ProjectAcl project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public ProjectAcl user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProjectAcl projectAcl = (ProjectAcl) o;
        if (projectAcl.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), projectAcl.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProjectAcl{" +
            "id=" + getId() +
            ", role='" + getRole() + "'" +
            "}";
    }
}
