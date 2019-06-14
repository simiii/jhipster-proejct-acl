package de.app.service;

import de.app.domain.Project;
import de.app.domain.ProjectAcl;
import de.app.domain.User;
import de.app.domain.enumeration.ProjectRole;
import de.app.repository.ProjectAclRepository;
import de.app.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectAclRepository projectAclRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserService userService;

    public Project saveWithAcl(Project project) {

        final Project result = projectRepository.save(project);

        final Optional<User> userWithAuthorities = userService.getUserWithAuthorities();
        projectAclRepository.save(new ProjectAcl()
            .role(ProjectRole.PROJECT_USER)
            .project(project)
            .user(userWithAuthorities.get()));
        projectAclRepository.save(new ProjectAcl()
            .role(ProjectRole.PROJECT_ADMIN)
            .project(project)
            .user(userWithAuthorities.get()));

        return result;
    }


}
