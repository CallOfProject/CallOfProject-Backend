package callofproject.dev.usermanagement.config;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import org.bson.UuidRepresentation;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
public class MongoRepoConfig
{
    @Bean
    public MongoClientFactoryBean mongoConfig(@Value("${spring.data.mongodb.uri}") String uri)
    {
        var mongo = new MongoClientFactoryBean();
        var conn = new ConnectionString(uri);
        mongo.setConnectionString(conn);

        var pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        var codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);

        var clientSettings = MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .applyConnectionString(conn)
                .codecRegistry(codecRegistry)
                .build();

        mongo.setMongoClientSettings(clientSettings);

        return mongo;
    }
}
