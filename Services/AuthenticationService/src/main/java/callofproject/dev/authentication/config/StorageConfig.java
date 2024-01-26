package callofproject.dev.authentication.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * StorageConfig
 */
@Configuration
public class StorageConfig
{
    @Value("${cloud.aws.credentials.access-key}")
    private String m_accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String m_secretKey;
    @Value("${cloud.aws.region.static}")
    private String m_region;

    /**
     * Constructs a new StorageConfig.
     */
    public StorageConfig()
    {
    }

    @Bean
    public AmazonS3 provideS3Client()
    {
        var credentials = new BasicAWSCredentials(m_accessKey, m_secretKey);
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(m_region).build();
    }
}
