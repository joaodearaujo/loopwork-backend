package loopwork.backend.occurrence;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/occurrences")
public class OccurrenceController {

    private final OccurrenceService service;

    public OccurrenceController(OccurrenceService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<OccurrenceResponse>> list(
            @RequestParam String professionalId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        List<OccurrenceResponse> occurrences = service.getOccurrencesOfProfessional(professionalId, start, end);
        return ResponseEntity.ok(occurrences);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OccurrenceResponse> cancel(@PathVariable String id) {
        OccurrenceResponse response = service.cancel(id);
        return ResponseEntity.ok(response);
    }
}