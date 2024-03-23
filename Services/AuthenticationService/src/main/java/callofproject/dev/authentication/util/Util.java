package callofproject.dev.authentication.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for general operations.
 */
public final class Util
{
    /**
     * Private constructor for Util class.
     */
    private Util()
    {
    }

    /**
     * Get email template content.
     *
     * @param templateName The name of the template file.
     * @return The content of the email template.
     */
    public static String getEmailTemplate(String templateName)
    {
        var templateContent = "";
        try
        {
            var resource = new ClassPathResource(templateName);
            var reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
            templateContent = FileCopyUtils.copyToString(reader);
            reader.close();

            return templateContent;
        } catch (IOException e)
        {
            System.err.println("Error reading email template");
        }

        return templateContent;
    }
}