package callofproject.dev.project.dto.owner;

import callofproject.dev.data.project.entity.enums.*;
import callofproject.dev.nosql.entity.ProjectTag;
import callofproject.dev.project.dto.ProjectParticipantDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a detailed view of a project for the project owner.
 * This class includes various properties of the project such as
 * title, summary, deadlines, and participant information.
 */
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

    /**
     * Default constructor for ProjectOwnerViewDTO.
     */
    public ProjectOwnerViewDTO()
    {
    }

    /**
     * Gets the unique identifier of the project.
     *
     * @return the project identifier as a String
     */
    public String getProjectId()
    {
        return projectId;
    }

    /**
     * Sets the unique identifier of the project.
     *
     * @param projectId the project identifier as a String
     */
    public void setProjectId(String projectId)
    {
        this.projectId = projectId;
    }

    /**
     * Gets the path of the project image.
     *
     * @return the project image path as a String
     */
    public String getProjectImagePath()
    {
        return projectImagePath;
    }

    /**
     * Sets the path of the project image.
     *
     * @param projectImagePath the project image path as a String
     */
    public void setProjectImagePath(String projectImagePath)
    {
        this.projectImagePath = projectImagePath;
    }

    /**
     * Gets the title of the project.
     *
     * @return the project title as a String
     */
    public String getProjectTitle()
    {
        return projectTitle;
    }

    /**
     * Sets the title of the project.
     *
     * @param projectTitle the project title as a String
     */
    public void setProjectTitle(String projectTitle)
    {
        this.projectTitle = projectTitle;
    }

    /**
     * Gets the summary of the project.
     *
     * @return the project summary as a String
     */
    public String getProjectSummary()
    {
        return projectSummary;
    }

    /**
     * Sets the summary of the project.
     *
     * @param projectSummary the project summary as a String
     */
    public void setProjectSummary(String projectSummary)
    {
        this.projectSummary = projectSummary;
    }

    /**
     * Gets the aim of the project.
     *
     * @return the project aim as a String
     */
    public String getProjectAim()
    {
        return projectAim;
    }

    /**
     * Sets the aim of the project.
     *
     * @param projectAim the project aim as a String
     */
    public void setProjectAim(String projectAim)
    {
        this.projectAim = projectAim;
    }

    /**
     * Gets the description of the project.
     *
     * @return the project description as a String
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Sets the description of the project.
     *
     * @param description the project description as a String
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Gets the application deadline of the project.
     *
     * @return the project application deadline as a LocalDate
     */
    public LocalDate getApplicationDeadline()
    {
        return applicationDeadline;
    }

    /**
     * Sets the application deadline of the project.
     *
     * @param applicationDeadline the project application deadline as a LocalDate
     */
    public void setApplicationDeadline(LocalDate applicationDeadline)
    {
        this.applicationDeadline = applicationDeadline;
    }

    /**
     * Gets the expected completion date of the project.
     *
     * @return the project expected completion date as a LocalDate
     */
    public LocalDate getExpectedCompletionDate()
    {
        return expectedCompletionDate;
    }

    /**
     * Sets the expected completion date of the project.
     *
     * @param expectedCompletionDate the project expected completion date as a LocalDate
     */
    public void setExpectedCompletionDate(LocalDate expectedCompletionDate)
    {
        this.expectedCompletionDate = expectedCompletionDate;
    }

    /**
     * Gets the start date of the project.
     *
     * @return the project start date as a LocalDate
     */
    public LocalDate getStartDate()
    {
        return startDate;
    }

    /**
     * Sets the start date of the project.
     *
     * @param startDate the project start date as a LocalDate
     */
    public void setStartDate(LocalDate startDate)
    {
        this.startDate = startDate;
    }

    /**
     * Gets the completion date of the project.
     *
     * @return the project completion date as a LocalDate
     */
    public LocalDate getCompletionDate()
    {
        return completionDate;
    }

    /**
     * Sets the completion date of the project.
     *
     * @param completionDate the project completion date as a LocalDate
     */
    public void setCompletionDate(LocalDate completionDate)
    {
        this.completionDate = completionDate;
    }

    /**
     * Gets the maximum participant count of the project.
     *
     * @return the project maximum participant count as an int
     */
    public int getMaxParticipant()
    {
        return maxParticipant;
    }

    /**
     * Sets the maximum participant count of the project.
     *
     * @param maxParticipant the project maximum participant count as an int
     */
    public void setMaxParticipant(int maxParticipant)
    {
        this.maxParticipant = maxParticipant;
    }

    /**
     * Gets the invite link of the project.
     *
     * @return the project invite link as a String
     */
    public String getInviteLink()
    {
        return inviteLink;
    }

    /**
     * Sets the invite link of the project.
     *
     * @param inviteLink the project invite link as a String
     */
    public void setInviteLink(String inviteLink)
    {
        this.inviteLink = inviteLink;
    }

    /**
     * Gets the technical requirements of the project.
     *
     * @return the project technical requirements as a String
     */
    public String getTechnicalRequirements()
    {
        return technicalRequirements;
    }


    /**
     * Sets the technical requirements of the project.
     *
     * @param technicalRequirements the project technical requirements as a String
     */
    public void setTechnicalRequirements(String technicalRequirements)
    {
        this.technicalRequirements = technicalRequirements;
    }

    /**
     * Gets the special requirements of the project.
     *
     * @return the project special requirements as a String
     */
    public String getSpecialRequirements()
    {
        return specialRequirements;
    }

    /**
     * Sets the special requirements of the project.
     *
     * @param specialRequirements the project special requirements as a String
     */
    public void setSpecialRequirements(String specialRequirements)
    {
        this.specialRequirements = specialRequirements;
    }

    /**
     * Gets the access type of the project.
     *
     * @return the project access type as an EProjectAccessType
     */
    public EProjectAccessType getProjectAccessType()
    {
        return projectAccessType;
    }

    /**
     * Sets the access type of the project.
     *
     * @param projectAccessType the project access type as an EProjectAccessType
     */
    public void setProjectAccessType(EProjectAccessType projectAccessType)
    {
        this.projectAccessType = projectAccessType;
    }

    /**
     * Gets the profession level of the project.
     *
     * @return the project profession level as an EProjectProfessionLevel
     */
    public EProjectProfessionLevel getProfessionLevel()
    {
        return professionLevel;
    }


    /**
     * Sets the profession level of the project.
     *
     * @param professionLevel the project profession level as an EProjectProfessionLevel
     */
    public void setProfessionLevel(EProjectProfessionLevel professionLevel)
    {
        this.professionLevel = professionLevel;
    }

    /**
     * Gets the sector of the project.
     *
     * @return the project sector as an ESector
     */
    public ESector getSector()
    {
        return sector;
    }

    /**
     * Sets the sector of the project.
     *
     * @param sector the project sector as an ESector
     */
    public void setSector(ESector sector)
    {
        this.sector = sector;
    }

    /**
     * Gets the degree of the project.
     *
     * @return the project degree as an EDegree
     */
    public EDegree getDegree()
    {
        return degree;
    }

    /**
     * Sets the degree of the project.
     *
     * @param degree the project degree as an EDegree
     */
    public void setDegree(EDegree degree)
    {
        this.degree = degree;
    }

    /**
     * Gets the level of the project.
     *
     * @return the project level as an EProjectLevel
     */
    public EProjectLevel getProjectLevel()
    {
        return projectLevel;
    }

    /**
     * Sets the level of the project.
     *
     * @param projectLevel the project level as an EProjectLevel
     */
    public void setProjectLevel(EProjectLevel projectLevel)
    {
        this.projectLevel = projectLevel;
    }

    /**
     * Gets the interview type of the project.
     *
     * @return the project interview type as an EInterviewType
     */
    public EInterviewType getInterviewType()
    {
        return interviewType;
    }

    /**
     * Sets the interview type of the project.
     *
     * @param interviewType the project interview type as an EInterviewType
     */
    public void setInterviewType(EInterviewType interviewType)
    {
        this.interviewType = interviewType;
    }

    /**
     * Gets the name of the project owner.
     *
     * @return the project owner name as a String
     */
    public String getProjectOwnerName()
    {
        return projectOwnerName;
    }

    /**
     * Sets the name of the project owner.
     *
     * @param projectOwnerName the project owner name as a String
     */
    public void setProjectOwnerName(String projectOwnerName)
    {
        this.projectOwnerName = projectOwnerName;
    }

    /**
     * Gets the admin note of the project.
     *
     * @return the project admin note as a String
     */
    public String getAdminNote()
    {
        return adminNote;
    }

    /**
     * Sets the admin note of the project.
     *
     * @param adminNote the project admin note as a String
     */
    public void setAdminNote(String adminNote)
    {
        this.adminNote = adminNote;
    }

    /**
     * Gets the status of the project.
     *
     * @return the project status as an EProjectStatus
     */
    public EProjectStatus getProjectStatus()
    {
        return projectStatus;
    }

    /**
     * Sets the status of the project.
     *
     * @param projectStatus the project status as an EProjectStatus
     */
    public void setProjectStatus(EProjectStatus projectStatus)
    {
        this.projectStatus = projectStatus;
    }

    /**
     * Gets the operation status of the project.
     *
     * @return the project operation status as an AdminOperationStatus
     */
    public AdminOperationStatus getAdminOperationStatus()
    {
        return adminOperationStatus;
    }

    /**
     * Sets the operation status of the project.
     *
     * @param adminOperationStatus the project operation status as an AdminOperationStatus
     */
    public void setAdminOperationStatus(AdminOperationStatus adminOperationStatus)
    {
        this.adminOperationStatus = adminOperationStatus;
    }

    /**
     * Gets the feedback time range of the project.
     *
     * @return the project feedback time range as an EFeedbackTimeRange
     */
    public EFeedbackTimeRange getFeedbackTimeRange()
    {
        return feedbackTimeRange;
    }

    /**
     * Sets the feedback time range of the project.
     *
     * @param feedbackTimeRange the project feedback time range as an EFeedbackTimeRange
     */
    public void setFeedbackTimeRange(EFeedbackTimeRange feedbackTimeRange)
    {
        this.feedbackTimeRange = feedbackTimeRange;
    }

    /**
     * Gets the list of project participants.
     *
     * @return the project participants as a List of ProjectParticipantDTO
     */
    public List<ProjectParticipantDTO> getProjectParticipants()
    {
        return projectParticipants;
    }

    /**
     * Sets the list of project participants.
     *
     * @param projectParticipants the project participants as a List of ProjectParticipantDTO
     */
    public void setProjectParticipants(List<ProjectParticipantDTO> projectParticipants)
    {
        this.projectParticipants = projectParticipants;
    }

    /**
     * Gets the list of project tags.
     *
     * @return the project tags as a List of ProjectTag
     */
    public List<ProjectTag> getProjectTags()
    {
        return projectTags;
    }

    /**
     * Sets the list of project tags.
     *
     * @param projectTags the project tags as a List of ProjectTag
     */
    public void setProjectTags(List<ProjectTag> projectTags)
    {
        this.projectTags = projectTags;
    }
}