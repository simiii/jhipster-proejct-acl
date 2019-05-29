package de.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import de.app.domain.ProjectAcl;

/**
 * Spring Data  repository for the ProjectAcl entity.
 */
@Repository
public interface ProjectAclRepository extends JpaRepository<ProjectAcl, Long> {

    @Query("select project_acl from ProjectAcl project_acl where project_acl.user.login = ?#{principal.username}")
    List<ProjectAcl> findByUserIsCurrentUser();

    @Query("select project_acl from ProjectAcl project_acl where project_acl.user.id = ?1")
    List<ProjectAcl> findByUserId(Long userId);

	List<ProjectAcl> findByProjectId(Long projectId);

}
