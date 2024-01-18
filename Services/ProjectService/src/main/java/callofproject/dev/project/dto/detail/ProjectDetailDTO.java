package callofproject.dev.project.dto.detail;

import callofproject.dev.data.project.entity.enums.*;
import callofproject.dev.nosql.entity.ProjectTag;
import callofproject.dev.project.dto.ProjectParticipantDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents detailed information about a project.
 * This class includes details such as project ID, image path, title, summary,
 * description, deadlines, participant and tag information, and various requirements.
 */
public class ProjectDetailDTO
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
    @JsonProperty("max_participant")
    private int maxParticipant;
    @JsonProperty("techinical_requirements")
    private String technicalRequirements;
    @JsonProperty("special_requirements")
    private String specialRequirements;
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
    @JsonProperty("project_status")
    private EProjectStatus projectStatus;
    @JsonProperty("feedback_time_range")
    private EFeedbackTimeRange feedbackTimeRange;
    @JsonProperty("project_owner_name")
    private String projectOwnerName;
    @JsonProperty("admin_note")
    private String adminNote;
    @JsonProperty("project_tags")
    private List<ProjectTag> projectTags;
    @JsonProperty("project_participants")
    private List<ProjectParticipantDTO> projectParticipants;

    //------------------------------------------------------------------------------------------------------------------

    /**
     * ProjectDetailDTO
     */
    public ProjectDetailDTO()
    {
    }

    /**
     * Retrieves the unique identifier for the project.
     *
     * @return the project ID
     */
    public String getProjectId()
    {
        return projectId;
    }

    /**
     * Sets the unique identifier for the project.
     *
     * @param projectId the project ID to set
     */
    public void setProjectId(String projectId)
    {
        this.projectId = projectId;
    }

    /**
     * Retrieves the path to the project image.
     *
     * @return the project image path
     */
    public String getProjectImagePath()
    {
        return projectImagePath;
    }


    /**
     * Sets the path to the project's image.
     *
     * @param projectImagePath the project image path to set
     */
    public void setProjectImagePath(String projectImagePath)
    {
        this.projectImagePath = projectImagePath;
    }

    /**
     * Retrieves the title of the project.
     *
     * @return the project title
     */
    public String getProjectTitle()
    {
        return projectTitle;
    }

    /**
     * Sets the title of the project.
     *
     * @param projectTitle the project title to set
     */
    public void setProjectTitle(String projectTitle)
    {
        this.projectTitle = projectTitle;
    }

    /**
     * Retrieves the description of the project.
     *
     * @return the project description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Sets the description of the project.
     *
     * @param description the project description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Retrieves the summary of the project.
     *
     * @return the project summary
     */
    public String getProjectSummary()
    {
        return projectSummary;
    }

    /**
     * Sets the summary of the project.
     *
     * @param projectSummary the project summary to set
     */
    public void setProjectSummary(String projectSummary)
    {
        this.projectSummary = projectSummary;
    }

    /**
     * Retrieves the aim of the project.
     *
     * @return the project aim
     */
    public String getProjectAim()
    {
        return projectAim;
    }

    /**
     * Sets the aim of the project.
     *
     * @param projectAim the project aim to set
     */
    public void setProjectAim(String projectAim)
    {
        this.projectAim = projectAim;
    }

    /**
     * Retrieves the name of the project owner.
     *
     * @return the project owner name
     */
    public String getProjectOwnerName()
    {
        return projectOwnerName;
    }

    /**
     * Sets the name of the project owner.
     *
     * @param projectOwnerName the project owner name to set
     */
    public void setProjectOwnerName(String projectOwnerName)
    {
        this.projectOwnerName = projectOwnerName;
    }


    /**
     * Retrieves the technical requirements of the project.
     *
     * @return the project technical requirements
     */
    public String getTechnicalRequirements()
    {
        return technicalRequirements;
    }

    /**
     * Sets the technical requirements of the project.
     *
     * @param technicalRequirements the project technical requirements to set
     */
    public void setTechnicalRequirements(String technicalRequirements)
    {
        this.technicalRequirements = technicalRequirements;
    }

    /**
     * Retrieves the special requirements of the project.
     *
     * @return the project special requirements
     */
    public String getSpecialRequirements()
    {
        return specialRequirements;
    }

    /**
     * Sets the special requirements of the project.
     *
     * @param specialRequirements the project special requirements to set
     */
    public void setSpecialRequirements(String specialRequirements)
    {
        this.specialRequirements = specialRequirements;
    }


    /**
     * Retrieves the admin note of the project.
     *
     * @return the project admin note
     */
    public String getAdminNote()
    {
        return adminNote;
    }

    /**
     * Sets the admin note of the project.
     *
     * @param adminNote the project admin note to set
     */
    public void setAdminNote(String adminNote)
    {
        this.adminNote = adminNote;
    }

    /**
     * Retrieves the maximum number of participants for the project.
     *
     * @return the project maximum number of participants
     */
    public int getMaxParticipant()
    {
        return maxParticipant;
    }

    /**
     * Sets the maximum number of participants for the project.
     *
     * @param maxParticipant the project maximum number of participants to set
     */
    public void setMaxParticipant(int maxParticipant)
    {
        this.maxParticipant = maxParticipant;
    }


    /**
     * Retrieves the application deadline of the project.
     *
     * @return the project application deadline
     */
    public LocalDate getApplicationDeadline()
    {
        return applicationDeadline;
    }

    /**
     * Sets the application deadline of the project.
     *
     * @param applicationDeadline the project application deadline to set
     */
    public void setApplicationDeadline(LocalDate applicationDeadline)
    {
        this.applicationDeadline = applicationDeadline;
    }

    /**
     * Retrieves the expected completion date of the project.
     *
     * @return the project expected completion date
     */
    public LocalDate getExpectedCompletionDate()
    {
        return expectedCompletionDate;
    }

    /**
     * Sets the expected completion date of the project.
     *
     * @param expectedCompletionDate the project expected completion date to set
     */
    public void setExpectedCompletionDate(LocalDate expectedCompletionDate)
    {
        this.expectedCompletionDate = expectedCompletionDate;
    }

    /**
     * Retrieves the start date of the project.
     *
     * @return the project start date
     */
    public LocalDate getStartDate()
    {
        return startDate;
    }

    /**
     * Sets the start date of the project.
     *
     * @param startDate the project start date to set
     */
    public void setStartDate(LocalDate startDate)
    {
        this.startDate = startDate;
    }


    /**
     * Retrieves the feedback time range of the project.
     *
     * @return the project feedback time range
     */
    public EFeedbackTimeRange getFeedbackTimeRange()
    {
        return feedbackTimeRange;
    }

    /**
     * Sets the feedback time range of the project.
     *
     * @param feedbackTimeRange the project feedback time range to set
     */
    public void setFeedbackTimeRange(EFeedbackTimeRange feedbackTimeRange)
    {
        this.feedbackTimeRange = feedbackTimeRange;
    }


    /**
     * Retrieves the profession level of the project.
     *
     * @return the project profession level
     */
    public EProjectProfessionLevel getProfessionLevel()
    {
        return professionLevel;
    }

    /**
     * Sets the profession level of the project.
     *
     * @param professionLevel the project profession level to set
     */
    public void setProfessionLevel(EProjectProfessionLevel professionLevel)
    {
        this.professionLevel = professionLevel;
    }


    /**
     * Retrieves the sector of the project.
     *
     * @return the project sector
     */
    public ESector getSector()
    {
        return sector;
    }

    /**
     * Sets the sector of the project.
     *
     * @param sector the project sector to set
     */
    public void setSector(ESector sector)
    {
        this.sector = sector;
    }


    /**
     * Retrieves the degree of the project.
     *
     * @return the project degree
     */
    public EDegree getDegree()
    {
        return degree;
    }

    /**
     * Sets the degree of the project.
     *
     * @param degree the project degree to set
     */
    public void setDegree(EDegree degree)
    {
        this.degree = degree;
    }


    /**
     * Retrieves the level of the project.
     *
     * @return the project level
     */
    public EProjectLevel getProjectLevel()
    {
        return projectLevel;
    }

    /**
     * Sets the level of the project.
     *
     * @param projectLevel the project level to set
     */
    public void setProjectLevel(EProjectLevel projectLevel)
    {
        this.projectLevel = projectLevel;
    }


    /**
     * Retrieves the interview type of the project.
     *
     * @return the project interview type
     */
    public EInterviewType getInterviewType()
    {
        return interviewType;
    }

    /**
     * Sets the interview type of the project.
     *
     * @param interviewType the project interview type to set
     */
    public void setInterviewType(EInterviewType interviewType)
    {
        this.interviewType = interviewType;
    }

    /**
     * Retrieves the status of the project.
     *
     * @return the project status
     */
    public EProjectStatus getProjectStatus()
    {
        return projectStatus;
    }

    /**
     * Sets the status of the project.
     *
     * @param projectStatus the project status to set
     */
    public void setProjectStatus(EProjectStatus projectStatus)
    {
        this.projectStatus = projectStatus;
    }

    /**
     * Retrieves the tags of the project.
     *
     * @return the project tags
     */
    public List<ProjectTag> getProjectTags()
    {
        return projectTags;
    }

    /**
     * Sets the tags of the project.
     *
     * @param projectTags the project tags to set
     */
    public void setProjectTags(List<ProjectTag> projectTags)
    {
        this.projectTags = projectTags;
    }

    /**
     * Retrieves the participants of the project.
     *
     * @return the project participants
     */
    public List<ProjectParticipantDTO> getProjectParticipants()
    {
        return projectParticipants;
    }

    /**
     * Sets the participants of the project.
     *
     * @param projectParticipants the project participants to set
     */
    public void setProjectParticipants(List<ProjectParticipantDTO> projectParticipants)
    {
        this.projectParticipants = projectParticipants;
    }
}
