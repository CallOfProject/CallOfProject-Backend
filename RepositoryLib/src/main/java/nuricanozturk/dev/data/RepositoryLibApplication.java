package nuricanozturk.dev.data;

import nuricanozturk.dev.data.entity.Address;
import nuricanozturk.dev.data.entity.House;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RepositoryLibApplication implements ApplicationRunner
{

    public static void main(String[] args)
    {
        SpringApplication.run(RepositoryLibApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception
    {
        var address = new Address("Izmir", "Turkey", "Street1");
        var house = new House("ev", "House1")
    }
}
