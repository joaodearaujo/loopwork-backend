package loopwork.backend.recurringSession;
import loopwork.backend.client.Client;
import loopwork.backend.client.ClientRepository;
import loopwork.backend.client.ClientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class RecurringSessionService {
    private final RecurringSessionRepository repository;
    private final ClientService clientService;

    public RecurringSessionService(RecurringSessionRepository repository, ClientService clientService) {
        this.repository = repository;
        this.clientService = clientService;
    }

    public RecurringSessionResponse create(RecurringSessionRequest request, String clientId) {
        Client client = clientService.findByIdOrThrow(clientId);

        RecurringSession newSession = new RecurringSession(
                client,
                request.dayOfWeek(),
                request.startTime(),
                request.endTime(),
                request.effectiveStartDate(),
                request.effectiveEndDate()
        );

        RecurringSession savedSession = repository.save(newSession);

        return RecurringSessionResponse.fromEntity(savedSession);
    }

    public List<RecurringSessionResponse> getRecurringSessionsOfClient(String clientId) {
        Client client = clientService.findByIdOrThrow(clientId);

        return repository.findByClientId(client.getId())
                .stream()
                .map(RecurringSessionResponse::fromEntity)
                .toList();
    }

    @Transactional
    public RecurringSessionResponse update(String recurringSessionId, UpdateRecurringSessionRequest updateRequest) {
        RecurringSession recurringSession = findByIdOrThrow(recurringSessionId);

        recurringSession.updateDayOfWeek(updateRequest.dayOfWeek());
        recurringSession.updateSchedule(updateRequest.startTime(), updateRequest.endTime());
        recurringSession.updateEffectivePeriod(updateRequest.effectiveStartDate(), updateRequest.effectiveEndDate());

        RecurringSession updatedRecurringSession = repository.save(recurringSession);
        return RecurringSessionResponse.fromEntity(updatedRecurringSession);
    }

    public List<RecurringSession> findAllActive() {
        return repository.findByEffectiveEndDateIsNullOrEffectiveEndDateGreaterThanEqual(LocalDate.now());
    }

    public RecurringSession findByIdOrThrow(String recurringSessionId) {
        return repository.findById(recurringSessionId)
                .orElseThrow(() -> new RecurringSessionNotFoundException(recurringSessionId));
    }
}
