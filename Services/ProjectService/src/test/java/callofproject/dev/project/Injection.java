package callofproject.dev.project;

import callofproject.dev.data.project.dal.ProjectServiceHelper;
import callofproject.dev.data.project.repository.IUserRepository;
import callofproject.dev.project.service.AdminService;
import callofproject.dev.project.service.ProjectOwnerService;
import callofproject.dev.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

@Component
@SpringBootTest
public class Injection
{
    @Autowired
    private ProjectOwnerService projectOwnerService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ProjectServiceHelper projectServiceHelper;

    @Autowired
    private IUserRepository userRepository;

    public IUserRepository getUserRepository()
    {
        return userRepository;
    }

    public ProjectServiceHelper getProjectServiceHelper()
    {
        return projectServiceHelper;
    }

    public ProjectOwnerService getProjectOwnerService()
    {
        return projectOwnerService;
    }

    public ProjectService getProjectService()
    {
        return projectService;
    }

    public AdminService getAdminService()
    {
        return adminService;
    }

}
