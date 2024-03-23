package callofproject.dev.service.ticket;

/**
 * Represents the bean name.
 * CopyRight(C) 2023 by Call Of Project Teams.
 */
public final class BeanName
{
    /**
     * The default constructor.
     */
    private BeanName()
    {
    }

    /**
     * The bean name for ticket kafka consumer.
     */
    public static final String TICKET_KAFKA_PRODUCER = "callofproject.dev.service.ticket.config.kafka.TicketKafkaProducer";

    /**
     * The bean name for ticket kafka consumer.
     */
    public static final String TICKET_REPOSITORY = "callofproject.dev.service.ticket.repository.ITicketRepository";

    /**
     * The bean name for ticket kafka consumer.
     */
    public static final String TICKET_SERVICE = "callofproject.dev.service.ticket.service.ticketservice";
}
