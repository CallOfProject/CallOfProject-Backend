package callofproject.dev.service.ticket;

import callofproject.dev.data.common.enums.EOperation;
import callofproject.dev.service.ticket.entity.Ticket;
import callofproject.dev.service.ticket.service.TicketService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import java.util.UUID;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableEurekaServer
public class TicketServiceApplication implements CommandLineRunner
{

    private final TicketService m_ticketService;

    public TicketServiceApplication(TicketService ticketService)
    {
        m_ticketService = ticketService;
    }

    public static void main(String[] args)
    {
        SpringApplication.run(TicketServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception
    {
       /* var a = new Ticket("nurican", UUID.randomUUID(), "title", "topic", "description", EOperation.DELETE);
        m_ticketService.upsertTicket(a);*/
    }
}