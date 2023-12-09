package callofproject.dev.project.dto.owner;

import callofproject.dev.data.project.entity.enums.*;
import callofproject.dev.nosql.entity.ProjectTag;
import callofproject.dev.project.dto.ProjectParticipantDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public class ProjectOwnerViewDTO
{
    @JsonProperty("project_id")
    private String projectId;
    @JsonProperty("project_image_path")
    private String projectImagePath;
    @JsonProperty("project_title")
    private String projectTitle;
    @JsonProperty("project_summary")
    private String projectSummary;
    @JsonProperty("project_aim")
    private String projectAim;
    @JsonProperty("project_description")
    private String description;
    @JsonProperty("application_deadline")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate applicationDeadline;
    @JsonProperty("expected_completion_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate expectedCompletionDate;
    @JsonProperty("start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate startDate;
    @JsonProperty("completion_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate completionDate;
    @JsonProperty("max_participant")
    private int maxParticipant;
    @JsonProperty("invite_link")
    private String inviteLink;
    @JsonProperty("techinical_requirements")
    private String technicalRequirements;
    @JsonProperty("special_requirements")
    private String specialRequirements;
    @JsonProperty("project_access_type")
    private EProjectAccessType projectAccessType;
    @JsonProperty("project_profession_level")
    private EProjectProfessionLevel professionLevel;
    @JsonProperty("project_sector")
    private ESector sector;
    @JsonProperty("project_degree")
    private EDegree degree;
    @JsonProperty("project_level")
    private EProjectLevel projectLevel;
    @JsonProperty("interview_type")
    private EInterviewType interviewType;
    @JsonProperty("project_owner_name")
    private String projectOwnerName;
    @JsonProperty("admin_note")
    private String adminNote;
    @JsonProperty("project_status")
    private EProjectStatus projectStatus;
    @JsonProperty("admin_operation_status")
    private AdminOperationStatus adminOperationStatus;
    @JsonProperty("feedback_time_range")
    private EFeedbackTimeRange feedbackTimeRange;
    @JsonProperty("project_participants")
    private List<ProjectParticipantDTO> projectParticipants;
    @JsonProperty("project_tags")
    private List<ProjectTag> projectTags;

    //------------------------------------------------------------------------------------------------------------------

    public ProjectOwnerViewDTO()
    {
    }

    public String getProjectId()
    {
        return projectId;
    }

    public void setProjectId(String projectId)
    {
        this.projectId = projectId;
    }

    public String getProjectImagePath()
    {
        return projectImagePath;
    }

    public void setProjectImagePath(String projectImagePath)
    {
        this.projectImagePath = projectImagePath;
    }

    public String getProjectTitle()
    {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle)
    {
        this.projectTitle = projectTitle;
    }

    public String getProjectSummary()
    {
        return projectSummary;
    }

    public void setProjectSummary(String projectSummary)
    {
        this.projectSummary = projectSummary;
    }

    public String getProjectAim()
    {
        return projectAim;
    }

    public void setProjectAim(String projectAim)
    {
        this.projectAim = projectAim;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public LocalDate getApplicationDeadline()
    {
        return applicationDeadline;
    }

    public void setApplicationDeadline(LocalDate applicationDeadline)
    {
        this.applicationDeadline = applicationDeadline;
    }

    public LocalDate getExpectedCompletionDate()
    {
        return expectedCompletionDate;
    }

    public void setExpectedCompletionDate(LocalDate expectedCompletionDate)
    {
        this.expectedCompletionDate = expectedCompletionDate;
    }

    public LocalDate getStartDate()
    {
        return startDate;
    }

    public void setStartDate(LocalDate startDate)
    {
        this.startDate = startDate;
    }

    public LocalDate getCompletionDate()
    {
        return completionDate;
    }

    public void setCompletionDate(LocalDate completionDate)
    {
        this.completionDate = completionDate;
    }

    public int getMaxParticipant()
    {
        return maxParticipant;
    }

    public void setMaxParticipant(int maxParticipant)
    {
        this.maxParticipant = maxParticipant;
    }

    public String getInviteLink()
    {
        return inviteLink;
    }

    public void setInviteLink(String inviteLink)
    {
        this.inviteLink = inviteLink;
    }

    public String getTechnicalRequirements()
    {
        return technicalRequirements;
    }

    public void setTechnicalRequirements(String technicalRequirements)
    {
        this.technicalRequirements = technicalRequirements;
    }

    public String getSpecialRequirements()
    {
        return specialRequirements;
    }

    public void setSpecialRequirements(String specialRequirements)
    {
        this.specialRequirements = specialRequirements;
    }

    public EProjectAccessType getProjectAccessType()
    {
        return projectAccessType;
    }

    public void setProjectAccessType(EProjectAccessType projectAccessType)
    {
        this.projectAccessType = projectAccessType;
    }

    public EProjectProfessionLevel getProfessionLevel()
    {
        return professionLevel;
    }

    public void setProfessionLevel(EProjectProfessionLevel professionLevel)
    {
        this.professionLevel = professionLevel;
    }

    public ESector getSector()
    {
        return sector;
    }

    public void setSector(ESector sector)
    {
        this.sector = sector;
    }

    public EDegree getDegree()
    {
        return degree;
    }

    public void setDegree(EDegree degree)
    {
        this.degree = degree;
    }

    public EProjectLevel getProjectLevel()
    {
        return projectLevel;
    }

    public void setProjectLevel(EProjectLevel projectLevel)
    {
        this.projectLevel = projectLevel;
    }

    public EInterviewType getInterviewType()
    {
        return interviewType;
    }

    public void setInterviewType(EInterviewType interviewType)
    {
        this.interviewType = interviewType;
    }

    public String getProjectOwnerName()
    {
        return projectOwnerName;
    }

    public void setProjectOwnerName(String projectOwnerName)
    {
        this.projectOwnerName = projectOwnerName;
    }

    public String getAdminNote()
    {
        return adminNote;
    }

    public void setAdminNote(String adminNote)
    {
        this.adminNote = adminNote;
    }

    public EProjectStatus getProjectStatus()
    {
        return projectStatus;
    }

    public void setProjectStatus(EProjectStatus projectStatus)
    {
        this.projectStatus = projectStatus;
    }

    public AdminOperationStatus getAdminOperationStatus()
    {
        return adminOperationStatus;
    }

    public void setAdminOperationStatus(AdminOperationStatus adminOperationStatus)
    {
        this.adminOperationStatus = adminOperationStatus;
    }

    public EFeedbackTimeRange getFeedbackTimeRange()
    {
        return feedbackTimeRange;
    }

    public void setFeedbackTimeRange(EFeedbackTimeRange feedbackTimeRange)
    {
        this.feedbackTimeRange = feedbackTimeRange;
    }

    public List<ProjectParticipantDTO> getProjectParticipants()
    {
        return projectParticipants;
    }

    public void setProjectParticipants(List<ProjectParticipantDTO> projectParticipants)
    {
        this.projectParticipants = projectParticipants;
    }

    public List<ProjectTag> getProjectTags()
    {
        return projectTags;
    }

    public void setProjectTags(List<ProjectTag> projectTags)
    {
        this.projectTags = projectTags;
    }
}