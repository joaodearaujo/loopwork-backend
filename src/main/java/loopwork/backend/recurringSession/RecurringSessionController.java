package loopwork.backend.recurringSession;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients/{clientId}/recurring-sessions")
public class RecurringSessionController {

    private final RecurringSessionService service;

    public RecurringSessionController(RecurringSessionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RecurringSessionResponse> create(
            @PathVariable String clientId,
            @Valid @RequestBody RecurringSessionRequest request
    ) {
        RecurringSessionResponse response = service.create(request, clientId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<RecurringSessionResponse>> list(@PathVariable String clientId) {
        List<RecurringSessionResponse> sessions = service.getRecurringSessionsOfClient(clientId);
        return ResponseEntity.ok(sessions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecurringSessionResponse> update(
            @PathVariable String id,
            @Valid @RequestBody UpdateRecurringSessionRequest request
    ) {
        RecurringSessionResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }
}