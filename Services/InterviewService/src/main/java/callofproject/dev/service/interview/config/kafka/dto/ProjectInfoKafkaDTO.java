package callofproject.dev.service.interview.config.kafka.dto;

import callofproject.dev.data.common.enums.AdminOperationStatus;
import callofproject.dev.data.interview.entity.enums.EProjectStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public record ProjectInfoKafkaDTO(
        @JsonProperty("project_id")
        UUID projectId,
        @JsonProperty("project_name")
        String projectName,
        @JsonProperty("project_owner")
        UserKafkaDTO projectOwner,
        @JsonProperty("project_participants")
        List<ProjectParticipantKafkaDTO> projectParticipants,
        @JsonProperty("project_status")
        EProjectStatus projectStatus,
        @JsonProperty("admin_operation_status")
        AdminOperationStatus adminOperationStatus)
{
    @Override
    public String toString()
    {
        return "ProjectInfoKafkaDTO{" +
                "projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                ", projectOwner=" + projectOwner +
                ", projectParticipants=" + projectParticipants +
                ", projectStatus=" + projectStatus +
                ", adminOperationStatus=" + adminOperationStatus +
                '}';
    }
}
