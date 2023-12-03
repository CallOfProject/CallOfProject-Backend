package callofproject.dev.authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record ExperienceUpsertDTO(

        @JsonProperty("company_name")
        String companyName,
        String description,
        @JsonProperty("company_website")
        String companyWebsite,
        @JsonProperty("start_date")
        LocalDate startDate,
        @JsonProperty("finish_date")
        LocalDate finishDate,
        @JsonProperty("is_continue")
        boolean isContinue

)
{
}
