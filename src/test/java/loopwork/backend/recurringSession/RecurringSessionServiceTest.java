package loopwork.backend.recurringSession;

import loopwork.backend.client.Client;
import loopwork.backend.client.ClientNotFoundException;
import loopwork.backend.client.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RecurringSessionServiceTest {

    @Mock
    RecurringSessionRepository recurringSessionRepository;

    @Mock
    ClientService clientService;

    @InjectMocks
    RecurringSessionService recurringSessionService;

    @Test
    void shouldCreateRecurringSessionWhenClientExists() {
        RecurringSessionRequest sessionRequest = new RecurringSessionRequest(
                DayOfWeek.TUESDAY,
                LocalTime.of(18, 0),
                LocalTime.of(19, 0),
                LocalDate.now(),
                null
        );

        Client client = new Client(
                "Client",
                "client@example.com",
                null
        );

        when(clientService.findByIdOrThrow("client-01")).thenReturn(client);
        when(recurringSessionRepository.save(any(RecurringSession.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RecurringSessionResponse session = recurringSessionService.create(sessionRequest, "client-01");

        verify(recurringSessionRepository, atLeastOnce()).save(any(RecurringSession.class));
        assertEquals(DayOfWeek.TUESDAY, session.dayOfWeek());
        assertNull(session.effectiveEndDate());
    }

    @Test
    void shouldThrowRecurringSessionWhenClientNoneExists() {
        RecurringSessionRequest sessionRequest = new RecurringSessionRequest(
                DayOfWeek.TUESDAY,
                LocalTime.of(18, 0),
                LocalTime.of(19, 0),
                LocalDate.now(),
                null
        );

        when(clientService.findByIdOrThrow("client-01")).thenThrow(new ClientNotFoundException("client-01"));

        assertThrows(ClientNotFoundException.class, () -> recurringSessionService.create(sessionRequest, "client-01"));
    }

    @Test
    void shouldUpdateRecurringSessionWhenItExists() {
        Client client = new Client(
                "Client",
                "client@example.com",
                null
        );

        RecurringSession session = new RecurringSession(
                client,
                DayOfWeek.TUESDAY,
                LocalTime.of(18, 0), LocalTime.of(19, 0),
                LocalDate.now(), null
        );

        UpdateRecurringSessionRequest updateRequest = new UpdateRecurringSessionRequest(
                "client-01",
                DayOfWeek.WEDNESDAY,
                LocalTime.of(19, 0),
                LocalTime.of(20, 0),
                null,
                null
        );

        when(recurringSessionRepository.findById("session-01")).thenReturn(Optional.of(session));
        when(recurringSessionRepository.save(any(RecurringSession.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RecurringSessionResponse recurringSessionUpdated = recurringSessionService.update("session-01", updateRequest);

        verify(recurringSessionRepository, atLeastOnce()).save(any(RecurringSession.class));
        assertEquals(DayOfWeek.WEDNESDAY, recurringSessionUpdated.dayOfWeek());
        assertEquals(LocalTime.of(19, 0), recurringSessionUpdated.startTime());
        assertEquals(LocalTime.of(20, 0), recurringSessionUpdated.endTime());
    }
}
