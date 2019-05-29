package de.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.app.domain.Project;

/**
 * Spring Data repository for the Project entity.
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>
{

	List<Project> findDistinctByAclsUserLogin(String login);

	void deleteByIdAndAclsUserLogin(Long id, String string);

	Optional<Project> findByIdAndAclsUserLogin(Long id, String string);


}
