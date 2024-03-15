package callofproject.dev.service.ticket.service;

import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.service.ticket.config.kafka.TicketKafkaProducer;
import callofproject.dev.service.ticket.dto.TicketCreateDTO;
import callofproject.dev.service.ticket.dto.TicketDTO;
import callofproject.dev.service.ticket.dto.TicketGiveResponseDTO;
import callofproject.dev.service.ticket.entity.TicketStatus;
import callofproject.dev.service.ticket.mapper.ITicketMapper;
import callofproject.dev.service.ticket.repository.ITicketRepository;
import org.antlr.v4.runtime.misc.Triple;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static java.lang.String.format;

@Service
@Lazy
public class TicketCallbackService
{
    private final int m_defaultPageSize = 30;
    private final ITicketRepository ticketRepository;
    private final TicketKafkaProducer m_ticketKafkaProducer;
    private final ITicketMapper m_ticketMapper;

    public TicketCallbackService(ITicketRepository ticketRepository, TicketKafkaProducer ticketKafkaProducer, ITicketMapper ticketMapper)
    {
        this.ticketRepository = ticketRepository;
        m_ticketKafkaProducer = ticketKafkaProducer;
        m_ticketMapper = ticketMapper;
    }


    public ResponseMessage<Object> upsertTicketCallback(TicketCreateDTO dto)
    {
        var ticket = doForDataService(() -> ticketRepository.save(m_ticketMapper.toTicket(dto)), "Error while saving ticket");
        return new ResponseMessage<>("Ticket is created successfully!", Status.CREATED, m_ticketMapper.toTicketUserViewDTO(ticket));
    }

    public ResponseMessage<Object> responseFeedbackCallback(TicketGiveResponseDTO dto)
    {
        var ticket = doForDataService(() -> ticketRepository.findById(dto.id()), "Error while fetching ticket");
        if (ticket.isEmpty())
            return new ResponseMessage<>("Ticket is not found!", Status.NOT_FOUND, null);

        ticket.get().setAnswer(dto.answer());
        ticket.get().setAdminId(dto.adminId());
        ticket.get().setAdminUsername(dto.adminUsername());
        ticket.get().setStatus(TicketStatus.CLOSED);
        ticket.get().setAnsweredDate(LocalDate.now());

        var updatedTicket = doForDataService(() -> ticketRepository.save(ticket.get()), "Error while updating ticket");
        var ticketDTO = m_ticketMapper.toTicketDTO(updatedTicket);

        return new ResponseMessage<>("Ticket is responded successfully!", Status.OK, ticketDTO);
    }

    public MultipleResponseMessagePageable<Object> findAllTicketsPageableCallback(int page)
    {
        var tickets = doForDataService(() -> ticketRepository.findAllByStatus(TicketStatus.OPEN, PageRequest.of(page - 1, m_defaultPageSize)), "Error while fetching tickets");
        var totalPages = tickets.getTotalPages();
        var ticketList = tickets.getContent();
        var ticketsDTO = ticketList.stream().map(m_ticketMapper::toTicketDTO).toList();
        return new MultipleResponseMessagePageable<>(totalPages, page, ticketList.size(), format("%d Tickets are found!", ticketList.size()), ticketsDTO);
    }

    public ResponseMessage<Object> findOpenTicketCount()
    {
        var openTicketCount = doForDataService(() -> ticketRepository.findTicketsByStatus(TicketStatus.OPEN),
                "Error while fetching open ticket count");

        return new ResponseMessage<>(format("%d open tickets are found!", openTicketCount.size()), Status.OK, openTicketCount.size());
    }

    public void sendNotification(TicketDTO ticket)
    {
    }

    public void sendEmail(TicketDTO object)
    {

    }
}
