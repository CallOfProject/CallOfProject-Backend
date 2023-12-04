package callofproject.dev.service.ticket.config.kafka;

import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.service.ticket.dto.TicketDTO;
import callofproject.dev.service.ticket.mapper.ITicketMapper;
import callofproject.dev.service.ticket.service.TicketService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer
{
    private final TicketService m_ticketService;
    private final ITicketMapper m_ticketMapper;

    public KafkaConsumer(TicketService ticketService, ITicketMapper ticketMapper)
    {
        m_ticketService = ticketService;
        m_ticketMapper = ticketMapper;
    }

    @KafkaListener(topics = {"${spring.kafka.topic-name}"}, groupId = "${spring.kafka.consumer.group-id}")
    public void listenTicketTopic(TicketDTO ticketDTO)
    {
        switch (ticketDTO.getStatus())
        {
            case CREATE, UPDATE -> m_ticketService.upsertTicket(m_ticketMapper.toTicket(ticketDTO));
            default -> throw new DataServiceException("Invalid status");
        }
    }
}