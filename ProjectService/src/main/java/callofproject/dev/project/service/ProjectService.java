package callofproject.dev.project.service;

import callofproject.dev.repository.repository.project.dal.ProjectServiceHelper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Lazy
public class ProjectService
{
    private final ProjectServiceHelper m_serviceHelper;

    public ProjectService(ProjectServiceHelper serviceHelper)
    {
        m_serviceHelper = serviceHelper;
    }


}
