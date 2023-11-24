package callofproject.dev.project.dto;

public record ResponseDTO(
        boolean status,
        String message,
        Object data)
{
}
