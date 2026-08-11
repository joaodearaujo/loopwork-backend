package loopwork.backend.occurrence;

import loopwork.backend.professional.ProfessionalService;
import loopwork.backend.recurringSession.RecurringSession;
import loopwork.backend.recurringSession.RecurringSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class OccurrenceService {

    private static final Logger logger = LoggerFactory.getLogger(OccurrenceService.class);
    private final OccurrenceRepository occurrenceRepository;
    private final RecurringSessionService recurringSessionService;
    private final ProfessionalService professionalService;

    public OccurrenceService(
            OccurrenceRepository occurrenceRepository,
            RecurringSessionService recurringSessionService,
            ProfessionalService professionalService
    ) {
        this.occurrenceRepository = occurrenceRepository;
        this.recurringSessionService = recurringSessionService;
        this.professionalService = professionalService;
    }

    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void materializeUpcomingOccurrences() {
        logger.info("Starting occurrence materialization job");

        List<RecurringSession> activeSessions = recurringSessionService.findAllActive();
        LocalDate today = LocalDate.now();
        LocalDate limit = today.plusWeeks(8);

        for (RecurringSession session : activeSessions) {
            LocalDate currentDate = today.with(TemporalAdjusters.nextOrSame(session.getDayOfWeek()));

            while (!currentDate.isAfter(limit)) {

                if (currentDate.isBefore(session.getEffectiveStartDate())) {
                    currentDate = currentDate.plusWeeks(1);
                    continue;
                }

                if (session.getEffectiveEndDate() != null && currentDate.isAfter(session.getEffectiveEndDate())) {
                    break;
                }

                boolean alreadyExists = occurrenceRepository
                        .existsByRecurringSessionIdAndDate(session.getId(), currentDate);

                if (!alreadyExists) {
                    Occurrence occurrence = new Occurrence(
                            session,
                            currentDate,
                            session.getStartTime(),
                            session.getEndTime(),
                            OccurrenceStatus.SCHEDULED
                    );
                    occurrenceRepository.save(occurrence);
                }

                currentDate = currentDate.plusWeeks(1);
                logger.info("Occurrence materialization job completed.");
            }
        }
    }

    public List<OccurrenceResponse> getOccurrencesOfProfessional(String professionalId, LocalDate start, LocalDate end) {
        professionalService.findByIdOrThrow(professionalId);

        List<Occurrence> occurrences = occurrenceRepository.findByProfessionalIdAndDateRange(professionalId, start, end);

        return occurrences
                .stream()
                .map(OccurrenceResponse::fromEntity)
                .toList();
    }

    public Page<OccurrenceResponse> getOccurrencesOfProfessionalByPage(String professionalId, LocalDate start, LocalDate end, Pageable pageable) {
        professionalService.findByIdOrThrow(professionalId);

        Page<Occurrence> occurrences = occurrenceRepository.findByProfessionalIdAndDateRange(professionalId, start, end, pageable);

        return occurrences.map(OccurrenceResponse::fromEntity);
    }

    @Transactional
    public OccurrenceResponse cancel(String occurrenceId) {
        Occurrence occurrence = findByIdOrThrow(occurrenceId);

        occurrence.cancel();

        Occurrence updated = occurrenceRepository.save(occurrence);

        return OccurrenceResponse.fromEntity(updated);
    }

    public OccurrenceResponse reschedule(RescheduleRequest rescheduleRequest, String occurrenceId) {
        Occurrence occurrence = findByIdOrThrow(occurrenceId);

        occurrence.reschedule(
                rescheduleRequest.newDate(),
                rescheduleRequest.newStartTime(),
                rescheduleRequest.newEndTime()
        );

        Occurrence rescheduled = occurrenceRepository.save(occurrence);

        return OccurrenceResponse.fromEntity(rescheduled);
    }

    public Occurrence findByIdOrThrow(String id) {
        return occurrenceRepository.findById(id)
                .orElseThrow(() -> new OccurrenceNotFoundException(id));
    }
}
