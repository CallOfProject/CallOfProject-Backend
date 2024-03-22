package callofproject.dev.service.filterandsearch.service;

import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.project.entity.Project;
import callofproject.dev.data.project.repository.IProjectRepository;
import callofproject.dev.repository.authentication.entity.User;
import callofproject.dev.repository.authentication.repository.rdbms.IUserRepository;
import callofproject.dev.service.filterandsearch.dto.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static callofproject.dev.service.filterandsearch.config.specification.ProjectFilterSpecifications.searchProjects;
import static callofproject.dev.service.filterandsearch.config.specification.UserFilterSpecifications.searchUsers;


@Service
@Lazy
@Transactional(transactionManager = "projectDbTransactionManager")
public class SearchService
{
    private final IUserRepository m_userRepository;
    private final IProjectRepository m_projectRepository;

    public SearchService(IUserRepository userRepository, IProjectRepository projectRepository)
    {
        m_userRepository = userRepository;
        m_projectRepository = projectRepository;
    }

    public MultipleResponseMessagePageable<Object> search(String keyword, int page)
    {
        Specification<User> userSpec = searchUsers(keyword);
        Specification<Project> projectSpec = searchProjects(keyword);
        var pageR = PageRequest.of(page - 1, 20);
        var users = m_userRepository.findAll(userSpec, pageR);
        var projects = m_projectRepository.findAll(projectSpec, pageR);
        var usersDTO = new UsersDTO(users.get().map(u -> new UserDTO(u.getUserId(), u.getUsername(), u.getFirstName(), u.getMiddleName(), u.getLastName())).toList());
        var projectsDTO = new ProjectsDTO(projects.get().map(p -> new ProjectDTO(p.getProjectId(), p.getProjectName(), p.getProjectImagePath(), p.getProjectSummary(), p.getProjectOwner().getUsername(), p.getProjectStatus())).toList());
        var elementCount = usersDTO.users().size() + projectsDTO.projects().size();
        return new MultipleResponseMessagePageable<>(1, page, elementCount, "Found!", new SearchUserAndProjectResponse(projectsDTO, usersDTO));
    }
}
