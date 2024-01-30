package callofproject.dev.project.config.kafka.dto;

import callofproject.dev.data.project.entity.enums.AdminOperationStatus;
import callofproject.dev.data.project.entity.enums.EProjectStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public record ProjectInfoKafkaDTO(
        @JsonProperty("project_id")
        UUID m_projectId,
        @JsonProperty("project_name")
        String m_projectName,
        @JsonProperty("project_owner")
        UserKafkaDTO m_projectOwner,
        @JsonProperty("project_participants")
        List<ProjectParticipantKafkaDTO> m_projectParticipants,
        @JsonProperty("project_status")
        EProjectStatus m_projectStatus,
        @JsonProperty("admin_operation_status")
        AdminOperationStatus m_adminOperationStatus)
{
}
