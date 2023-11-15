package callofproject.dev.repository.repository.project;

import callofproject.dev.repository.repository.project.entity.Project;
import callofproject.dev.repository.repository.project.entity.ProjectAccessType;
import callofproject.dev.repository.repository.project.entity.ProjectProfessionLevel;
import callofproject.dev.repository.repository.project.repository.IProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class Appp implements ApplicationRunner
{
    @Autowired
    private IProjectRepository m_repository;
    public static void main(String[] args)
    {
        SpringApplication.run(Appp.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception
    {
        var p = new Project("photo", "name", "sum", "desc","aim",
                LocalDate.now(), LocalDate.now(), LocalDate.now(), 10, ProjectAccessType.PUBLIC, ProjectProfessionLevel.Expert,
                "fsdfds");

        var p2 = new Project("photo2", "name2", "sum", "desc","aim",
                LocalDate.now(), LocalDate.now(), LocalDate.now(), 10, ProjectAccessType.PRIVATE, ProjectProfessionLevel.Intermediate,
                "fsdfds");
        m_repository.save(p);
        m_repository.save(p2);

    }
}
