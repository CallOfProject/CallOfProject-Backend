package callofproject.dev.repository.environment.dto;

import java.util.List;

public class UniversitiesDTO
{
    private List<UniversityDTO> universities;

    public UniversitiesDTO(List<UniversityDTO> universities)
    {
        this.universities = universities;
    }

    public List<UniversityDTO> getUniversities()
    {
        return universities;
    }
}
