package callofproject.dev.service.ticket.config.kafka;

import callofproject.dev.service.ticket.dto.TicketDTO;
import callofproject.dev.service.ticket.service.TicketService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TicketKafkaConsumer
{
    private final TicketService m_ticketService;

    public TicketKafkaConsumer(TicketService ticketService)
    {
        m_ticketService = ticketService;
    }

    @KafkaListener(topics = {"${spring.kafka.topic-name}"}, groupId = "${spring.kafka.consumer.group-id}")
    public void listenTicketTopic(TicketDTO ticketDTOEEx)
    {
       /* switch (ticketDTO.getStatus())
        {
            case OPEN, CLOSED -> m_ticketService.upsertTicket(ticketDTO);
            default -> throw new DataServiceException("Invalid status");
        }*/
    }
}