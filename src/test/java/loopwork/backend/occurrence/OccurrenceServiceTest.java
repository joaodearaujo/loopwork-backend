package loopwork.backend.occurrence;

import loopwork.backend.recurringSession.RecurringSession;
import loopwork.backend.recurringSession.RecurringSessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OccurrenceServiceTest {

    @Mock
    OccurrenceRepository occurrenceRepository;

    @Mock
    RecurringSessionService recurringSessionService;

    @InjectMocks
    OccurrenceService occurrenceService;

    @Test
    void shouldMaterializeUpcomingOccurrenceWhenNoneExists() {
        RecurringSession session = new RecurringSession(
                null, DayOfWeek.TUESDAY,
                LocalTime.of(18, 0), LocalTime.of(19, 0),
                LocalDate.now(), null
        );

        when(recurringSessionService.findAllActive()).thenReturn(List.of(session));
        when(occurrenceRepository.existsByRecurringSessionIdAndDate(any(), any())).thenReturn(false);

        occurrenceService.materializeUpcomingOccurrences();

        verify(occurrenceRepository, atLeastOnce()).save(any(Occurrence.class));
    }

    @Test
    void shouldNotMaterializeWhenOccurrenceAlreadyExists() {
        RecurringSession session = new RecurringSession(
                null, DayOfWeek.TUESDAY,
                LocalTime.of(18, 0), LocalTime.of(19, 0),
                LocalDate.now(), null
        );

        when(recurringSessionService.findAllActive()).thenReturn(List.of(session));
        when(occurrenceRepository.existsByRecurringSessionIdAndDate(any(), any())).thenReturn(true);

        occurrenceService.materializeUpcomingOccurrences();

        verify(occurrenceRepository, never()).save(any(Occurrence.class));
    }


}
